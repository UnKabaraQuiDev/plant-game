package lu.kbra.plant_game;

import java.io.File;
import java.util.Locale;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.locale.LocalizationService;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.MainMenuUIScene;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;

public class PGLogic extends GameLogic {

	public static PGLogic INSTANCE;

	public final WorkerDispatcher WORKERS = new WorkerDispatcher("WORKERS", 8);

	private WorldLevelScene worldScene;
	private UIScene uiScene;
	private DeferredCompositor compositor;

	private WindowInputHandler inputHandler;

	public PGLogic() {
		INSTANCE = this;
	}

	@Override
	public void init() throws Exception {
		inputHandler = new MappingInputHandler(engine);
		inputHandler.setOwner(engine.getUpdateThread());
		((MappingInputHandler) inputHandler).saveMappings(new File(Consts.CONFIG_DIR, "mappings.json"));

		compositor = new DeferredCompositor(engine, engine.getRenderThread());
		compositor.getBackgroundColor().set(1, 1, 0, 1);

		worldScene = new WorldLevelScene("world", cache);
		worldScene.getCamera().setPosition(new Vector3f(-20, 25, 20).mul(1.5f));
		worldScene.getCamera().lookAt(worldScene.getCamera().getPosition(), new Vector3f(0, 0, 0)).updateMatrix();
		worldScene.getCamera().getProjection().setFov((float) Math.toRadians(40));
		worldScene.getLightDirection().set(new Vector3f(0.5f, 0.5f, 0.5f).normalize());

		uiScene = new MainMenuUIScene(cache);

		UIObjectFactory.INSTANCE = new UIObjectFactory(uiScene.getCache(), WORKERS, RENDER_DISPATCHER);
		GameObjectFactory.INSTANCE = new GameObjectFactory(worldScene.getCache(), WORKERS, RENDER_DISPATCHER);
		LocalizationService.INSTANCE = new LocalizationService(Locale.US);

		uiScene.init(WORKERS, RENDER_DISPATCHER);
		worldScene.init(WORKERS, RENDER_DISPATCHER);
	}

	private final UpdateFrameState frameState = new UpdateFrameState();

	@Override
	public void input(float dTime) {
		frameState.reset();
		inputHandler.onFrameBegin();

		uiScene.input(inputHandler, dTime, frameState);
		worldScene.input(inputHandler, dTime, frameState);
	}

	@Override
	public void update(float dTime) {
		uiScene.update(inputHandler, dTime, compositor, WORKERS, RENDER_DISPATCHER);
		worldScene.update(inputHandler, dTime, compositor, WORKERS, RENDER_DISPATCHER);
	}

	@Override
	public void render(float dTime) {
		worldScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());
		uiScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());

		compositor.render(engine, worldScene, uiScene);
	}

	@Override
	public void cleanup() {
		compositor.cleanup();
		WORKERS.shutdown();
	}

	public WorldLevelScene getWorldScene() {
		return worldScene;
	}

	public UIScene getUiScene() {
		return uiScene;
	}

	public DeferredCompositor getCompositor() {
		return compositor;
	}

	public WindowInputHandler getInputHandler() {
		return inputHandler;
	}

	public UpdateFrameState getFrameState() {
		return frameState;
	}

	public static double TOTAL_TIME() {
		return INSTANCE.engine.getTotalTime();
	}

}
