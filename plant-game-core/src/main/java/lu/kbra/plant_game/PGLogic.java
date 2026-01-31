package lu.kbra.plant_game;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.Consumer;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.fasterxml.jackson.databind.ObjectMapper;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.data.json.OrgJOMLModule;
import lu.kbra.plant_game.engine.data.json.OrgJSONModule;
import lu.kbra.plant_game.engine.data.json.VersionMatcherModule;
import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.obj_inst.particles.GravityParticleGameObject;
import lu.kbra.plant_game.engine.entity.go.obj_inst.particles.ParticleGameObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.vanilla.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.vanilla.scene.overlay.OverlayUIScene;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class PGLogic extends GameLogic {

	public static PGLogic INSTANCE;
	public static ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.registerModule(new OrgJSONModule());
		OBJECT_MAPPER.registerModule(new OrgJOMLModule());
		OBJECT_MAPPER.registerModule(new VersionMatcherModule());
	}

	public final WorkerDispatcher WORKERS = new WorkerDispatcher("WORKERS", 8);

	private WorldLevelScene worldScene;
	private MainMenuUIScene mainMenuUIScene;
	private OverlayUIScene overlayUIScene;
	private UIScene uiScene;
	private DeferredCompositor compositor;

	private MappingInputHandler inputHandler;

	private PluginJarLoader pluginJarLoader = new PluginJarLoader();

	public PGLogic() {
		INSTANCE = this;
	}

	private ParticleGameObject parts;

	@Override
	public void init() throws Exception {
		this.inputHandler = new MappingInputHandler(this.engine);
		this.inputHandler.setOwner(this.engine.getUpdateThread());
		this.inputHandler.loadMappings(new File(Consts.CONFIG_DIR, "mappings.json"));
		// this.inputHandler.saveMappings(new File(Consts.CONFIG_DIR, "mappings.json"));

		VanillaBuildingDefinitionRegistry.init();

		this.pluginJarLoader.loadAll(Paths.get("plugins"));

		this.compositor = new DeferredCompositor(this.engine, this.engine.getRenderThread());
		this.compositor.getBackgroundColor().set(1, 1, 0, 1);

		this.worldScene = new WorldLevelScene("world", this.cache);

		this.mainMenuUIScene = new MainMenuUIScene(this.cache);
		this.overlayUIScene = new OverlayUIScene(this.cache);
		this.uiScene = this.mainMenuUIScene;

		UIObjectFactory.INSTANCE = new UIObjectFactory(this.mainMenuUIScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		GameObjectFactory.INSTANCE = new GameObjectFactory(this.worldScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		LocalizationService.INSTANCE = new LocalizationService(Locale.US);

		this.uiScene.init(this.WORKERS, this.RENDER_DISPATCHER);
		this.overlayUIScene.init(this.WORKERS, this.RENDER_DISPATCHER);
		this.worldScene.init(this.WORKERS, this.RENDER_DISPATCHER);

		this.uiScene = null;
		this.uiScene = this.overlayUIScene;

//		final byte[] colors = new byte[20];
//		for (int i = 0; i < colors.length; i++) {
//			colors[i] = (byte) ColorMaterial.byId(i % ColorMaterial.values().length + 1).getId();
//		}
//		GameObjectFactory
//				.create(ParticleGameObject.class,
//						this.worldScene,
//						new InstanceData(i -> new Transform3D(new Vector3f(i, 0, 0)).scaleMul(0.25f).update(), 20, Arrays
//								.asList(() -> new Vec3fAttribArray("velocity", ParticleGameObject.VELOCITY_BUFFER_INDEX, 1,
//										new Vector3f[20], BufferType.ARRAY, false),
//										() -> new Vec3fAttribArray("acceleration", ParticleGameObject.ACCELERATION_BUFFER_INDEX, 1,
//												new Vector3f[20], BufferType.ARRAY, false),
//										() -> new UByteAttribArray("color", GameObject.MESH_ATTRIB_MATERIAL_ID_ID, 1, colors, false, 1))),
//						new Transform3D(new Vector3f(0, 10, 0))/*
//																 * , ColorMaterial.PINK.getId()
//																 */)
//				.then(this.WORKERS, (Function<ParticleGameObject, ParticleGameObject>) obj -> this.parts = obj)
//				.then(this.WORKERS, (Consumer<ParticleGameObject>) System.out::println)
//				.push();

		ParticleGameObject
				.createGravity(this.WORKERS,
						this.worldScene,
						100,
						ColorMaterial.RED,
						new Transform3D(new Vector3f(0, 3, 0)),
						true,
						0,
						GravityParticleGameObject.IRON_DENSITY,
						i -> new Vector3f(1, 0, 0).rotateY((float) (Math.random() * Math.PI * 2))
								.normalize()
								.mul(PCUtils.randomFloatRange(0.8f, 1.5f)),
						null,
						i -> new Vector3f(0, 2, 0),
						i -> GameEngineUtils.randomQuaterionf(),
						i -> PCUtils.randomFloatRange(0.08f, 0.1f))
				.then(this.WORKERS,
						(Consumer<GravityParticleGameObject>) parts -> this.worldScene.getParticleManager().getActiveObjects().add(parts))
				.push();

		ParticleGameObject
				.createFloating(this.WORKERS,
						this.worldScene,
						100,
						ColorMaterial.DARK_BROWN,
						new Transform3D(new Vector3f(0, 3, 0)),
						GravityParticleGameObject.IRON_DENSITY,
						i -> PCUtils.randomFloatRange(0.8f, 1.5f),
						null,
						i -> GameEngineUtils.randomQuaterionf(),
						i -> PCUtils.randomFloatRange(0.08f, 0.1f))
				.then(this.WORKERS,
						(Consumer<GravityParticleGameObject>) parts -> this.worldScene.getParticleManager().getActiveObjects().add(parts))
				.push();
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
		// uiScene.getCamera().getProjection().update(window.getWidth(), window.getHeight());

		this.worldScene.render(dTime);

		this.compositor.render(this.engine, this.worldScene, this.uiScene);
	}

	@Override
	public void cleanup() {
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

}
