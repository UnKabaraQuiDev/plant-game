package lu.kbra.plant_game;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.lwjgl.glfw.GLFW;

import com.codedisaster.steamworks.SteamAPI;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.base.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.base.scene.overlay.OverlayUIScene;
import lu.kbra.plant_game.base.scene.world.MainMenuWorldScene;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.data.json.LevelDataModule;
import lu.kbra.plant_game.engine.data.json.OrgJOMLModule;
import lu.kbra.plant_game.engine.data.json.OrgJSONModule;
import lu.kbra.plant_game.engine.data.json.ResourceTypeModule;
import lu.kbra.plant_game.engine.data.json.VersionMatcherModule;
import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.shader.compute.filter.VignetteShader;
import lu.kbra.plant_game.engine.render.shader.compute.filter.VignetteShader.VignetteShaderConfiguration;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.GameData;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.plugin.PluginManager;
import lu.kbra.plant_game.plugin.registry.LevelRegistry;
import lu.kbra.plant_game.plugin.registry.LevelRegistry.LevelDefinition;
import lu.kbra.standalone.gameengine.impl.Cleanupable;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;

public class PGLogic extends GameLogic {

	public static PGLogic INSTANCE;
	public static final ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper(JsonFactory.builder().configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true).build());

		OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
		OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

		OBJECT_MAPPER.registerModule(new OrgJSONModule());
		OBJECT_MAPPER.registerModule(new OrgJOMLModule());
		OBJECT_MAPPER.registerModule(new VersionMatcherModule());
		OBJECT_MAPPER.registerModule(new ResourceTypeModule());
		OBJECT_MAPPER.registerModule(new LevelDataModule());
	}

	public final WorkerDispatcher WORKERS = new WorkerDispatcher("WORKERS", 8);

	private MainMenuWorldScene mainMenuWorldScene;
	private WorldLevelScene gameWorldScene;
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
		this.inputHandler.loadMappings(new File(PGMain.CONFIG_DIR, "mappings.json"));

		this.pluginManager.load();

		this.pluginManager.onLoad();

		this.compositor = new DeferredCompositor(this.engine, this.engine.getRenderThread());

		this.mainMenuWorldScene = new MainMenuWorldScene(this.cache);
		this.worldScene = this.mainMenuWorldScene;

		this.mainMenuUIScene = new MainMenuUIScene(this.cache, this.mainMenuWorldScene);
		this.uiScene = this.mainMenuUIScene;

		UIObjectFactory.INSTANCE = new UIObjectFactory(this.mainMenuUIScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		GameObjectFactory.INSTANCE = new GameObjectFactory(this.worldScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		LocalizationService.INSTANCE = new LocalizationService(Locale.US);

		final LevelData levelData = LevelRegistry.LEVELS.get(0).getLevelData();
		this.gameData = GameData.fromBlankLevel(levelData);

		this.mainMenuUIScene.init(this.WORKERS, this.RENDER_DISPATCHER);
		this.mainMenuWorldScene.init(this.WORKERS, this.RENDER_DISPATCHER, this.gameData, new IntPointer(0));

		this.pluginManager.onEnable();

		final VignetteShaderConfiguration config = this.compositor.getFilterShader(VignetteShader.class).newConfigurationInstance();
		this.compositor.enableFilter(config);
	}

	private final UpdateFrameState frameState = new UpdateFrameState();

	@Override
	public void input(final float dTime) {
		this.frameState.reset();
		this.inputHandler.onFrameBegin(dTime);

		if (this.uiScene != null) {
			this.uiScene.input(this.inputHandler, this.frameState);
		}
		if (this.worldScene != null) {
			this.worldScene.input(this.inputHandler, this.frameState);
		}

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
		if (this.worldScene != null) {
			this.worldScene.update(this.inputHandler, this.compositor, this.WORKERS, this.RENDER_DISPATCHER);
		}
	}

	@Override
	public void render(final float dTime) {
		if (this.worldScene != null) {
			this.worldScene.getCamera().getProjection().update(this.window.getWidth(), this.window.getHeight());
			this.worldScene.render(dTime);
		}

		this.compositor.render(this.engine, this.worldScene, this.uiScene);
	}

	@Override
	public void main() {
		if (PGMain.STEAM_LAUCHED && SteamAPI.isSteamRunning()) {
			SteamAPI.runCallbacks();
		}
	}

	public void startLevel(final LevelDefinition currentLevelDefinition) {
		renderCleanup(this.gameWorldScene);
		renderCleanup(this.worldScene);
		renderCleanup(this.overlayUIScene);
		renderCleanup(this.uiScene);

		final LevelData levelData = currentLevelDefinition.getLevelData();
		final IntPointer progress = new IntPointer(0);

		this.gameData = GameData.fromBlankLevel(levelData);
		currentLevelDefinition.setGameData(this.gameData);

		this.overlayUIScene = new OverlayUIScene(this.cache);
		new TaskFuture<>(this.WORKERS, (Runnable) () -> {
			this.overlayUIScene.init(this.WORKERS, this.WORKERS, this.gameData).thenOther(OverlayUIScene::doLayout);
			this.uiScene = this.overlayUIScene;
		}).push();

		this.gameWorldScene = new WorldLevelScene(levelData.getInternalName(), this.cache);
		new TaskFuture<>(this.WORKERS, (Runnable) () -> {
			this.gameWorldScene.init(this.WORKERS, this.RENDER_DISPATCHER, this.gameData, progress);
			this.worldScene = this.gameWorldScene;
		}).push();
	}

	public void resumeLevel(final LevelDefinition currentLevelDefinition) {
		System.err.println("not supported");
	}

	@Override
	public void cleanup() {
		try {
			this.inputHandler.saveMappings();
		} catch (IOException e) {
			GlobalLogger.severe("Couldn't save InputHandler mappings.", e);
		}

		this.pluginManager.onDisable();
		if (this.compositor != null) {
			this.compositor.cleanup();
		}
		this.WORKERS.shutdown();
	}

	public static void renderCleanup(final Cleanupable c) {
		if (c == null) {
			return;
		}
		INSTANCE.RENDER_DISPATCHER.post(c::cleanup);
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

	public MainMenuWorldScene getMainMenuWorldScene() {
		return this.mainMenuWorldScene;
	}

	public OverlayUIScene getOverlayUIScene() {
		return this.overlayUIScene;
	}

	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

}
