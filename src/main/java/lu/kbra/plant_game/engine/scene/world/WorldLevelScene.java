package lu.kbra.plant_game.engine.scene.world;

import java.util.concurrent.CountDownLatch;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.impl.ThrowingConsumer;
import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.energy.SolarPanelObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterWheelObject;
import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.generator.ImageWorldGenerator;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator.TerrainMaterialType;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.gl.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class WorldLevelScene extends Scene3D {

	private LevelData levelData;

	private CacheManager worldCache;

	private GameObject waterLevel;
	private TerrainObject terrain;

	private Vector3f lightColor = new Vector3f(1), lightDirection = new Vector3f(0.8f, 0.5f, 0.5f).normalize();
	private float ambientLight = 0.25f;

	private final Vector3f posAdd = new Vector3f();
	private float rotation = 0;
	private float fovDiff = 0;

	private boolean movingObject = false;
	private PlaceableObject attachedObject = null;
	private Direction targetRotation = Direction.DEFAULT();
	private TaskFuture<?, Void>.TaskState<Void> moveObjectTaskState;

	public WorldLevelScene(String name, CacheManager parent) {
		super(name);
		this.worldCache = new CacheManager(name, parent);
	}

	public void init(Dispatcher workers, Dispatcher renderDispatcher) {
		final CountDownLatch terrainLatch = new CountDownLatch(1);

		moveObjectTaskState = new TaskFuture<Void, Void>(workers, () -> {
			terrainLatch.await();
		}).push();

		new TaskFuture<>(workers, () -> {
			final WorldGenerator worldGenerator = new ImageWorldGenerator("classpath:/maps/world_map.png", 4 / 255f);
			GlobalLogger.info("Generating world...");
			final long time = PCUtils.nanoTime(() -> worldGenerator.compute());
			GlobalLogger.info("World generated in " + (time / 1e6) + " ms");
			return worldGenerator;
		}).then(renderDispatcher, (ThrowingFunction<WorldGenerator, TerrainMesh, Throwable>) (worldGenerator) -> {
			GlobalLogger.info("Generating mesh...");
			final Pair<TerrainMesh, Long> mesh = PCUtils.nanoTime(() -> worldGenerator.generateMesh(this.getCache()));
			GlobalLogger.info("Mesh generated in " + (mesh.getValue() / 1e6) + " ms");
			return mesh.getKey();
		}, 0).then(workers, (mesh) -> {
			GlobalLogger.info("Creating entity...");
			final long time = PCUtils.nanoTime((Runnable) () -> {
				final TerrainObject terrainEntity = new TerrainObject("terrain", mesh);
				terrainEntity.getTransform().getTranslation().set(-mesh.getWidth() / 2, 0, -mesh.getLength() / 2);
				terrainEntity.getTransform().updateMatrix();
				this.setTerrain(terrainEntity);
			});
			GlobalLogger.info("Entity created in " + (time / 1e6) + " ms");
		}).then(workers, () -> {
			terrainLatch.countDown();

			new TaskFuture<>(renderDispatcher, () -> {
				GlobalLogger.info("Generating water mesh...");
				final Pair<Mesh, Long> meshTime = PCUtils
						.nanoTime(() -> new QuadMesh("water", null, new Vector2f(((TerrainMesh) this.getTerrain().getMesh()).getWidth(),
								((TerrainMesh) this.getTerrain().getMesh()).getLength())));
				this.getCache().addMesh(meshTime.getKey());
				GlobalLogger.info("Water mesh generated in " + (meshTime.getValue() / 1e6) + " ms");
				return meshTime.getKey();
			})
					.then(workers,
							(ThrowingConsumer<Mesh, Throwable>) (mesh) -> this
									.setWaterLevel(new GameObject("water", mesh, new Transform3D(new Vector3f(0, 0.9f, 0)),
											new Vector3i(2, 0, 0), TerrainMaterialType.WATER.getId())))
					.push();

			GameObjectFactory
					.create(WaterTowerObject.class, this, new Transform3D())
					.then(workers, (ThrowingConsumer<WaterTowerObject, Throwable>) (obj) -> {
						final Vector2i pos = new Vector2i(0, 0);
						while (!obj.isPlaceable(this, pos, Direction.NONE)) {
							pos.x++;
							if (pos.x >= this.getTerrain().getMesh().getWidth()) {
								pos.x = 0;
								pos.y++;
							}
							if (pos.y >= this.getTerrain().getMesh().getLength()) {
								break;
							}
						}
						obj.placeDown(this, pos, Direction.NONE);
					})
					.push();

			GameObjectFactory
					.create(WaterTowerObject.class, this, new Transform3D())
					.then(workers, (ThrowingConsumer<WaterTowerObject, Throwable>) (obj) -> {
						obj.placeDown(this, new Vector2i(11, 15), Direction.NONE);
					})
					.push();

			GameObjectFactory
					.create(SolarPanelObject.class, this, new Transform3D())
					.then(workers,
							(ThrowingConsumer<SolarPanelObject, Throwable>) (obj) -> obj
									.placeDown(this, new Vector2i(15, 5), Direction.NONE))
					.push();

			GameObjectFactory
					.create(WaterWheelObject.class, this, new Transform3D())
					.then(workers,
							(ThrowingConsumer<WaterWheelObject, Throwable>) (obj) -> obj
									.placeDown(this, new Vector2i(11, 7), Direction.WEST))
					.push();

			GameObjectFactory
					.create(WaterWheelObject.class, this, new Transform3D())
					.then(workers,
							(ThrowingConsumer<WaterWheelObject, Throwable>) (obj) -> obj
									.placeDown(this, new Vector2i(13, 8), Direction.WEST))
					.push();

		}).push();
	}

	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		fovDiff = (float) (inputHandler.getMouseScroll().y * 0.05f);

		posAdd.zero();
		rotation = 0;

		if (!frameState.uiSceneCaughtKeyboardInput) {
			if (inputHandler.isKeyHeld(GLFW.GLFW_KEY_W)) {
				posAdd.z -= 1;
			}
			if (inputHandler.isKeyHeld(GLFW.GLFW_KEY_S)) {
				posAdd.z += 1;
			}
			if (inputHandler.isKeyHeld(GLFW.GLFW_KEY_A)) {
				posAdd.x -= 1;
			}
			if (inputHandler.isKeyHeld(GLFW.GLFW_KEY_D)) {
				posAdd.x += 1;
			}
			if (inputHandler.isKeyHeld(GLFW.GLFW_KEY_Q)) {
				rotation -= 1;
			}
			if (inputHandler.isKeyHeld(GLFW.GLFW_KEY_E)) {
				rotation += 1;
			}

			if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_T)) {
				targetRotation = targetRotation.getClockwise();
			}
			if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_R)) {
				targetRotation = targetRotation.getCounterClockwise();
			}
		}

		if (!frameState.uiSceneCaughtMouseInput) {
			if (inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				movingObject = !movingObject;
			}
		}
	}

	public void update(
			final WindowInputHandler inputHandler,
			float dTime,
			DeferredCompositor compositor,
			Dispatcher workers,
			Dispatcher renderDispatcher) {
		if (moveObjectTaskState == null || moveObjectTaskState.isDone()) {
			if (movingObject) {
				if (attachedObject == null) {
					moveObjectTaskState = new TaskFuture<Void, Void>(workers, () -> {
						compositor.pollObjectId(true);

						final Vector3i ids = new Vector3i(compositor.getObjectId().y, compositor.getObjectId().z,
								compositor.getObjectId().w);

						super.getEntities()
								.values()
								.parallelStream()
								.filter(e -> e instanceof GameObject && e instanceof PlaceableObject
										&& ((GameObject) e).getObjectIdLocation() == AttributeLocation.ENTITY);

						attachedObject = (PlaceableObject) super.getEntities()
								.values()
								.parallelStream()
								.filter(e -> e instanceof GameObject && e instanceof PlaceableObject
										&& ((GameObject) e).getObjectIdLocation() == AttributeLocation.ENTITY)
								.filter(e -> ids.equals(((GameObject) e).getObjectId()))
								.findFirst()
								.orElse(null);

						if (attachedObject == null) {
							moveObjectTaskState = null;
							movingObject = false;
						} else {
							compositor.addOutline(attachedObject, new Vector4f(1, 0, 1, 1));
						}
					}).push();
				} else {
					final Vector2f mousePos = inputHandler.getMousePosition();
					moveObjectTaskState = new TaskFuture<Void, Void>(renderDispatcher, () -> {
						final Vector2i pos = this
								.getTerrain()
								.pickTerrainCell(super.getCamera(),
										mousePos,
										compositor.getWindow().getWidth(),
										compositor.getWindow().getHeight());
						System.err.println(pos);
						if (pos != null) {
							attachedObject.placeDown(this, pos, targetRotation);
							targetRotation = Direction.DEFAULT();
						}
					}).push();
				}
			} else if (attachedObject != null) {
				compositor.removeOutline(attachedObject);
				attachedObject = null;
				moveObjectTaskState = null;
			}
		}

		final float time = (float) inputHandler.getGameEngine().getTotalTime();

		synchronized (super.getEntitiesLock()) {

			for (Entity e : this) {
				if (e instanceof AnimatedGameObject ago) {
					ago.computeAnimatedTransform(time);
				}
			}

		}

		final Camera3D camera = super.getCamera();
		camera.getProjection().setFov((float) (camera.getProjection().getFov() + fovDiff));
		camera.getPosition().fma(dTime * 10, posAdd);
		camera.getRotation().rotateY((float) Math.toRadians(rotation * 50 * dTime));
		camera.updateMatrix();
	}

	public GameObject getWaterLevel() {
		return waterLevel;
	}

	public <T extends GameObject> T setWaterLevel(T waterLevel) {
		this.waterLevel = waterLevel;
		return super.addEntity(waterLevel);
	}

	public TerrainObject getTerrain() {
		return terrain;
	}

	public void setTerrain(TerrainObject terrain) {
		this.terrain = terrain;
		super.addEntity(terrain);
	}

	public CacheManager getCache() {
		return worldCache;
	}

	public Vector3f getLightColor() {
		return lightColor;
	}

	public void setLightColor(Vector3f lightColor) {
		this.lightColor = lightColor;
	}

	public Vector3f getLightDirection() {
		return lightDirection;
	}

	public void setLightDirection(Vector3f lightDirection) {
		this.lightDirection = lightDirection;
	}

	public float getAmbientLight() {
		return ambientLight;
	}

	public void setAmbientLight(float ambientLight) {
		this.ambientLight = ambientLight;
	}

}
