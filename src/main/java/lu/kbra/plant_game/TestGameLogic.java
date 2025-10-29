package lu.kbra.plant_game;

import java.io.File;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.impl.WindowInputHandler;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldGenerator.TerrainMaterialType;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.geom.CubeMesh;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.Transform3DComponent;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TestGameLogic extends GameLogic {

	private float TOTAL_TIME = 0;

	private Dispatcher WORKERS = new WorkerDispatcher("WORKERS", 8);

	private WorldLevelScene worldScene;
	private UIScene uiScene;
	private DeferredCompositor compositor;
	private Entity cubeEntity;

	private TaskFuture<?, Void>.TaskState<Void> state;

	private WindowInputHandler inputHandler;

	@Override
	public void init(GameEngine e) throws Exception {
		inputHandler = new MappingInputHandler(window);
		((MappingInputHandler) inputHandler).saveMappings(new File(Consts.CONFIG_DIR, "mappings.json"));

		compositor = new DeferredCompositor(engine, e.getRenderThread());
		compositor.getBackgroundColor().set(1, 1, 0, 1);

		worldScene = new WorldLevelScene("world", cache);
		worldScene.getCamera().setPosition(new Vector3f(-20, 25, 20).mul(1.5f));
		worldScene.getCamera().lookAt(worldScene.getCamera().getPosition(), new Vector3f(0, 0, 0)).updateMatrix();
		worldScene.getCamera().getProjection().setFov((float) Math.toRadians(40));
		worldScene.getLightDirection().set(new Vector3f(0.5f, 0.5f, 0.5f).normalize());

		uiScene = new UIScene("ui", cache);
		uiScene.getCamera().getPosition().set(0, 1, 0);
		uiScene.getCamera().getRotation()
				.set(new Quaternionf().lookAlong(new Vector3f(0, -1, 0), new Vector3f(0, 0, -1)));
		uiScene.getCamera().getProjection().setSize(1);
		uiScene.getCamera().getProjection().setNearPlane(0.001f);
		uiScene.getCamera().getProjection().setFarPlane(1000f);
		uiScene.getCamera().getProjection().setPerspective(false);
		uiScene.getCamera().getProjection().update();
		uiScene.getCamera().updateMatrix();

		UIObjectFactory.INSTANCE = new UIObjectFactory(uiScene.getCache(), WORKERS, RENDER_DISPATCHER);
		GameObjectFactory.INSTANCE = new GameObjectFactory(worldScene.getCache(), WORKERS, RENDER_DISPATCHER);

		final CubeMesh cubeMesh = new CubeMesh("cubeMesh", null, new Vector3f(0.5f));
		worldScene.getCache().addMesh(cubeMesh);
		cubeEntity = worldScene.addEntity(new GameObject("cubeEntity", cubeMesh, new Transform3D(),
				new Vector3i(1, 0, 255), TerrainMaterialType.GRASS.getId()));

		worldScene.init(WORKERS, RENDER_DISPATCHER);

		UIObjectFactory.create(ButtonUIObject.class, uiScene, new Transform3D()).push();
	}

	@Override
	public void input(float dTime) {
		inputHandler.onFrameBegin();

		if (!uiScene.input(inputHandler, dTime)) {
			worldScene.input(inputHandler, dTime);
		}
	}

	@Override
	public void update(float dTime) {
		worldScene.update(engine, dTime, compositor, WORKERS, RENDER_DISPATCHER);

		TOTAL_TIME += dTime;

		final Transform3DComponent transform3DComponent = cubeEntity.getComponent(Transform3DComponent.class);
		transform3DComponent.getTransform().getRotation().rotateY(dTime);
		transform3DComponent.getTransform().updateMatrix();
	}

	@Override
	public void render(float dTime) {
		worldScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());
		uiScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());
		compositor.render(engine, worldScene, uiScene);
	}

}
