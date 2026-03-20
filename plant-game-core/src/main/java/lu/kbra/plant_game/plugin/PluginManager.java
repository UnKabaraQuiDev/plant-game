package lu.kbra.plant_game.plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.PGMain;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.plugin.PluginJarLoader.LoadedPlugin;
import lu.kbra.plant_game.plugin.exception.PluginLoadException;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;
import lu.kbra.plant_game.plugin.registry.GameObjectRegistry;
import lu.kbra.plant_game.plugin.registry.LevelRegistry;
import lu.kbra.plant_game.plugin.registry.Registry;
import lu.kbra.plant_game.plugin.registry.UIObjectRegistry;

public final class PluginManager {

	public static final String FAIL_ON_REGISTRY_NOT_FOUND_PROPERTY = PluginManager.class.getSimpleName() + ".fail_on_registry_not_founds";
	public static boolean FAIL_ON_REGISTRY_NOT_FOUND = Boolean.getBoolean(FAIL_ON_REGISTRY_NOT_FOUND_PROPERTY);
	public static final String FAIL_ON_REGISTRY_EXCEPTION_PROPERTY = PluginManager.class.getSimpleName() + ".fail_on_registry_exception";
	public static boolean FAIL_ON_REGISTRY_EXCEPTION = PCUtils.getBoolean(FAIL_ON_REGISTRY_EXCEPTION_PROPERTY, true);
	public static final String EXTERNAL_PLUGIN_PATHS_PROPERTY = PluginManager.class.getSimpleName() + ".ext_plugin_paths";
	public static String EXTERNAL_PLUGINS_PATH = System.getProperty(EXTERNAL_PLUGIN_PATHS_PROPERTY);

	private final PluginJarLoader pluginJarLoader = new PluginJarLoader();
	private final Map<Class<? extends PluginMain>, LoadedPlugin> plugins = new HashMap<>();

	public void load() {
		try {
			final List<Path> pluginDirs = new ArrayList<>();
			if (EXTERNAL_PLUGINS_PATH != null && !EXTERNAL_PLUGINS_PATH.isBlank()) {
				Arrays.stream(EXTERNAL_PLUGINS_PATH.split(";")).map(Paths::get).forEach(pluginDirs::add);
			}
			pluginDirs.add(Paths.get(PGMain.APP_DIR.getPath()).resolve("plugins"));
			pluginDirs.add(Paths.get(PGMain.APP_DATA_DIR.getPath()).resolve("plugins"));

			final PluginDescriptor basePluginDescriptor = PGLogic.OBJECT_MAPPER
					.readValue(PCUtils.readStringSource("classpath:/plugin.json"), PluginDescriptor.class);

			this.pluginJarLoader
					.loadAll(this, pluginDirs, List.of(basePluginDescriptor))
					.forEach(lp -> this.plugins.put(lp.main().getClass(), lp));
		} catch (Exception e) {
			throw new RuntimeException("Failed to load plugins.", e);
		}
	}

	public void onLoad() throws PluginLoadException {
		this.plugins.values().forEach(c -> c.main().onLoad());

		final String goReg = "autogen.GenGORegistry";
		final String uiReg = "autogen.GenUIRegistry";

		for (LoadedPlugin c : this.plugins.values()) {
			try {
				final PriorityQueue<Registry> registries = new PriorityQueue(Comparator.comparingInt(Registry::getPriority));

				try {
					final Class<? extends GameObjectRegistry> goDefClazz = (Class<? extends GameObjectRegistry>) c
							.classLoader()
							.loadClass(c.descriptor().relativePath(goReg));
					final GameObjectRegistry reg = goDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
					registries.add(reg);
				} catch (ClassNotFoundException e) {
					GlobalLogger
							.info(c.descriptor().toString() + " doesn't define a GameObject Registry (" + c.descriptor().relativePath(goReg)
									+ ")");
					if (FAIL_ON_REGISTRY_NOT_FOUND) {
						throw new PluginLoadException(c.descriptor(), c.descriptor().relativePath(goReg), e);
					}
				}

				try {
					final Class<? extends UIObjectRegistry> uiDefClazz = (Class<? extends UIObjectRegistry>) c
							.classLoader()
							.loadClass(c.descriptor().relativePath(uiReg));
					final UIObjectRegistry reg = uiDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
					registries.add(reg);
				} catch (ClassNotFoundException e) {
					GlobalLogger
							.info(c.descriptor().toString() + " doesn't define a UIObject Registry (" + c.descriptor().relativePath(uiReg)
									+ ")");
					if (FAIL_ON_REGISTRY_NOT_FOUND) {
						throw new PluginLoadException(c.descriptor(), c.descriptor().relativePath(uiReg), e);
					}
				}

				for (String regName : c.descriptor().getRegistries()) {
					try {
						if (regName == null || regName.isBlank()) {
							GlobalLogger.warning("Invalid registry found in: " + c.descriptor().toString() + ": '" + regName + "'");
							return;
						}
						final Class<? extends Registry> resourceDefClazz = (Class<? extends Registry>) c
								.classLoader()
								.loadClass(c.descriptor().relativePath(regName));
						final Registry reg = resourceDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
						registries.add(reg);
						GlobalLogger.info("Found: " + c.toString() + " with priority: " + reg.getPriority());
					} catch (ClassNotFoundException e) {
						GlobalLogger.info("Couldn't find " + c.toString() + "'s Resources registry.");
						if (FAIL_ON_REGISTRY_NOT_FOUND) {
							throw new PluginLoadException(c.descriptor(), e);
						}
					}
				}

				for (Registry r : registries) {
					try {
						GlobalLogger
								.info("Registering: " + PCUtils.leftPadString(Integer.toString(r.getPriority()), " ", 5) + " | "
										+ r.getClass().getSimpleName());
						r.register();
						if (r instanceof NeedsPostConstruct) {
							((NeedsPostConstruct) r).postConstruct();
						}
					} catch (RegistryFailedException e) {
						GlobalLogger
								.warning("Exception when initializing registry: " + r.getClass().getName() + " from: "
										+ c.descriptor().toString());
						if (!FAIL_ON_REGISTRY_EXCEPTION) {
							throw e;
						}
					}
				}
			} catch (RegistryFailedException e) {
				throw e;
			} catch (Exception e) {
				throw new PluginLoadException(c.descriptor(), e);
			}
		}
	}

	public void onEnable() {
		this.plugins.values().forEach(c -> c.main().onEnable());
	}

	public void onDisable() {
		PCUtils.reversedStream(this.plugins.values()).forEachOrdered(c -> c.main().onDisable());
	}

	public <T extends PluginMain> Optional<T> getPlugin(final Class<T> clazz) {
		return Optional.ofNullable(this.plugins.get(clazz)).map(c -> (T) c.main());
	}

	@Override
	public String toString() {
		return "PluginManager@" + System.identityHashCode(this) + " [pluginJarLoader=" + this.pluginJarLoader + ", plugins=" + this.plugins
				+ "]";
	}

}
