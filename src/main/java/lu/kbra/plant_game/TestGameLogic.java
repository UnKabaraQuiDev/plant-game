package lu.kbra.plant_game;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.engine.DeferredCompositor;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.geom.CubeMesh;
import lu.kbra.standalone.gameengine.graph.window.Window;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.RenderComponent;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene2D;
import lu.kbra.standalone.gameengine.scene.Scene3D;
import lu.kbra.standalone.gameengine.scene.camera.Camera3D;

public class TestGameLogic extends GameLogic {

	private Scene3D worldScene = new Scene3D("world");
	private Scene2D uiScene;
	private CacheManager worldCache;
	private CacheManager uiCache;
	private DeferredCompositor simpleCompositor;
	private TextEmitter textEmitter;
	private Entity cubeEntity;

	@Override
	public void init(GameEngine e) {
		worldCache = new CacheManager("world", cache);
		worldScene.getCamera().lookAt(new Vector3f(2, 2, 2), new Vector3f(0, 0, 0)).updateMatrix();

		simpleCompositor = new DeferredCompositor();

		final CubeMesh cubeMesh = new CubeMesh("cubeMesh", null, new Vector3f(0.5f));
		worldCache.addMesh(cubeMesh);
		cubeEntity = worldScene
				.addEntity("cubeEntity", new RenderComponent(10), new MeshComponent(cubeMesh), new Transform3DComponent(new Vector3f(0)));
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {
		final Transform3DComponent transform3DComponent = cubeEntity.getComponent(Transform3DComponent.class);
		transform3DComponent.getTransform().getRotation().rotateY(dTime);
		transform3DComponent.getTransform().updateMatrix();

		final Window window = engine.getWindow();
		final Camera3D camera = worldScene.getCamera();

		final Vector3f posAdd = new Vector3f();
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

		camera.getPosition().fma(dTime, posAdd);
		camera.updateMatrix();
	}

	@Override
	public void render(float dTime) {
		worldScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());
		simpleCompositor.setBackground(new Vector4f(1, 0.5f, 0, 1));
		simpleCompositor.render(engine, worldScene, uiScene, worldCache, uiCache);
	}

}
