package lu.kbra.plant_game;

import java.util.concurrent.CountDownLatch;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.impl.ExceptionConsumer;
import lu.pcy113.pclib.impl.ExceptionFunction;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.electric.SolarPanelObject;
import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.water.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.water.WaterWheelObject;
import lu.kbra.plant_game.engine.mesh.AttributeLocation;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.ImageWorldGenerator;
import lu.kbra.plant_game.engine.scene.world.WorldGenerator;
import lu.kbra.plant_game.engine.scene.world.WorldGenerator.TerrainMaterialType;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.geom.CubeMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.gl.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TestGameLogic extends GameLogic {

	private float TOTAL_TIME = 0;

	private Dispatcher WORKERS = new WorkerDispatcher("WORKERS", 8);

	private WorldLevelScene worldScene;
	private UIScene uiScene;
	private DeferredCompositor compositor;
	private Entity cubeEntity;

	private TaskFuture<?, Void>.TaskState<Void> state;
	private TaskFuture<?, Void>.TaskState<Void> moveObjectTaskState;

	private InputHandler inputHandler;

	@Override
	public void init(GameEngine e) {
		inputHandler = new InputHandler(window);

		compositor = new DeferredCompositor(engine, e.getRenderThread());

		worldScene = new WorldLevelScene("world", cache);
		worldScene.getCamera().setPosition(new Vector3f(-20, 25, 20).mul(1.5f));
		worldScene.getCamera().lookAt(worldScene.getCamera().getPosition(), new Vector3f(0, 0, 0)).updateMatrix();
		worldScene.getCamera().getProjection().setFov((float) Math.toRadians(40));
		worldScene.getLightDirection().set(new Vector3f(0.5f, 0.5f, 0.5f).normalize());

		uiScene = new UIScene("ui", cache);

		UIObjectFactory.INSTANCE = new UIObjectFactory(uiScene.getCache(), WORKERS, RENDER_DISPATCHER);

		GameObjectFactory.INSTANCE = new GameObjectFactory(worldScene.getCache(), WORKERS, RENDER_DISPATCHER);

		final CubeMesh cubeMesh = new CubeMesh("cubeMesh", null, new Vector3f(0.5f));
		worldScene.getCache().addMesh(cubeMesh);
		cubeEntity = worldScene
				.addEntity(new GameObject("cubeEntity", cubeMesh, new Transform3D(), new Vector3i(1, 0, 255),
						TerrainMaterialType.GRASS.getId()));
	}

	final Vector3f posAdd = new Vector3f();
	float rotation = 0;
	float fovDiff = 0;
	final Vector2i mousePos = new Vector2i();
	boolean movingObject = false, prevMousePressed = false;
	PlaceableObject attachedObject = null;
	Direction targetRotation = Direction.ZERO();

	@Override
	public void input(float dTime) {
		fovDiff = (float) (window.getScroll().y * 0.05f);

		posAdd.zero();
		rotation = 0;

		if (window.getKeyState(GLFW.GLFW_KEY_W) != KeyState.RELEASE) {
			posAdd.z -= 1;
		}
		if (window.getKeyState(GLFW.GLFW_KEY_S) != KeyState.RELEASE) {
			posAdd.z += 1;
		}
		if (window.getKeyState(GLFW.GLFW_KEY_A) != KeyState.RELEASE) {
			posAdd.x -= 1;
		}
		if (window.getKeyState(GLFW.GLFW_KEY_D) != KeyState.RELEASE) {
			posAdd.x += 1;
		}
		if (window.getKeyState(GLFW.GLFW_KEY_Q) != KeyState.RELEASE) {
			rotation -= 1;
		}
		if (window.getKeyState(GLFW.GLFW_KEY_E) != KeyState.RELEASE) {
			rotation += 1;
		}

		mousePos.set((int) window.getMousePosition().x, (int) window.getMousePosition().y);

		if (inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			movingObject = !movingObject;
		}

		if (inputHandler.isKeyPressedOnce(GLFW.GLFW_KEY_T)) {
			targetRotation = targetRotation.getClockwise();
		}
		if (inputHandler.isKeyPressedOnce(GLFW.GLFW_KEY_R)) {
			targetRotation = targetRotation.getCounterClockwise();
		}

		if (targetRotation != Direction.ZERO()) {
			System.err.println(targetRotation);
		}
	}

	@Override
	public void update(float dTime) {
		if (state == null) {
			final CountDownLatch terrainLatch = new CountDownLatch(1);

			moveObjectTaskState = new TaskFuture<Void, Void>(WORKERS, () -> {
				terrainLatch.await();
			}).push();

			state = new TaskFuture<>(WORKERS, () -> {
				final WorldGenerator worldGenerator = new ImageWorldGenerator("classpath:/maps/world_map.png", 4 / 255f);
				GlobalLogger.info("Generating world...");
				final long time = PCUtils.nanoTime(() -> worldGenerator.compute());
				GlobalLogger.info("World generated in " + (time / 1e6) + " ms");
				return worldGenerator;
			}).then(RENDER_DISPATCHER, (ExceptionFunction<WorldGenerator, TerrainMesh>) (worldGenerator) -> {
				GlobalLogger.info("Generating mesh...");
				final Pair<TerrainMesh, Long> mesh = PCUtils.nanoTime(() -> worldGenerator.generateMesh(worldScene.getCache()));
				GlobalLogger.info("Mesh generated in " + (mesh.getValue() / 1e6) + " ms");
				return mesh.getKey();
			}, 0).then(WORKERS, (mesh) -> {
				GlobalLogger.info("Creating entity...");
				final long time = PCUtils.nanoTime((Runnable) () -> {
					final TerrainObject terrainEntity = new TerrainObject("terrain", mesh);
					terrainEntity.getTransform().getTranslation().set(-mesh.getWidth() / 2, 0, -mesh.getLength() / 2);
					terrainEntity.getTransform().updateMatrix();
					worldScene.setTerrain(terrainEntity);
				});
				GlobalLogger.info("Entity created in " + (time / 1e6) + " ms");
			}).then(WORKERS, () -> {
				terrainLatch.countDown();

				new TaskFuture<>(RENDER_DISPATCHER, () -> {
					GlobalLogger.info("Generating water mesh...");
					final Pair<Mesh, Long> meshTime = PCUtils
							.nanoTime(() -> new QuadMesh("water", null,
									new Vector2f(((TerrainMesh) worldScene.getTerrain().getMesh()).getWidth(),
											((TerrainMesh) worldScene.getTerrain().getMesh()).getLength())));
					worldScene.getCache().addMesh(meshTime.getKey());
					GlobalLogger.info("Water mesh generated in " + (meshTime.getValue() / 1e6) + " ms");
					return meshTime.getKey();
				})
						.then(WORKERS,
								(ExceptionFunction<Mesh, GameObject>) (mesh) -> worldScene
										.setWaterLevel(new GameObject("water", mesh,
												new Transform3D(new Vector3f(0, 0.9f, 0),
														new Quaternionf().rotateX((float) Math.toRadians(-90))),
												new Vector3i(2, 0, 0), TerrainMaterialType.WATER.getId())))
						.push();

				GameObjectFactory
						.create(WaterTowerObject.class, worldScene, new Transform3D())
						.then(WORKERS, (ExceptionConsumer<WaterTowerObject>) (obj) -> {
							final Vector2i pos = new Vector2i(0, 0);
							while (!obj.isPlaceable(worldScene, pos, Direction.NONE)) {
								pos.x++;
								if (pos.x >= worldScene.getTerrain().getMesh().getWidth()) {
									pos.x = 0;
									pos.y++;
								}
								if (pos.y >= worldScene.getTerrain().getMesh().getLength()) {
									break;
								}
							}
							obj.placeDown(worldScene, pos, Direction.NONE);
						})
						.push();

				GameObjectFactory
						.create(WaterTowerObject.class, worldScene, new Transform3D())
						.then(WORKERS, (ExceptionConsumer<WaterTowerObject>) (obj) -> {
							obj.placeDown(worldScene, new Vector2i(11, 15), Direction.NONE);
						})
						.push();

				GameObjectFactory
						.create(SolarPanelObject.class, worldScene, new Transform3D())
						.then(WORKERS,
								(ExceptionConsumer<SolarPanelObject>) (obj) -> obj
										.placeDown(worldScene, new Vector2i(15, 5), Direction.NONE))
						.push();

				GameObjectFactory
						.create(WaterWheelObject.class, worldScene, new Transform3D())
						.then(WORKERS,
								(ExceptionConsumer<WaterWheelObject>) (obj) -> obj
										.placeDown(worldScene, new Vector2i(11, 7), Direction.WEST))
						.push();

				GameObjectFactory
						.create(WaterWheelObject.class, worldScene, new Transform3D())
						.then(WORKERS,
								(ExceptionConsumer<WaterWheelObject>) (obj) -> obj
										.placeDown(worldScene, new Vector2i(13, 8), Direction.WEST))
						.push();

				UIObjectFactory
						.create(IconUIObject.class, uiScene, new Transform3D())
						.then(WORKERS, (ExceptionConsumer<IconUIObject>) System.err::println)
						.push();

			}).push();
		}

		if (moveObjectTaskState == null || moveObjectTaskState.isDone()) {
			if (movingObject) {
				if (attachedObject == null) {
					moveObjectTaskState = new TaskFuture<Void, Void>(WORKERS, () -> {
						compositor.pollObjectId(true);

						final Vector3i ids = new Vector3i(compositor.getObjectId().y, compositor.getObjectId().z,
								compositor.getObjectId().w);

						worldScene
								.getEntities()
								.values()
								.parallelStream()
								.filter(e -> e instanceof GameObject && e instanceof PlaceableObject
										&& ((GameObject) e).getObjectIdLocation() == AttributeLocation.ENTITY)
								.forEach(System.err::println);

						attachedObject = (PlaceableObject) worldScene
								.getEntities()
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
					moveObjectTaskState = new TaskFuture<Void, Void>(RENDER_DISPATCHER, () -> {
						final Vector2i pos = worldScene
								.getTerrain()
								.pickTerrainCell(worldScene.getCamera(), mousePos, window.getWidth(), window.getHeight());
						if (pos != null) {
							attachedObject.placeDown(worldScene, pos, targetRotation);
							targetRotation = Direction.ZERO();
						}
					}).push();
				}
			} else if (attachedObject != null) {
				compositor.removeOutline(attachedObject);
				attachedObject = null;
				moveObjectTaskState = null;
			}
		}

		synchronized (worldScene.getEntitiesLock()) {

			for (Entity e : worldScene.getEntities().values()) {
				if (e instanceof AnimatedGameObject ago) {
					ago.computeAnimatedTransform(TOTAL_TIME);
				}
			}

		}

		TOTAL_TIME += dTime;

		final Transform3DComponent transform3DComponent = cubeEntity.getComponent(Transform3DComponent.class);
		transform3DComponent.getTransform().getRotation().rotateY(dTime);
		transform3DComponent.getTransform().updateMatrix();

		final Camera3D camera = worldScene.getCamera();

		camera.getProjection().setFov((float) (camera.getProjection().getFov() + fovDiff));

		camera.getPosition().fma(dTime * 10, posAdd);
		camera.getRotation().rotateY((float) Math.toRadians(rotation * 50 * dTime));
		camera.updateMatrix();
	}

	@Override
	public void render(float dTime) {
		worldScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());
		compositor.render(engine, worldScene, uiScene);
	}

}
