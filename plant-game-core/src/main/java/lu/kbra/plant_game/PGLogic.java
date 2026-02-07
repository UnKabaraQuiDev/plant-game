package lu.kbra.plant_game;

import java.io.File;
import java.util.Locale;

import org.lwjgl.glfw.GLFW;

import com.fasterxml.jackson.databind.ObjectMapper;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.base.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.base.scene.overlay.OverlayUIScene;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.data.json.OrgJOMLModule;
import lu.kbra.plant_game.engine.data.json.OrgJSONModule;
import lu.kbra.plant_game.engine.data.json.ResourceTypeModule;
import lu.kbra.plant_game.engine.data.json.VersionMatcherModule;
import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.GameData;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.plugin.PluginManager;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;

public class PGLogic extends GameLogic {

	public static PGLogic INSTANCE;
	public static ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.registerModule(new OrgJSONModule());
		OBJECT_MAPPER.registerModule(new OrgJOMLModule());
		OBJECT_MAPPER.registerModule(new VersionMatcherModule());
		OBJECT_MAPPER.registerModule(new ResourceTypeModule());
	}

	public final WorkerDispatcher WORKERS = new WorkerDispatcher("WORKERS", 8);

	private WorldLevelScene worldScene;
	private MainMenuUIScene mainMenuUIScene;
	private OverlayUIScene overlayUIScene;
	private UIScene uiScene;
	private DeferredCompositor compositor;

	private MappingInputHandler inputHandler;

	private GameData gameData;

	private final PluginManager pluginManager = new PluginManager();

	public PGLogic() {
		INSTANCE = this;
	}

	@Override
	public void init() throws Exception {
		this.inputHandler = new MappingInputHandler(this.engine);
		this.inputHandler.setOwner(this.engine.getUpdateThread());
		this.inputHandler.loadMappings(new File(Consts.CONFIG_DIR, "mappings.json"));

		this.pluginManager.load();

		this.pluginManager.onLoad();

		this.compositor = new DeferredCompositor(this.engine, this.engine.getRenderThread());
		this.compositor.getBackgroundColor().set(1, 1, 0, 1);

		this.worldScene = new WorldLevelScene("world", this.cache);

		this.mainMenuUIScene = new MainMenuUIScene(this.cache);
		this.overlayUIScene = new OverlayUIScene(this.cache);
		this.uiScene = this.mainMenuUIScene;

		UIObjectFactory.INSTANCE = new UIObjectFactory(this.mainMenuUIScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		GameObjectFactory.INSTANCE = new GameObjectFactory(this.worldScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		LocalizationService.INSTANCE = new LocalizationService(Locale.US);

		final LevelData levelData = OBJECT_MAPPER.readValue(PCUtils.readStringSource("classpath:/levels/level0.json"), LevelData.class);
		this.gameData = GameData.fromBlankLevel(levelData);

		this.uiScene.init(this.WORKERS, this.RENDER_DISPATCHER);
		this.overlayUIScene.init(this.WORKERS, this.RENDER_DISPATCHER);
		this.worldScene.init(this.WORKERS, this.RENDER_DISPATCHER, this.gameData);

//		this.uiScene = null;
//		this.uiScene = this.overlayUIScene;

		this.pluginManager.onEnable();
	}

	private final UpdateFrameState frameState = new UpdateFrameState();

	@Override
	public void input(final float dTime) {
		this.frameState.reset();
		this.inputHandler.onFrameBegin(dTime);

		if (this.uiScene != null) {
			this.uiScene.input(this.inputHandler, this.frameState);
		}
		this.worldScene.input(this.inputHandler, this.frameState);

		if (this.inputHandler.isKeyPressedOnce(GLFW.GLFW_KEY_H)) {
			this.uiScene.getCache().dump(System.out);
			this.worldScene.getCache().dump(System.out);
			this.overlayUIScene.getCache().dump(System.out);
			super.cache.dump(System.out);
		}
	}

	@Override
	public void update(final float dTime) {
		if (this.uiScene != null) {
			this.uiScene.update(this.inputHandler, this.compositor, this.WORKERS, this.RENDER_DISPATCHER);
		}
		this.worldScene.update(this.inputHandler, this.compositor, this.WORKERS, this.RENDER_DISPATCHER);
	}

	@Override
	public void render(final float dTime) {
		this.worldScene.getCamera().getProjection().update(this.window.getWidth(), this.window.getHeight());
		// uiScene.getCamera().getProjection().update(window.getWidth(),
		// window.getHeight());

		this.worldScene.render(dTime);

		this.compositor.render(this.engine, this.worldScene, this.uiScene);
	}

	@Override
	public void cleanup() {
		this.pluginManager.onDisable();
		if (this.compositor != null) {
			this.compositor.cleanup();
		}
		this.WORKERS.shutdown();
	}

	public WorldLevelScene getWorldScene() {
		return this.worldScene;
	}

	public UIScene getUiScene() {
		return this.uiScene;
	}

	public MainMenuUIScene getMainMenuUIScene() {
		return this.mainMenuUIScene;
	}

	public DeferredCompositor getCompositor() {
		return this.compositor;
	}

	public MappingInputHandler getInputHandler() {
		return this.inputHandler;
	}

	public UpdateFrameState getFrameState() {
		return this.frameState;
	}

	public static double TOTAL_TIME() {
		return INSTANCE.engine.getTotalTime();
	}

	public GameData getGameData() {
		return this.gameData;
	}

	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

}
