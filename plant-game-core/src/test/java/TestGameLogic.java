
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.junit.Test;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;

public class TestGameLogic extends GameLogic {

	private final Dispatcher WORKERS = new WorkerDispatcher("WORKERS", 8);

	private WorldLevelScene worldScene;
	private UIScene uiScene;
	private DeferredCompositor compositor;

	private TaskFuture<?, Void>.TaskState<Void> state;

	private WindowInputHandler inputHandler;

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

		this.uiScene = new UIScene("ui", this.cache);
		this.uiScene.getCamera().getPosition().set(0, 1, 0);
		this.uiScene.getCamera().getRotation().set(new Quaternionf().lookAlong(new Vector3f(0, -1, 0), new Vector3f(0, 0, -1)));
		this.uiScene.getCamera().getProjection().setSize(1);
		this.uiScene.getCamera().getProjection().setNearPlane(0.001f);
		this.uiScene.getCamera().getProjection().setFarPlane(1000f);
		this.uiScene.getCamera().getProjection().setPerspective(false);
		this.uiScene.getCamera().getProjection().update();
		this.uiScene.getCamera().updateMatrix();

		UIObjectFactory.INSTANCE = new UIObjectFactory(this.uiScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);
		GameObjectFactory.INSTANCE = new GameObjectFactory(this.worldScene.getCache(), this.WORKERS, this.RENDER_DISPATCHER);

		this.uiScene.init(this.WORKERS, this.RENDER_DISPATCHER);
		this.worldScene.init(this.WORKERS, this.RENDER_DISPATCHER);
	}

	private final UpdateFrameState frameState = new UpdateFrameState();

	@Override
	public void input(final float dTime) {
		this.frameState.reset();
		this.inputHandler.onFrameBegin(dTime);

		this.uiScene.input(this.inputHandler, this.frameState);
		this.worldScene.input(this.inputHandler, this.frameState);
	}

	@Override
	public void update(final float dTime) {
		this.worldScene.update(this.inputHandler, this.compositor, this.WORKERS, this.RENDER_DISPATCHER);
	}

	@Override
	public void render(final float dTime) {
		this.worldScene.getCamera().getProjection().update(this.window.getWidth(), this.window.getHeight());
		this.uiScene.getCamera().getProjection().update(this.window.getWidth(), this.window.getHeight());

		this.compositor.render(this.engine, this.worldScene, this.uiScene);
	}

	@Override
	public void cleanup() {
		this.compositor.cleanup();
	}

	@Test
	public void main() throws FileNotFoundException, IOException {
		final GameLogic gameLogic = new TestGameLogic();

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		final GameEngine engine = new GameEngine("test", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

	public static void main(final String[] args) throws FileNotFoundException, IOException {
		new TestGameLogic().main();
	}

}
