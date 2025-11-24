package lu.kbra.plant_game.engine.scene.world;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.impl.ThrowingConsumer;
import lu.pcy113.pclib.impl.ThrowingFunction;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.mesh.pipe.PipeMesh;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.energy.SolarPanelObject;
import lu.kbra.plant_game.engine.entity.go.obj.grass.LargeGrassObject;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterWheelObject;
import lu.kbra.plant_game.engine.mesh.data.AttributeLocation;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.generator.ImageWorldGenerator;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator.TerrainMaterialType;
import lu.kbra.plant_game.engine.window.input.StandardKeyOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedQuadMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.gl.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class WorldLevelScene extends Scene3D {

	private LevelData levelData;

	private final CacheManager worldCache;

	private GameObject waterLevel;
	private TerrainObject terrain;

	private Vector3f lightColor = new Vector3f(1), lightDirection = new Vector3f(0.8f, 0.5f, 0.5f).normalize();
	private float ambientLight = 0.25f;
	private Vector2f windDirection = new Vector2f(1, 1).normalize();

	private final Vector3f posAdd = new Vector3f();
	private float rotation = 0;
	private float fovDiff = 0;

	private boolean movingObject = false;
	private PlaceableObject attachedObject = null;
	private Direction targetRotation = Direction.DEFAULT();
	private TaskFuture<?, Void>.TaskState<Void> moveObjectTaskState;

	public WorldLevelScene(final String name, final CacheManager parent) {
		super(name);
		this.worldCache = new CacheManager(name, parent);

		this.getCamera().setPosition(new Vector3f(-20, 25, 20).mul(1.5f));
		this.getCamera().lookAt(this.getCamera().getPosition(), new Vector3f(0, 0, 0)).updateMatrix();
		this.getCamera().getProjection().setFov((float) Math.toRadians(40));
		this.getLightDirection().set(new Vector3f(0.5f, 0.5f, 0.5f).normalize());
	}

	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		final CountDownLatch terrainLatch = new CountDownLatch(1);

		this.moveObjectTaskState = new TaskFuture<Void, Void>(workers, () -> {
			terrainLatch.await();
		}).push();

		new TaskFuture<>(workers, () -> {
			final WorldGenerator worldGenerator = new ImageWorldGenerator("classpath:/maps/world_map.png", 4 / 255f);
			GlobalLogger.info("Generating world...");
			final long time = PCUtils.nanoTime(() -> worldGenerator.compute());
			GlobalLogger.info("World generated in " + (time / 1e6) + " ms");
			return worldGenerator;
		}).then(renderDispatcher, (ThrowingFunction<WorldGenerator, TerrainMesh, Throwable>) worldGenerator -> {
			GlobalLogger.info("Generating mesh...");
			final Pair<TerrainMesh, Long> mesh = PCUtils.nanoTime(() -> worldGenerator.generateMesh(this.getCache()));
			GlobalLogger.info("Mesh generated in " + (mesh.getValue() / 1e6) + " ms");
			return mesh.getKey();
		}, 0).then(workers, (Consumer<TerrainMesh>) mesh -> {
			GlobalLogger.info("Creating entity...");
			final long time = PCUtils.nanoTime((Runnable) () -> {
				final TerrainObject terrainEntity = new TerrainObject("terrain", mesh);
				terrainEntity.getTransform().getTranslation().set(-mesh.getWidth() / 2, 0, -mesh.getLength() / 2);
				terrainEntity.getTransform().updateMatrix();
				this.setTerrain(terrainEntity);
			});
			GlobalLogger.info("Entity created in " + (time / 1e6) + " ms");
		}).then(workers, (Runnable) () -> {
			terrainLatch.countDown();

			new TaskFuture<>(renderDispatcher, () -> {
				final Vector3f[] pos = new Vector3f[48];
				int index = 0;
				final TerrainMesh terrainMesh = this.getTerrain().getMesh();
				for (int x = 0, y = 2; x < 12; x += terrainMesh.getCellSize()) {
					final float h0 = terrainMesh.getCellHeight(x, y) + 0.5f;
					final float h1 = terrainMesh.getCellHeight(x + terrainMesh.getCellSize(), y) + 0.5f;

					pos[index + 0] = new Vector3f(x, h0, y + 0.5f);
					pos[index + 1] = new Vector3f(x + terrainMesh.getCellSize(), h0, y + 0.5f);
					index += 2;

					if (h0 != h1) {
						final float low = Math.min(h0, h1);
						final float high = Math.max(h0, h1);

						pos[index + 0] = new Vector3f(x + terrainMesh.getCellSize(), low, y + 0.5f);
						pos[index + 1] = new Vector3f(x + terrainMesh.getCellSize(), high, y + 0.5f);
						index += 2;
					}
				}
				final int[] indices = new int[pos.length * 2 + 2];
				for (int i = 0; i < pos.length - 1; i++) {
					indices[i * 2 + 0] = i;
					indices[i * 2 + 1] = i + 1;
				}

				final PipeMesh mesh = new PipeMesh(
						"pipe-" + "@" + System.identityHashCode(this),
						12,
						new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, 1, pos),
						new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, 1, indices, BufferType.ELEMENT_ARRAY));
				mesh.setEffectiveLength(index);
				this.worldCache.addMesh(mesh);
				this.terrain
						.getComponent(SubEntitiesComponent.class)
						.addEntity(new GameObject("test", mesh, new Transform3D(), new Vector3i(), TerrainMaterialType.LIGHT_BLUE.getId()));

				for (int x = terrainMesh.getWidth(), y = 12; x > 0; x -= terrainMesh.getCellSize()) {
					final float h0 = terrainMesh.getCellHeight(x, y) + 0.5f;
					final float h1 = terrainMesh.getCellHeight(x + terrainMesh.getCellSize(), y) + 0.5f;

					mesh.addPoint(new Vector3f(x + terrainMesh.getCellSize(), h0, y + 0.5f));

					if (h0 != h1) {
						final float low = Math.min(h0, h1);
						final float high = Math.max(h0, h1);

						mesh.addPoint(new Vector3f(x + terrainMesh.getCellSize(), high, y + 0.5f));
					}
				}

				System.err.println("created pipe");
			}).push();

			new TaskFuture<>(renderDispatcher, () -> {
				GlobalLogger.info("Generating water mesh...");
				final Pair<Mesh, Long> meshTime = PCUtils
						.nanoTime(() -> new LoadedQuadMesh(
								"water",
								null,
								new Vector2f(this.getTerrain().getMesh().getWidth(), this.getTerrain().getMesh().getLength())));
				this.getCache().addMesh(meshTime.getKey());
				GlobalLogger.info("Water mesh generated in " + (meshTime.getValue() / 1e6) + " ms");
				return meshTime.getKey();
			})
					.then(workers,
							(ThrowingConsumer<Mesh, Throwable>) mesh -> this
									.setWaterLevel(new GameObject(
											"water",
											mesh,
											new Transform3D(new Vector3f(0, 0.9f, 0)),
											new Vector3i(2, 0, 0),
											TerrainMaterialType.WATER.getId())))
					.push();

			GameObjectFactory
					.create(WaterTowerObject.class, this, new Transform3D())
					.then(workers, (ThrowingConsumer<WaterTowerObject, Throwable>) obj -> {
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
					.then(workers, (ThrowingConsumer<WaterTowerObject, Throwable>) obj -> {
						obj.placeDown(this, new Vector2i(11, 15), Direction.NONE);
					})
					.push();

			GameObjectFactory
					.create(SolarPanelObject.class, this, new Transform3D())
					.then(workers,
							(ThrowingConsumer<SolarPanelObject, Throwable>) obj -> obj.placeDown(this, new Vector2i(15, 5), Direction.NONE))
					.push();

			GameObjectFactory
					.create(WaterWheelObject.class, this, new Transform3D())
					.then(workers,
							(ThrowingConsumer<WaterWheelObject, Throwable>) obj -> obj.placeDown(this, new Vector2i(11, 7), Direction.WEST))
					.push();

			GameObjectFactory
					.create(WaterWheelObject.class, this, new Transform3D())
					.then(workers,
							(ThrowingConsumer<WaterWheelObject, Throwable>) obj -> obj.placeDown(this, new Vector2i(13, 8), Direction.WEST))
					.push();

			if (!this.getTerrain().hasComponentMatching(SubEntitiesComponent.class)) {
				this.getTerrain().addComponent(new SubEntitiesComponent<GameObject>());
			}
			final SubEntitiesComponent subEntities = this.getTerrain().getComponent(SubEntitiesComponent.class);
			for (int x = 0; x < this.getTerrain().getMesh().getWidth(); x++) {
				for (int z = 0; z < this.getTerrain().getMesh().getLength(); z++) {
					final float y = this.getTerrain().getMesh().getCellHeight(x, z);
					GameObjectFactory
							.create(LargeGrassObject.class,
									subEntities,
									new Transform3D(
											new Vector3f(x + 0.5f, y, z + 0.5f),
											new Quaternionf().rotateY((float) (Math.PI / 2 * PCUtils.randomIntRange(0, 3)))),
									(short) 5)
							.push();
				}
			}
		}).push();
	}

	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		this.posAdd.zero();
		this.rotation = 0;

		if (!frameState.uiSceneCaughtKeyboardInput) {
			if (inputHandler.isKeyHeld(StandardKeyOption.FORWARD)) {
				this.posAdd.z -= 1;
			}
			if (inputHandler.isKeyHeld(StandardKeyOption.BACKWARD)) {
				this.posAdd.z += 1;
			}
			if (inputHandler.isKeyHeld(StandardKeyOption.LEFT)) {
				this.posAdd.x -= 1;
			}
			if (inputHandler.isKeyHeld(StandardKeyOption.RIGHT)) {
				this.posAdd.x += 1;
			}
			if (inputHandler.isKeyHeld(StandardKeyOption.ROTATE_LEFT)) {
				this.rotation -= 1;
			}
			if (inputHandler.isKeyHeld(StandardKeyOption.ROTATE_RIGHT)) {
				this.rotation += 1;
			}

			if (inputHandler.isKeyPressedOrRepeat(StandardKeyOption.TURN_CW)) {
				this.targetRotation = this.targetRotation.getClockwise();
			}
			if (inputHandler.isKeyPressedOrRepeat(StandardKeyOption.TURN_CCW)) {
				this.targetRotation = this.targetRotation.getCounterClockwise();
			}
		}

		if (!frameState.uiSceneCaughtMouseInput) {
			this.fovDiff = (float) (inputHandler.getMouseScroll().y * 0.05f);

			if (inputHandler.isMouseButtonPressedOnce(StandardKeyOption.PLACE)) {
				this.movingObject = !this.movingObject;
			}
		}
	}

	public void update(
			final WindowInputHandler inputHandler,
			final float dTime,
			final DeferredCompositor compositor,
			final Dispatcher workers,
			final Dispatcher renderDispatcher) {
		if (this.moveObjectTaskState == null || this.moveObjectTaskState.isDone()) {
			if (this.movingObject) {
				if (this.attachedObject == null) {
					this.moveObjectTaskState = new TaskFuture<Void, Void>(workers, () -> {
						compositor.pollObjectId(true);

						final Vector3i ids = new Vector3i(
								compositor.getObjectId().y,
								compositor.getObjectId().z,
								compositor.getObjectId().w);

						super.getEntities()
								.values()
								.parallelStream()
								.filter(e -> e instanceof GameObject && e instanceof PlaceableObject
										&& ((GameObject) e).getObjectIdLocation() == AttributeLocation.ENTITY);

						this.attachedObject = (PlaceableObject) super.getEntities()
								.values()
								.parallelStream()
								.filter(e -> e instanceof GameObject && e instanceof PlaceableObject
										&& ((GameObject) e).getObjectIdLocation() == AttributeLocation.ENTITY)
								.filter(e -> ids.equals(((GameObject) e).getObjectId()))
								.findFirst()
								.orElse(null);

						if (this.attachedObject == null) {
							this.moveObjectTaskState = null;
							this.movingObject = false;
						} else {
							compositor.addOutline(this.attachedObject, new Vector4f(1, 0, 1, 1));
						}
					}).push();
				} else {
					final Vector2f mousePos = inputHandler.getMousePosition();
					this.moveObjectTaskState = new TaskFuture<Void, Void>(renderDispatcher, () -> {
						final Vector2i pos = this
								.getTerrain()
								.pickTerrainCell(super.getCamera(),
										mousePos,
										compositor.getWindow().getWidth(),
										compositor.getWindow().getHeight());
						if (pos != null) {
							this.attachedObject.placeDown(this, pos, this.targetRotation);
							this.targetRotation = Direction.DEFAULT();
						}
					}).push();
				}
			} else if (this.attachedObject != null) {
				compositor.removeOutline(this.attachedObject);
				this.attachedObject = null;
				this.moveObjectTaskState = null;
			}
		}

		final float time = (float) inputHandler.getGameEngine().getTotalTime();

		synchronized (super.getEntitiesLock()) {

			for (final Entity e : this) {
				if (e instanceof final AnimatedGameObject ago) {
					ago.computeAnimatedTransform(time);
				}
			}

		}

		final Camera3D camera = super.getCamera();
		camera.getProjection().setFov(camera.getProjection().getFov() + this.fovDiff);
		camera.getPosition().fma(dTime * 10, this.posAdd);
		camera.getRotation().rotateY((float) Math.toRadians(this.rotation * 50 * dTime));
		camera.updateMatrix();
	}

	public GameObject getWaterLevel() {
		return this.waterLevel;
	}

	public <T extends GameObject> T setWaterLevel(final T waterLevel) {
		this.waterLevel = waterLevel;
		return super.addEntity(waterLevel);
	}

	public TerrainObject getTerrain() {
		return this.terrain;
	}

	public void setTerrain(final TerrainObject terrain) {
		this.terrain = terrain;
		super.addEntity(terrain);
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

}
