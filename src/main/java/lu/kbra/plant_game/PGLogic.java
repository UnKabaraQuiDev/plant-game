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
		this.inputHandler = new MappingInputHandler(this.engine);
		this.inputHandler.setOwner(this.engine.getUpdateThread());
		((MappingInputHandler) this.inputHandler).saveMappings(new File(Consts.CONFIG_DIR, "mappings.json"));

		this.compositor = new DeferredCompositor(this.engine, this.engine.getRenderThread());
		this.compositor.getBackgroundColor().set(1, 1, 0, 1);

		this.worldScene = new WorldLevelScene("world", this.cache);
		this.worldScene.getCamera().setPosition(new Vector3f(-20, 25, 20).mul(1.5f));
		this.worldScene.getCamera().lookAt(this.worldScene.getCamera().getPosition(), new Vector3f(0, 0, 0)).updateMatrix();
		this.worldScene.getCamera().getProjection().setFov((float) Math.toRadians(40));
		this.worldScene.getLightDirection().set(new Vector3f(0.5f, 0.5f, 0.5f).normalize());

		this.uiScene = new MainMenuUIScene(this.cache);

		UIObjectFactory.INSTANCE = new UIObjectFactory(this.uiScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		GameObjectFactory.INSTANCE = new GameObjectFactory(this.worldScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		LocalizationService.INSTANCE = new LocalizationService(Locale.US);

		this.uiScene.init(this.WORKERS, this.RENDER_DISPATCHER);
		this.worldScene.init(this.WORKERS, this.RENDER_DISPATCHER);
	}

	private final UpdateFrameState frameState = new UpdateFrameState();

	@Override
	public void input(final float dTime) {
		this.frameState.reset();
		this.inputHandler.onFrameBegin();

		this.uiScene.input(this.inputHandler, dTime, this.frameState);
		this.worldScene.input(this.inputHandler, dTime, this.frameState);
	}

	@Override
	public void update(final float dTime) {
		this.uiScene.update(this.inputHandler, dTime, this.compositor, this.WORKERS, this.RENDER_DISPATCHER);
		this.worldScene.update(this.inputHandler, dTime, this.compositor, this.WORKERS, this.RENDER_DISPATCHER);
	}

	@Override
	public void render(final float dTime) {
		this.worldScene.getCamera().getProjection().update(this.window.getWidth(), this.window.getHeight());
		// uiScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());

		this.compositor.render(this.engine, this.worldScene, this.uiScene);
	}

	@Override
	public void cleanup() {
		this.compositor.cleanup();
		this.WORKERS.shutdown();
	}

	public WorldLevelScene getWorldScene() {
		return this.worldScene;
	}

	public UIScene getUiScene() {
		return this.uiScene;
	}

	public DeferredCompositor getCompositor() {
		return this.compositor;
	}

	public WindowInputHandler getInputHandler() {
		return this.inputHandler;
	}

	public UpdateFrameState getFrameState() {
		return this.frameState;
	}

	public static double TOTAL_TIME() {
		return INSTANCE.engine.getTotalTime();
	}

}
