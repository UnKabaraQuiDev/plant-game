
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joml.Vector3f;
import org.junit.Test;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.BuildingRegistry;
import lu.kbra.plant_game.GameObjectRegistry;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.ResourceRegistry;
import lu.kbra.plant_game.UIObjectRegistry;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.DeferredIconRenderer;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.PluginJarLoader;
import lu.kbra.plant_game.plugin.PluginJarLoader.LoadedPlugin;
import lu.kbra.plant_game.plugin.PluginMain;
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

	private final PluginJarLoader pluginJarLoader = new PluginJarLoader();
	private final Map<Class<? extends PluginMain>, LoadedPlugin> plugins = new HashMap<>();

	@Override
	public void init() throws Exception {
		this.deferredCompositor = new DeferredCompositor(this.engine, this.engine.getRenderThread());
		this.iconRenderer = new DeferredIconRenderer(this.engine, this.engine.getRenderThread());

		GameObjectFactory.INSTANCE = new GameObjectFactory(this.cache, this.WORKERS, this.RENDER_DISPATCHER);
		UIObjectFactory.INSTANCE = new UIObjectFactory(this.cache, this.WORKERS, this.RENDER_DISPATCHER);

		this.pluginJarLoader
				.loadAll(List.of(Paths.get("plugins")),
						List.of(PGLogic.OBJECT_MAPPER.readValue(PCUtils.readStringSource("classpath:/plugin.json"),
								PluginDescriptor.class)))
				.forEach(lp -> this.plugins.put(lp.main().getClass(), lp));

		for (LoadedPlugin c : this.plugins.values()) {
			try {
				final String buildingReg = "autogen.GenGORegistry";
				final Class<? extends GameObjectRegistry> buildingDefClazz = (Class<? extends GameObjectRegistry>) c.classLoader()
						.loadClass(c.descriptor().relativePath(buildingReg));
				final GameObjectRegistry reg = buildingDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
				reg.init();
			} catch (ClassNotFoundException e) {
				GlobalLogger.info(c.toString() + " doesn't define a GameObject Registry.");
			}

			try {
				final String buildingReg = "autogen.GenUIRegistry";
				final Class<? extends UIObjectRegistry> buildingDefClazz = (Class<? extends UIObjectRegistry>) c.classLoader()
						.loadClass(c.descriptor().relativePath(buildingReg));
				final UIObjectRegistry reg = buildingDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
				reg.init();
			} catch (ClassNotFoundException e) {
				GlobalLogger.info(c.toString() + " doesn't define a UIObject Registry.");
			}

			{
				final String resourceReg = c.descriptor().getRegistries().getResource();
				if (resourceReg == null || resourceReg.isBlank()) {
					return;
				}
				final Class<? extends ResourceRegistry> resourceDefClazz = (Class<? extends ResourceRegistry>) c.classLoader()
						.loadClass(c.descriptor().relativePath(resourceReg));
				final ResourceRegistry reg = resourceDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
				reg.init();
			}

			{
				final String buildingReg = c.descriptor().getRegistries().getBuilding();
				if (buildingReg == null || buildingReg.isBlank()) {
					return;
				}
				final Class<? extends BuildingRegistry> buildingDefClazz = (Class<? extends BuildingRegistry>) c.classLoader()
						.loadClass(c.descriptor().relativePath(buildingReg));
				final BuildingRegistry reg = buildingDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
				reg.init();
			}
		}

		for (final BuildingDefinition<?> bd : BuildingRegistry.BUILDING_DEFS.values().stream().flatMap(List::stream).toList()) {
			final Class<? extends GameObject> goClazz = bd.getClazz();
			if (!MeshGameObject.class.isAssignableFrom(goClazz) || !PlaceableObject.class.isAssignableFrom(goClazz)) {
				continue;
			}
			final TaskFuture<List<Object>, ? extends GameObject>.TaskState<? extends GameObject> state = GameObjectFactory.create(goClazz)
					.set(t -> t.setTransform(new Transform3D()))
					.set(t -> this.objs.offer(Pairs.readOnly(bd, (MeshGameObject) t)))
					.push();
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
		final MemImage img = this.iconRenderer
				.renderIcon(this.engine, obj.getValue(), size, new Vector3f(1), new Vector3f(1, 1, 1).normalize(), 0.1f);
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
	public void main() throws FileNotFoundException, IOException {
		final GameLogic gameLogic = new IconRendererTestLogic();

		final Properties props = new Properties();
		props.load(new StringReader(PCUtils.readStringSource("classpath:/config/main.properties")));

		final GameEngine engine = new GameEngine("test", gameLogic, new WindowOptions(props, "windowOptions"));
		engine.start();
	}

	public static void main(final String[] args) throws FileNotFoundException, IOException {
		new IconRendererTestLogic().main();
	}

}
