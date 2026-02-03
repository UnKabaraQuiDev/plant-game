package lu.kbra.plant_game.plugin;

import static lu.kbra.plant_game.PGLogic.OBJECT_MAPPER;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.GameObjectRegistry;
import lu.kbra.plant_game.UIObjectRegistry;
import lu.kbra.plant_game.plugin.PluginJarLoader.LoadedPlugin;

public final class PluginManager {

	public static final String FAIL_ON_REGISTRY_NOT_FOUND_PROPERTY = PluginManager.class.getSimpleName() + ".fail_on_registry_not_founds";
	public static boolean FAIL_ON_REGISTRY_NOT_FOUND = Boolean.getBoolean(FAIL_ON_REGISTRY_NOT_FOUND_PROPERTY);
	public static final String SKIP_FAIL_ON_REGISTRY_EXCEPTION_PROPERTY = PluginManager.class.getSimpleName()
			+ ".skip_fail_on_registry_exception";
	public static boolean SKIP_FAIL_ON_REGISTRY_EXCEPTION = PCUtils.getBoolean(SKIP_FAIL_ON_REGISTRY_EXCEPTION_PROPERTY, true);

	private final PluginJarLoader pluginJarLoader = new PluginJarLoader();
	private final Map<Class<? extends PluginMain>, LoadedPlugin> plugins = new HashMap<>();

	public void load() {
		try {
			this.pluginJarLoader
					.loadAll(List.of(Paths.get("plugins")),
							List.of(OBJECT_MAPPER.readValue(PCUtils.readStringSource("classpath:/plugin.json"), PluginDescriptor.class)))
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
				try {
					final Class<? extends GameObjectRegistry> goDefClazz = (Class<? extends GameObjectRegistry>) c.classLoader()
							.loadClass(c.descriptor().relativePath(goReg));
					final GameObjectRegistry reg = goDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
					reg.init();
				} catch (ClassNotFoundException e) {
					GlobalLogger.info(c.descriptor().toString() + " doesn't define a GameObject Registry.");
					if (FAIL_ON_REGISTRY_NOT_FOUND) {
						throw new PluginLoadException(c.descriptor(), c.descriptor().relativePath(goReg), e);
					}
				}

				try {
					final Class<? extends UIObjectRegistry> uiDefClazz = (Class<? extends UIObjectRegistry>) c.classLoader()
							.loadClass(c.descriptor().relativePath(uiReg));
					final UIObjectRegistry reg = uiDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
					reg.init();
				} catch (ClassNotFoundException e) {
					GlobalLogger.info(c.descriptor().toString() + " doesn't define a UIObject Registry.");
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
						final Class<? extends Registry> resourceDefClazz = (Class<? extends Registry>) c.classLoader()
								.loadClass(c.descriptor().relativePath(regName));
						final Registry reg = resourceDefClazz.getDeclaredConstructor(PluginDescriptor.class).newInstance(c.descriptor());
						reg.init();
					} catch (ClassNotFoundException e) {
						GlobalLogger.info("Couldn't find " + c.toString() + "'s Resources registry.");
						if (FAIL_ON_REGISTRY_NOT_FOUND) {
							throw new PluginLoadException(c.descriptor(), e);
						}
					} catch (RegistryFailedException e) {
						GlobalLogger.warning("Exception when initializing registry: " + regName + " from: " + c.descriptor().toString());
						if (!SKIP_FAIL_ON_REGISTRY_EXCEPTION) {
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
