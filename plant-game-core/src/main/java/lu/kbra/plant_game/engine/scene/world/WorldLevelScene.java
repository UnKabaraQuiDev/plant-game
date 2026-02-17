package lu.kbra.plant_game.engine.scene.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
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
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.base.entity.go.obj.water.NeedsRandomTick;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.data.AttributeLocation;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainEdgeMesh;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainEdgeObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainGameObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainHighlightObject;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
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
import lu.kbra.standalone.gameengine.scene.EntityContainer;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class WorldLevelScene extends Scene3D implements ActiveModalController {

	private static final int CAMERA_MOVEMENT_SPEED = 10;
	private static final long RANDOM_TICK_DELAY = 1000;

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

	private ParticleManager particleManager;

	private final Map<Class<? extends Modal>, Modal> modals = Collections.synchronizedMap(new HashMap<>());
	private Modal activeModal;

	private final WeakHashMap<NeedsRandomTick, Long> needsRandomTick = new WeakHashMap<>();

	public WorldLevelScene(final String name, final CacheManager parentCache) {
		super(name);
		this.worldCache = new CacheManager(name, parentCache);

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

		final ObjectTriggerLatch<WorldLevelScene> latch = new ObjectTriggerLatch<>(2, this);

		new TaskFuture<>(render, (Runnable) () -> {
			this.particleManager = new ParticleManager(this.getCache().getParent(), this);
			latch.trigger(null);
		}).push();

		this.initTerrain(workers, render, Optional.of(gameData), gameData.getLevelData(), worldProgress).then(c -> {
			this.setTerrain(c.get());
		}).latch(latch);
		return latch;
	}

	protected ObjectTriggerLatch<ObjectPointer<TerrainGameObject>> initTerrain(
			final Dispatcher workers,
			final Dispatcher render,
			final Optional<GameData> gameData,
			final LevelData levelData,
			final IntPointer worldProgress) {
		final ObjectPointer<TerrainGameObject> data = new ObjectPointer<>();
		final ObjectTriggerLatch<ObjectPointer<TerrainGameObject>> latch = new ObjectTriggerLatch<>(1, data);

		new TaskFuture<>(workers, (Supplier<WorldGenerator>) () -> {
			final WorldGenerator wg = levelData.getWorldGenerator();
			wg.compute(worldProgress);
			return wg;
		}).then(render, (Function<WorldGenerator, Pair<WorldGenerator, TerrainMesh>>) wg -> {
			return Pairs.readOnly(wg, wg.generateMesh(worldProgress, this.getCache()));
		}).then(workers, (Function<Pair<WorldGenerator, TerrainMesh>, WorldGenerator>) pair -> {
			data.set(new TerrainGameObject("terrain-" + levelData.getInternalName(), pair.getValue()));
			data.get().getTransform().translationSet(-pair.getValue().getWidth() / 2, 0, -pair.getValue().getLength() / 2);
			data.get().getTransform().rotationPivotSub(data.get().getTransform().getTranslation());
			data.get().getTransform().scalePivotSub(data.get().getTransform().getTranslation());
			data.get().getTransform().updateMatrix();
			return pair.getKey();
		}).then(render, (Function<WorldGenerator, Pair<WorldGenerator, TerrainEdgeMesh>>) wg -> {
			return Pairs.readOnly(wg, wg.generateEdgeMesh(worldProgress, this.getCache()));
		}).then(workers, (Function<Pair<WorldGenerator, TerrainEdgeMesh>, WorldGenerator>) pair -> {
			data.get().setTerrainEdgeEntity(new TerrainEdgeObject("terrain-edge-" + levelData.getInternalName(), pair.getValue()));
			return pair.getKey();
		}).then(render, (Function<WorldGenerator, Pair<WorldGenerator, Mesh>>) wg -> {
			return Pairs.readOnly(wg, wg.generateHighlightMesh(worldProgress, this.getCache()));
		}).then(workers, (Consumer<Pair<WorldGenerator, Mesh>>) pair -> {
			data.get()
					.setTerrainHighlightEntity(
							new TerrainHighlightObject("terrain-highlight-" + levelData.getInternalName(), pair.getValue()));
		}).then(render, (Supplier<QuadLoadedMesh>) () -> {
			final int width = data.get().getMesh().getWidth();
			final int length = data.get().getMesh().getLength();
			final QuadLoadedMesh mesh = new QuadLoadedMesh("water-" + levelData.getInternalName() + "-" + width + "x" + length,
					null,
					new Vector2f(width, length));
			this.getCache().addMesh(mesh);
			worldProgress.add(100);
			return mesh;
		}).then(workers, (Consumer<QuadLoadedMesh>) mesh -> {
			data.get().setWaterLevel(new MeshGameObject("water-" + levelData.getInternalName(), mesh));
			data.get()
					.getTerrainWaterObject()
					.setTransform(new Transform3D(new Vector3f(mesh.getSize().x() / 2,
							gameData.map(c -> c.getCurrentWaterLevel()).orElse(levelData.getWorld().getWaterLevel().getMin()),
							mesh.getSize().y() / 2)));
			data.get().getTerrainWaterObject().setObjectId(WATER_ID);
			data.get().getTerrainWaterObject().setColorMaterial(ColorMaterial.BLUE);

			worldProgress.add(100);

			latch.trigger(null);
		}).push();

		return latch;
	}

	protected boolean shouldStartPlaceModal = false;

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

		if (!this.hasActiveModal() && inputHandler.isMouseButtonPressedOnce(DefaultKeyOption.PLACE)) {
			this.shouldStartPlaceModal = true;
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

		if (this.shouldStartPlaceModal) {
			final MoveBuildingModal modal = this.getModal(MoveBuildingModal.class);

			this.getClickedObject(workers, compositor).then(workers, (Consumer<Optional<PlaceableObject>>) obj -> obj.ifPresent(o -> {
				modal.setAttachedObject(o);
				this.startModal(modal);
			})).push();

			this.shouldStartPlaceModal = false;
		}

		final float time = (float) inputHandler.getGameEngine().getTotalTime();
		final float dTime = inputHandler.dTime();

		this.forEach(e -> this.updateEntity(inputHandler, e, time));

		synchronized (this.needsRandomTick) {
			final long now = System.currentTimeMillis();

			for (Map.Entry<NeedsRandomTick, Long> entry : this.needsRandomTick.entrySet()) {
				if (now - entry.getValue() > RANDOM_TICK_DELAY) {
					entry.getKey().randomTick(inputHandler, this);
					entry.setValue(now);
				}
			}
		}

		final Camera3D camera = super.getCamera();
		camera.getProjection().setFov(camera.getProjection().getFov() + this.fovDiff);
		camera.getRotation().rotateY((float) Math.toRadians(this.rotation * 50 * dTime));
		camera.getPosition()
				.fma(dTime * CAMERA_MOVEMENT_SPEED, this.posAdd.rotateY(-camera.getRotation().getEulerAnglesXYZ(new Vector3f()).y()));
		camera.updateMatrix();

		if (this.getTerrain() != null) {
			this.getTerrain().getMesh().flushColorMaterial();
		}
	}

	protected void updateEntity(final WindowInputHandler inputHandler, final SceneEntity e, final float time) {
		if (e instanceof final EntityContainer<?> ec) {
			ec.forEach(e2 -> this.updateEntity(inputHandler, e2, time));
		}

		if (e instanceof final NeedsUpdate needsUpdate) {
			needsUpdate.update(inputHandler);
		}

		if (e instanceof final AnimatedTransformOwner ato) {
			ato.computeAnimatedTransform(time);
		}
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

		if (this.particleManager != null) {
			this.particleManager.render(dTime);
		}
	}

	@Deprecated
	public <T extends NeedsRandomTick> T addRandomTick(final T obj) {
		synchronized (this.needsRandomTick) {
			this.needsRandomTick.put(obj, System.currentTimeMillis());
		}
		return obj;
	}

	@Override
	public <T extends SceneEntity> T add(final T entity) {
		if (entity instanceof NeedsRandomTick nrt) {
			synchronized (this.needsRandomTick) {
				this.needsRandomTick.put(nrt, 0L);
			}
		}
		return super.add(entity);
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

	@Deprecated
	public <T extends MeshGameObject> T setWaterLevel(final T waterLevel) {
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
