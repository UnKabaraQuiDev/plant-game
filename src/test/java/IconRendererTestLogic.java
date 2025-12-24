
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.joml.Vector3f;
import org.junit.Test;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.DeferredIconRenderer;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.graph.window.WindowOptions;
import lu.kbra.standalone.gameengine.impl.GameLogic;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.file.FileUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;
import lu.kbra.standalone.gameengine.utils.mem.img.MemImage;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class IconRendererTestLogic extends GameLogic {

	private final Dispatcher WORKERS = new WorkerDispatcher("WORKERS", 1);

	private DeferredCompositor deferredCompositor;
	private DeferredIconRenderer iconRenderer;

	private WorldLevelScene world;

	private TaskFuture<?, ? extends GameObject>.TaskState<? extends GameObject> state;

	@Override
	public void init() throws Exception {
		deferredCompositor = new DeferredCompositor(engine, engine.getRenderThread());
		iconRenderer = new DeferredIconRenderer(engine, engine.getRenderThread());

		GameObjectFactory.INSTANCE = new GameObjectFactory(cache, WORKERS, RENDER_DISPATCHER);
		UIObjectFactory.INSTANCE = new UIObjectFactory(cache, WORKERS, RENDER_DISPATCHER);

		state = GameObjectFactory.create(WaterTowerObject.class, new Transform3D()).push();
		world = new WorldLevelScene("world", cache);
	}

	@Override
	public void input(float dTime) {
	}

	@Override
	public void update(float dTime) {
	}

	@Override
	public void render(float dTime) {
		if (!state.isDone()) {
			return;
		}

		GlobalLogger.info("Starting rendering.");
		final SingleTexture txt = iconRenderer
				.renderIcon(engine, state.getResult(), 128, new Vector3f(1), new Vector3f(1, 1, 1).normalize(), 0.1f);
		GlobalLogger.info("Rendering done.");
		GlobalLogger.info("Fetching image.");
		final MemImage img = txt.getStoredImage();
		GlobalLogger.info("Saving image.");
		final File outFile = new File(Consts.ICONS_BAKES_RES_DIR, state.getResult().getClass().getSimpleName() + ".png");
		FileUtils.STBISave(outFile, img);
		GlobalLogger.info("Saved to: " + outFile.getAbsolutePath());

		super.stop();
	}

	@Override
	public void cleanup() {
		deferredCompositor.cleanup();
		iconRenderer.cleanup();
	}

	@Test
	public void main() throws FileNotFoundException, IOException {
		final GameLogic gameLogic = new IconRendererTestLogic();

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		final GameEngine engine = new GameEngine("test", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		new IconRendererTestLogic().main();
	}

}
