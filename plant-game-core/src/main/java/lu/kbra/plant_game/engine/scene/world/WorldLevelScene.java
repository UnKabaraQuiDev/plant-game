package lu.kbra.plant_game.engine.scene.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.datastructure.pair.Pair;
import lu.kbra.pclib.datastructure.pair.Pairs;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.AnimatedMeshGameObject;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.data.AttributeLocation;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainEdgeMesh;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainEdgeObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainGameObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainHighlightObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator;
import lu.kbra.plant_game.engine.scene.world.particle.ParticleManager;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadLoadedMesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public final class WorldLevelScene extends Scene3D implements ActiveModalController {

	private static final int CAMERA_MOVEMENT_SPEED = 10;

	public static final Vector3ic WATER_ID = new Vector3i(2, 0, 0);

	private final CacheManager worldCache;

	private GameObject waterLevel;
	private TerrainGameObject terrain;

	private Vector3f lightColor = new Vector3f(1);
	private Vector3f lightDirection = new Vector3f(0.8f, 0.5f, 0.5f).normalize();
	private float ambientLight = 0.25f;

	private Vector2f windDirection = new Vector2f(1, 1).normalize();

	private final Vector3f posAdd = new Vector3f();
	private float rotation = 0;
	private float fovDiff = 0;

	private final ParticleManager particleManager;

	private final Map<Class<? extends Modal>, Modal> modals = Collections.synchronizedMap(new HashMap<>());
	private Modal activeModal;

	public WorldLevelScene(final String name, final CacheManager parentCache) {
		super(name);
		this.worldCache = new CacheManager(name, parentCache);

		this.particleManager = new ParticleManager(parentCache, this);

		this.getCamera().setPosition(new Vector3f(-20, 25, 20).mul(1.5f));
		this.getCamera().lookAt(this.getCamera().getPosition(), new Vector3f(0, 0, 0)).updateMatrix();
		this.getCamera().getProjection().setFov((float) Math.toRadians(40));
		this.getLightDirection().set(new Vector3f(0.5f, 0.5f, 0.5f).normalize());
	}

	public ObjectTriggerLatch<WorldLevelScene> init(
			final Dispatcher workers,
			final Dispatcher render,
			final GameData gameData,
			final IntPointer worldProgress) {
		this.registerModal(new MoveBuildingModal(this, PGLogic.INSTANCE.getCompositor()));

		final ObjectTriggerLatch<WorldLevelScene> latch = new ObjectTriggerLatch<>(1, this);

		final LevelData levelData = gameData.getLevelData();

		new TaskFuture<>(workers, (Supplier<WorldGenerator>) () -> {
			final WorldGenerator wg = levelData.getWorldGenerator();
			wg.compute(worldProgress);
			return wg;
		}).then(render, (Function<WorldGenerator, Pair<WorldGenerator, TerrainMesh>>) wg -> {
			return Pairs.readOnly(wg, wg.generateMesh(worldProgress, this.getCache()));
		}).then(workers, (Function<Pair<WorldGenerator, TerrainMesh>, WorldGenerator>) pair -> {
			this.setTerrain(new TerrainGameObject("terrain-" + levelData.getInternalName(), pair.getValue()));
			this.getTerrain().getTransform().translationSet(-pair.getValue().getWidth() / 2, 0, -pair.getValue().getLength() / 2);
			this.getTerrain().getTransform().updateMatrix();
			return pair.getKey();
		}).then(render, (Function<WorldGenerator, Pair<WorldGenerator, TerrainEdgeMesh>>) wg -> {
			return Pairs.readOnly(wg, wg.generateEdgeMesh(worldProgress, this.getCache()));
		}).then(workers, (Function<Pair<WorldGenerator, TerrainEdgeMesh>, WorldGenerator>) pair -> {
			final TerrainEdgeObject terrainEdges = new TerrainEdgeObject("terrain-edge-" + levelData.getInternalName(), pair.getValue());
			this.getTerrain().setTerrainEdgeEntity(terrainEdges);
			return pair.getKey();
		}).then(render, (Function<WorldGenerator, Pair<WorldGenerator, Mesh>>) wg -> {
			return Pairs.readOnly(wg, wg.generateHighlightMesh(worldProgress, this.getCache()));
		}).then(workers, (Consumer<Pair<WorldGenerator, Mesh>>) pair -> {
			final TerrainHighlightObject terrainHighlights = new TerrainHighlightObject("terrain-highlight-" + levelData.getInternalName(),
					pair.getValue());
			this.getTerrain().setTerrainHighlightEntity(terrainHighlights);
		}).then(render, (Supplier<QuadLoadedMesh>) () -> {
			final QuadLoadedMesh mesh = new QuadLoadedMesh("water",
					null,
					new Vector2f(this.getTerrain().getMesh().getWidth(), this.getTerrain().getMesh().getLength()));
			this.worldCache.addMesh(mesh);
			worldProgress.add(100);
			return mesh;
		}).then(workers, (Consumer<QuadLoadedMesh>) mesh -> {
			final MeshGameObject water = new MeshGameObject("water", mesh);
			water.setTransform(
					new Transform3D(new Vector3f(mesh.getSize().x() / 2, gameData.getCurrentWaterLevel(), mesh.getSize().y() / 2)));
			water.setObjectId(WATER_ID);
			water.setColorMaterial(ColorMaterial.BLUE);
			this.setWaterLevel(water);
			worldProgress.add(100);

			latch.trigger(null);
		}).push();

		return latch;
	}

	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		this.posAdd.zero();
		this.rotation = 0;

		if (this.activeModal != null) {
			this.activeModal.input(inputHandler, frameState);
		}

		if (!frameState.uiSceneCaughtKeyboardInput) {
			if (inputHandler.isKeyHeld(DefaultKeyOption.FORWARD)) {
				this.posAdd.z -= 1;
			}
			if (inputHandler.isKeyHeld(DefaultKeyOption.BACKWARD)) {
				this.posAdd.z += 1;
			}
			if (inputHandler.isKeyHeld(DefaultKeyOption.LEFT)) {
				this.posAdd.x -= 1;
			}
			if (inputHandler.isKeyHeld(DefaultKeyOption.RIGHT)) {
				this.posAdd.x += 1;
			}
			if (inputHandler.isKeyHeld(DefaultKeyOption.ROTATE_LEFT)) {
				this.rotation -= 1;
			}
			if (inputHandler.isKeyHeld(DefaultKeyOption.ROTATE_RIGHT)) {
				this.rotation += 1;
			}

		}

		if (!frameState.uiSceneCaughtMouseInput) {
			this.fovDiff = (float) (inputHandler.getMouseScroll().y * 0.05f);
		}
	}

	public void update(
			final WindowInputHandler inputHandler,
			final DeferredCompositor compositor,
			final Dispatcher workers,
			final Dispatcher renderDispatcher) {
		if (this.activeModal != null) {
			this.activeModal.update(inputHandler, compositor, workers, renderDispatcher);
		}

		if (!this.hasActiveModal() && inputHandler.isMouseButtonPressedOnce(DefaultKeyOption.PLACE)) {
			final MoveBuildingModal modal = this.getModal(MoveBuildingModal.class);

			this.getClickedObject(workers, compositor).then(workers, (Consumer<Optional<PlaceableObject>>) obj -> obj.ifPresent(o -> {
				modal.setAttachedObject(o);
				this.startModal(modal);
			})).push();
		}

		final float time = (float) inputHandler.getGameEngine().getTotalTime();
		final float dTime = inputHandler.dTime();

		synchronized (super.getEntitiesLock()) {

			for (final SceneEntity e : this) {
				if (e instanceof final AnimatedMeshGameObject ago) {
					ago.computeAnimatedTransform(time);
				}
			}

		}

		final Camera3D camera = super.getCamera();
		camera.getProjection().setFov(camera.getProjection().getFov() + this.fovDiff);
		camera.getRotation().rotateY((float) Math.toRadians(this.rotation * 50 * dTime));
		camera.getPosition()
				.fma(dTime * CAMERA_MOVEMENT_SPEED, this.posAdd.rotateY(-camera.getRotation().getEulerAnglesXYZ(new Vector3f()).y()));
		camera.updateMatrix();
	}

	public TaskFuture<?, Optional<PlaceableObject>> getClickedObject(final Dispatcher workers, final DeferredCompositor compositor) {
		return this.getClickedId(workers, compositor)
				.then(workers,
						(Function<Vector3i, Optional<PlaceableObject>>) ids -> super.getEntities().values()
								.parallelStream()
								.filter(e -> e instanceof GameObject && e instanceof PlaceableObject
										&& ((GameObject) e).getObjectIdLocation() == AttributeLocation.ENTITY)
								.filter(e -> ids.equals(((GameObject) e).getObjectId()))
								.map(PlaceableObject.class::cast)
								.findFirst());
	}

	public TaskFuture<?, Vector3i> getClickedId(final Dispatcher workers, final DeferredCompositor compositor) {
		return new TaskFuture<>(workers, (Supplier<Vector3i>) () -> {
			compositor.pollObjectId(true);

			return new Vector3i(compositor.getObjectId().y(), compositor.getObjectId().z(), compositor.getObjectId().w());
		});
	}

	public boolean isPlaceable(final PlaceableObject targetObject, final Vector2i targetPos, final Direction targetRotation) {
		if (!targetObject.isPlaceable(this, targetPos, targetRotation)) {
			return false;
		}

		return !this.getEntities().values().stream().map(GameObject.class::cast).anyMatch(c -> {
			if (!c.hasTransform() || !(c instanceof final PlaceableObject po) || c == targetObject) {
				return false;
			}

			final Vector2i thisPos = this.getTerrain().getCellPosition(po.getTransform().getTranslation());

			return po.intersects(thisPos, targetPos, targetObject);
		});
	}

	public void render(final float dTime) {
		if (this.activeModal != null) {
			this.activeModal.render(dTime);
		}

		this.particleManager.render(dTime);
	}

	@Override
	public Modal getActiveModal() {
		return this.activeModal;
	}

	@Override
	public void setActiveModal(final Modal activeModal) {
		this.activeModal = activeModal;
	}

	public GameObject getWaterLevel() {
		return this.waterLevel;
	}

	public <T extends GameObject> T setWaterLevel(final T waterLevel) {
		this.waterLevel = waterLevel;
		this.getTerrain().setWaterLevel(waterLevel);
		return waterLevel;
	}

	public TerrainGameObject getTerrain() {
		return this.terrain;
	}

	public void setTerrain(final TerrainGameObject terrain) {
		super.replace(this.terrain, terrain);
		this.terrain = terrain;
	}

	public CacheManager getCache() {
		return this.worldCache;
	}

	public Vector3f getLightColor() {
		return this.lightColor;
	}

	public void setLightColor(final Vector3f lightColor) {
		this.lightColor = lightColor;
	}

	public Vector3f getLightDirection() {
		return this.lightDirection;
	}

	public void setLightDirection(final Vector3f lightDirection) {
		this.lightDirection = lightDirection;
	}

	public float getAmbientLight() {
		return this.ambientLight;
	}

	public void setAmbientLight(final float ambientLight) {
		this.ambientLight = ambientLight;
	}

	public Vector2f getWindDirection() {
		return this.windDirection;
	}

	public void setWindDirection(final Vector2f windDirection) {
		this.windDirection = windDirection;
	}

	public float getWaterHeight() {
		return this.getWaterLevel() == null ? 0
				: (this.getWaterLevel().getTransform().getTranslation().y() + this.getTerrain().getTransform().getTranslation().y())
						/ this.getTerrain().getTransform().getScale().y();
	}

	public ParticleManager getParticleManager() {
		return this.particleManager;
	}

	@Override
	public Map<Class<? extends Modal>, Modal> getModals() {
		return this.modals;
	}

	@Override
	public void cleanup() {
		this.worldCache.cleanup();
		super.cleanup();
	}

}
