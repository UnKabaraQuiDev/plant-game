package lu.kbra.plant_game;

import java.util.function.Function;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.WorldGenerator.MaterialType;
import lu.kbra.plant_game.engine.entity.GameObject;
import lu.kbra.plant_game.engine.entity.WorldLevelScene;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.CubeMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.graph.window.Window;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.scene.Scene2D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;
import lu.kbra.standalone.gameengine.utils.MathUtils;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.noise.NoiseGenerator;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TestGameLogic extends GameLogic {

	private Dispatcher WORKERS = new WorkerDispatcher("WORKERS", 5);

	private WorldLevelScene worldScene;
	private Scene2D uiScene;
	private CacheManager uiCache;
	private DeferredCompositor simpleCompositor;
	private Entity cubeEntity;

	private TaskFuture<?, Void>.TaskState<Void> state;

	@Override
	public void init(GameEngine e) {
		simpleCompositor = new DeferredCompositor();

		worldScene = new WorldLevelScene("world", cache);
		worldScene.getCamera().lookAt(new Vector3f(0, 10, 10), new Vector3f(0, 0, 0)).updateMatrix();

		final Mesh waterLevelMesh = new QuadMesh("water", null, new Vector2f(15, 15));
		worldScene.getWorldCache().addMesh(waterLevelMesh);
		worldScene
				.addEntity(new GameObject("water", waterLevelMesh,
						new Transform3D(new Vector3f(0, -0.1f, 0), new Quaternionf().rotateX((float) Math.toRadians(-90))), true,
						new Vector3i(2, 0, 0), MaterialType.WATER.getId()));

		final CubeMesh cubeMesh = new CubeMesh("cubeMesh", null, new Vector3f(0.5f));
		worldScene.getWorldCache().addMesh(cubeMesh);
		cubeEntity = worldScene
				.addEntity(new GameObject("cubeEntity", cubeMesh, new Transform3D(), false, new Vector3i(1, 0, 255),
						MaterialType.GRASS.getId()));
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {
		if (state == null) {
			state = new TaskFuture<>(WORKERS, () -> {
				final WorldGenerator worldGenerator = new WorldGenerator(15, 15) {
					private NoiseGenerator noise = new NoiseGenerator(1234, 10);

					@Override
					protected Integer genNoise(int x, int z) {
						final float oct1 = MathUtils.map(Interpolators.SINE_OUT.evaluate(noise.noise(x + 0.5f, z + 0.5f)), 0, 1, 0, 1);
						final float oct2 = MathUtils
								.map(Interpolators.BOUNCE_OUT
										.evaluate(noise.noise(MathUtils.rotate(new Vector2f(x + 0.5f, (z + 0.5f) * 0.5f), 45))), 0, 1, -1, 1);
						return (int) Math.floor(Math.max(-1, Math.pow(oct1 * 2 + oct2 * 3, 1.2) + 3));
					}
				};
				GlobalLogger.info("Generating world...");
				final long time = PCUtils.nanoTime(() -> worldGenerator.compute());
				GlobalLogger.info("World generated in " + (time / 1e6) + " ms");
				return worldGenerator;
			}).then(RENDER_DISPATCHER, (Function<WorldGenerator, Mesh>) (worldGenerator) -> {
				GlobalLogger.info("Generating mesh...");
				final Pair<Mesh, Long> mesh = PCUtils.nanoTime(() -> worldGenerator.generateMesh(worldScene.getWorldCache()));
				GlobalLogger.info("Mesh generated in " + (mesh.getValue() / 1e6) + " ms");
				return mesh.getKey();
			}, 0).then(WORKERS, (mesh) -> {
				GlobalLogger.info("Creating entity...");
				final long time = PCUtils.nanoTime((Runnable) () -> {
					final GameObject terrainEntity = new GameObject("terrain", mesh, new Transform3D(), false, new Vector3i(0, 255, 0),
							(byte) 2);
					terrainEntity.setCompositeMaterialId(true);
					worldScene.setTerrain(terrainEntity);
				});
				GlobalLogger.info("Entity created in " + (time / 1e6) + " ms");
			}).push();
		}

		final Transform3DComponent transform3DComponent = cubeEntity.getComponent(Transform3DComponent.class);
		transform3DComponent.getTransform().getRotation().rotateY(dTime);
		transform3DComponent.getTransform().updateMatrix();

		final Window window = engine.getWindow();
		final Camera3D camera = worldScene.getCamera();

		final Vector3f posAdd = new Vector3f();
		float rotation = 0;
		if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
			posAdd.z -= 1;
		}
		if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
			posAdd.z += 1;
		}
		if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
			posAdd.x -= 1;
		}
		if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
			posAdd.x += 1;
		}
		if (window.isKeyPressed(GLFW.GLFW_KEY_Q)) {
			rotation -= 1;
		}
		if (window.isKeyPressed(GLFW.GLFW_KEY_E)) {
			rotation += 1;
		}

		camera.getPosition().fma(dTime, posAdd);
		camera.getRotation().rotateY((float) Math.toRadians(rotation * 50 * dTime));
		camera.updateMatrix();
	}

	@Override
	public void render(float dTime) {
		worldScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());
		simpleCompositor.render(engine, worldScene, uiScene, worldScene.getWorldCache(), uiCache);
	}

}
