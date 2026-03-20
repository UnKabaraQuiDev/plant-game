package lu.kbra.plant_game.engine.scene.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.datastructure.list.WeakArrayList;
import lu.kbra.pclib.datastructure.list.WeakList;
import lu.kbra.pclib.datastructure.pair.Pair;
import lu.kbra.pclib.datastructure.pair.Pairs;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.base.entity.go.obj.StarterPodObject;
import lu.kbra.plant_game.base.entity.go.obj.energy.ResourceProducer;
import lu.kbra.plant_game.base.entity.go.obj.water.NeedsRandomTick;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.GenericGameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.data.AttributeLocation;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.impl.ResourceContainer;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainEdgeMesh;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainEdgeObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainGameObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainHighlightObject;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.data.LevelData.World.StarterPod;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator;
import lu.kbra.plant_game.engine.scene.world.particle.ParticleManager;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.registry.GameObjectRegistry;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.BoundingBox;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadLoadedMesh;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.scene.EntityContainer;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class WorldLevelScene extends Scene3D implements ActiveModalController, SunLightOwner {

	private static final int CAMERA_MOVEMENT_SPEED = 10;

	public static final Vector3ic WATER_ID = new Vector3i(2, 0, 0);

	private final CacheManager worldCache;

	private GenericGameObject waterLevel;
	private TerrainGameObject terrain;

	private Vector3f lightColor = new Vector3f(1);
	private Vector3f lightDirection = new Vector3f(0.8f, 0.5f, 0.5f).normalize();
	private float ambientLight = 0.25f;
	private float lightIntensity = 1f;
	private final Matrix4fc lightViewMatrix = new Matrix4f();
	private final Matrix4fc lightProjectionMatrix = new Matrix4f();
	private final Matrix4fc lightSpaceMatrix = new Matrix4f();

	private Vector2f windDirection = new Vector2f(1, 1).normalize();

	private final Vector3f posAdd = new Vector3f();
	private float rotation = 0;
	private float fovDiff = 0;

	private ParticleManager particleManager;

	private final Map<Class<? extends Modal>, Modal> modals = Collections.synchronizedMap(new HashMap<>());
	private Modal activeModal;

	private GameData gameData;

	private final Map<NeedsRandomTick, Long> needsRandomTick = new WeakHashMap<>();
	private final WeakList<ResourceProducer> resourceProducing = new WeakArrayList<>();
	private final Map<ResourceType, Integer> maxResources = new HashMap<>();

	public WorldLevelScene(final String name, final CacheManager parentCache) {
		super(name);
		this.worldCache = new CacheManager(name, parentCache);

		this.getCamera().setPosition(new Vector3f(-20, 25, 20).mul(1.5f));
		this.getCamera().lookAt(this.getCamera().getPosition(), new Vector3f(0, 0, 0)).updateMatrix();
		this.getCamera().getProjection().setFov((float) Math.toRadians(40));
		this.getLightDirection().set(new Vector3f(0.5f, 0.5f, 0.5f).normalize());

		this.recomputeLightMatrices(new BoundingBox(new Vector3f(-1), new Vector3f(1)), new Vector3f(0));

		this.maxResources.put(DefaultResourceType.MONEY, Integer.MAX_VALUE);
	}

	public ObjectTriggerLatch<WorldLevelScene> init(
			final Dispatcher workers,
			final Dispatcher render,
			final GameData gameData,
			final IntPointer worldProgress) {
		this.gameData = gameData;

		this.registerModal(new MoveBuildingModal(this, PGLogic.INSTANCE.getCompositor()));

		final ObjectTriggerLatch<WorldLevelScene> latch = new ObjectTriggerLatch<>(2, this);

		new TaskFuture<>(render, (Runnable) () -> {
			this.particleManager = new ParticleManager(this.getCache().getParent(), this);
			latch.trigger(null);
		}).push();

		this.initTerrain(workers, render, Optional.of(gameData), gameData.getLevelData(), worldProgress).latch(latch);
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
			data
					.get()
					.setTerrainHighlightEntity(
							new TerrainHighlightObject("terrain-highlight-" + levelData.getInternalName(), pair.getValue()));
		}).then(render, (Supplier<QuadLoadedMesh>) () -> {
			final int width = data.get().getMesh().getWidth();
			final int length = data.get().getMesh().getLength();
			final QuadLoadedMesh mesh = new QuadLoadedMesh("water-" + levelData.getInternalName() + "-" + width + "x" + length, null,
					new Vector2f(width, length));
			this.getCache().addMesh(mesh);
			worldProgress.add(100);
			return mesh;
		}).then(workers, (Consumer<QuadLoadedMesh>) mesh -> {
			data.get().setWaterLevel(new MeshGameObject("water-" + levelData.getInternalName(), mesh));
			data
					.get()
					.getTerrainWaterObject()
					.setTransform(new Transform3D(new Vector3f(mesh.getSize().x() / 2,
							gameData.map(c -> c.getCurrentWaterLevel()).orElse(levelData.getWorld().getWaterLevel().getMin()),
							mesh.getSize().y() / 2)));
			data.get().getTerrainWaterObject().setObjectId(WATER_ID);
			data.get().getTerrainWaterObject().setColorMaterial(ColorMaterial.BLUE);

			worldProgress.add(100);
		}).then(workers, (Runnable) () -> {
			final StarterPod pod = levelData.getWorld().getStarterPod();

			this.setTerrain(data.get());

			GameObjectFactory
					.create(GameObjectRegistry.<StarterPodObject>getClass(pod.getPodClass()))
					.set(i -> i.setTransform(new Transform3D()))
					.postInit(c -> data.get().add(c))
					.postInit(c -> c.placeDown(this.terrain, pod.getTile(), pod.getDirection()))
					.postInit(c -> c
							.getTransform()
							.translationAdd(data.get().getMesh().getBoundingBox().getSize().x() / 2,
									0,
									data.get().getMesh().getBoundingBox().getSize().z() / 2)
							.updateMatrix())
					.postInit(c -> worldProgress.add(100))
					.latch(latch)
					.push();
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

	protected final ResourceBuffer resourceBuffer = new ResourceBuffer();

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

		// this also clears
		this.resourceBuffer.copyFrom(this.gameData);

		this.forEach(e -> this.updateEntity(inputHandler, e, time));

		synchronized (this.needsRandomTick) {
			final long now = System.currentTimeMillis();

			for (Entry<NeedsRandomTick, Long> entry : this.needsRandomTick.entrySet()) {
				final NeedsRandomTick obj = entry.getKey();

				while (now >= entry.getValue()) {
					obj.randomTick(inputHandler, this);
					entry.setValue(entry.getValue() + obj.getRandomTickDuration());
				}
			}
		}

		this.resourceBuffer.clear();

		synchronized (this.resourceProducing) {
			this.resourceProducing.forEach(c -> c.produce(dTime, this.resourceBuffer));
		}

		final Map<ResourceType, Float> result = this.gameData.getResources();
		this.resourceBuffer.getProduced().forEach((type, value) -> result.merge(type, value, Float::sum));
		synchronized (this.maxResources) {
			result.entrySet().forEach(e -> e.setValue(Math.clamp(e.getValue(), 0, this.maxResources.getOrDefault(e.getKey(), 0))));
		}

		final Camera3D camera = super.getCamera();
		camera.getProjection().setFov(camera.getProjection().getFov() + this.fovDiff);
		camera.getRotation().rotateY((float) Math.toRadians(this.rotation * 50 * dTime));
		camera
				.getPosition()
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
		return this
				.getClickedId(workers, compositor)
				.then(workers,
						(Function<Vector3i, Optional<PlaceableObject>>) ids -> super.getEntities()
								.values()
								.parallelStream()
								.filter(e -> e instanceof GenericGameObject && e instanceof PlaceableObject
										&& ((GenericGameObject) e).getObjectIdLocation() == AttributeLocation.ENTITY)
								.filter(e -> ids.equals(((GenericGameObject) e).getObjectId()))
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

		return !this.getEntities().values().stream().map(GenericGameObject.class::cast).anyMatch(c -> {
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
			this.needsRandomTick.put(obj, System.currentTimeMillis() + obj.getRandomTickDuration());
		}
		return obj;
	}

	@Override
	protected <T extends SceneEntity> void onAdd(final T entity) {
		if (entity instanceof final NeedsRandomTick nrt) {
			synchronized (this.needsRandomTick) {
				this.needsRandomTick.put(nrt, System.currentTimeMillis() + nrt.getRandomTickDuration());
			}
		}
		if (entity instanceof final ResourceProducer rp) {
			synchronized (this.resourceProducing) {
				this.resourceProducing.add(rp);
			}
		}
		if (entity instanceof final ResourceContainer rc) {
			synchronized (this.maxResources) {
				rc.getMaxResources().forEach((k, v) -> this.maxResources.merge(k, v, Integer::sum));
			}
		}
	}

	@Override
	protected <T extends SceneEntity> void onRemove(final T entity) {
		if (entity instanceof final NeedsRandomTick nrt) {
			synchronized (this.needsRandomTick) {
				this.needsRandomTick.remove(nrt);
			}
		}
		if (entity instanceof final ResourceProducer rp) {
			synchronized (this.resourceProducing) {
				this.resourceProducing.remove(rp);
			}
		}
		if (entity instanceof final ResourceContainer rc) {
			synchronized (this.maxResources) {
				rc.getMaxResources().forEach((k, v) -> this.maxResources.merge(k, -v, Integer::sum));
			}
		}
	}

	public void recomputeMaxResources() {
		synchronized (this.maxResources) {
			this.maxResources.clear();
		}
		this.forEach(se -> {
			if (se instanceof final ResourceContainer rc) {
				synchronized (this.maxResources) {
					rc.getMaxResources().forEach((k, v) -> this.maxResources.merge(k, v, Integer::sum));
				}
			}
		});
	}

	public void recomputeResourceProducer() {
		synchronized (this.resourceProducing) {
			this.resourceProducing.clear();
		}
		this.forEach(se -> {
			if (se instanceof final ResourceProducer rp) {
				synchronized (this.resourceProducing) {
					this.resourceProducing.add(rp);
				}
			}
		});
	}

	public void recomputeNeedsRandomTick() {
		synchronized (this.needsRandomTick) {
			this.needsRandomTick.clear();
		}
		this.forEach(se -> {
			if (se instanceof final NeedsRandomTick rp) {
				synchronized (this.needsRandomTick) {
					this.needsRandomTick.put(rp, System.currentTimeMillis() + rp.getRandomTickDuration());
				}
			}
		});
	}

	@Override
	public Modal getActiveModal() {
		return this.activeModal;
	}

	@Override
	public void setActiveModal(final Modal activeModal) {
		this.activeModal = activeModal;
	}

	public GenericGameObject getWaterLevel() {
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
		this.recomputeLightMatrices(terrain.getMesh().getBoundingBox(), terrain.getAbsoluteTransform().getTranslation(new Vector3f()));
	}

	public CacheManager getCache() {
		return this.worldCache;
	}

	@Override
	public Vector3f getLightColor() {
		return this.lightColor;
	}

	@Override
	public void setLightColor(final Vector3f lightColor) {
		this.lightColor = lightColor;
	}

	@Override
	public Vector3f getLightDirection() {
		return this.lightDirection;
	}

	@Override
	public void setLightDirection(final Vector3f lightDirection) {
		this.lightDirection = lightDirection;
	}

	@Override
	public float getAmbientLight() {
		return this.ambientLight;
	}

	@Override
	public void setAmbientLight(final float ambientLight) {
		this.ambientLight = ambientLight;
	}

	@Override
	public float getLightIntensity() {
		return this.lightIntensity;
	}

	@Override
	public void setLightIntensity(final float lightIntensity) {
		this.lightIntensity = lightIntensity;
	}

	@Override
	public Matrix4fc getLightViewMatrix() {
		return this.lightViewMatrix;
	}

	@Override
	public Matrix4fc getLightProjectionMatrix() {
		return this.lightProjectionMatrix;
	}

	@Override
	public Matrix4fc getLightSpaceMatrix() {
		return this.lightSpaceMatrix;
	}

	public Vector2f getWindDirection() {
		return this.windDirection;
	}

	public void setWindDirection(final Vector2f windDirection) {
		this.windDirection = windDirection;
	}

	@Deprecated
	public float getWaterHeight() {
		return this.getWaterLevel() == null ? 0
				: (this.getWaterLevel().getTransform().getTranslation().y() + this.getTerrain().getTransform().getTranslation().y())
						/ this.getTerrain().getTransform().getScale().y();
	}

	public GameData getGameData() {
		return this.gameData;
	}

	public ResourceBuffer getResourceBuffer() {
		assert Thread.currentThread() == GameLogic.INSTANCE.getGameEngine().getUpdateThread()
				: "Access denied from: " + Thread.currentThread().getName();
		return this.resourceBuffer;
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
