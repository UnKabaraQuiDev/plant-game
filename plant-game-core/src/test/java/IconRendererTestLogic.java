
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joml.Vector3f;
import org.junit.Test;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.datastructure.pair.Pair;
import lu.kbra.pclib.datastructure.pair.Pairs;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.DeferredIconRenderer;
import lu.kbra.plant_game.plugin.PluginManager;
import lu.kbra.plant_game.plugin.registry.BuildingRegistry;
import lu.kbra.standalone.gameengine.GameEngine;
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

//	private WorldLevelScene world;
	private final Queue<Pair<BuildingDefinition<?>, MeshGameObject>> objs = new ConcurrentLinkedQueue<>();
	private int awaitingCount = 0;

	private final PluginManager pluginManager = new PluginManager();

	@Override
	public void init() throws Exception {
		this.deferredCompositor = new DeferredCompositor(this.engine, this.engine.getRenderThread());
		this.iconRenderer = new DeferredIconRenderer(this.engine, this.engine.getRenderThread());

		GameObjectFactory.INSTANCE = new GameObjectFactory(this.cache, this.WORKERS, this.RENDER_DISPATCHER);
		UIObjectFactory.INSTANCE = new UIObjectFactory(this.cache, this.WORKERS, this.RENDER_DISPATCHER);

		this.pluginManager.load();
		this.pluginManager.onLoad();

		for (final BuildingDefinition<?> bd : BuildingRegistry.BUILDING_DEFS.values().stream().flatMap(List::stream)
				.toList()) {
			final Class<? extends GameObject> goClazz = bd.getClazz();
			if (!MeshGameObject.class.isAssignableFrom(goClazz) || !PlaceableObject.class.isAssignableFrom(goClazz)) {
				continue;
			}
			final TaskFuture<? extends GameObject, ? extends GameObject>.TaskState<? extends GameObject> state = GameObjectFactory
					.create(goClazz).set(t -> t.setTransform(new Transform3D()))
					.set(t -> this.objs.offer(Pairs.readOnly(bd, (MeshGameObject) t))).push();
			this.awaitingCount++;
		}
	}

	@Override
	public void input(final float dTime) {
	}

	@Override
	public void update(final float dTime) {
	}

	private int renderedCount = 0;

	@Override
	public void render(final float dTime) {
		if (this.renderedCount == this.awaitingCount) {
			super.stop();
			return;
		}
		if (this.objs.isEmpty()) {
			return;
		}

		final Pair<BuildingDefinition<?>, MeshGameObject> obj = this.objs.poll();
		final int size = 1024;

		GlobalLogger.info("Starting rendering.");
		final MemImage img = this.iconRenderer.renderIcon(this.engine, obj.getValue(), size, new Vector3f(1),
				new Vector3f(1, 1, 1).normalize(), 0.1f);
		GlobalLogger.info("Rendering done.");
		final File outFile = new File(Consts.ICONS_BAKES_RES_DIR, obj.getKey().getInternalPath() + ".png");
		FileUtils.STBISave(outFile, img);
		GlobalLogger.info("Saved to: " + outFile.getAbsolutePath());

		img.cleanup();

		this.renderedCount++;
	}

	@Override
	public void cleanup() {
		this.deferredCompositor.cleanup();
		this.iconRenderer.cleanup();
	}

	@Test
	public void start() throws FileNotFoundException, IOException {
		final GameLogic gameLogic = new IconRendererTestLogic();

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		final GameEngine engine = new GameEngine("test", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

	public static void main(final String[] args) throws FileNotFoundException, IOException {
		new IconRendererTestLogic().start();
	}

}
