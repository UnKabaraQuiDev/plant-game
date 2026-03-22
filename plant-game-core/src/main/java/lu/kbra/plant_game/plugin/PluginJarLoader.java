package lu.kbra.plant_game.plugin;

import static lu.kbra.plant_game.PGLogic.OBJECT_MAPPER;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import lu.kbra.pclib.datastructure.tree.dependency.DependencyResolver;
import lu.kbra.pclib.logger.GlobalLogger;

public class PluginJarLoader {

	public record LoadedPlugin(Path jarPath, PluginDescriptor descriptor, ClassLoader classLoader, PluginMain main) {

		public boolean isInternal() {
			return this.jarPath == null;
		}

	}

	private final ClassLoader pluginClassLoader = PluginJarLoader.class.getClassLoader();

	public List<LoadedPlugin> loadAll(
			final PluginManager parent,
			final List<Path> pluginsDirs,
			final List<PluginDescriptor> existingPlugins) throws Exception {
		final Map<String, Path> pluginJars = new HashMap<>();
		final Map<String, PluginDescriptor> descriptors = new HashMap<>();

		for (Path pluginsDir : pluginsDirs) {
			pluginsDir = pluginsDir.normalize();
			Files.createDirectories(pluginsDir);

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(pluginsDir, "*.jar")) {
				for (final Path jarPath : stream) {
					final PluginDescriptor desc = this.loadDescriptor(jarPath);

					if (desc == null) {
						GlobalLogger.info("Skipping plugin: " + jarPath + ", no `plugin.json` found.");
						continue;
					}

					final String name = desc.getInternalName();
					if (pluginJars.putIfAbsent(name, jarPath) != null) {
						throw new IllegalStateException("Duplicate plugin: " + name);
					}

					descriptors.put(name, desc);
				}
			}
		}

		final List<String> order = this.resolveLoadOrder(descriptors);

		final Map<String, ClassLoader> loaders = new HashMap<>();
		final List<LoadedPlugin> result = new ArrayList<>();

		int id = 0;

		for (PluginDescriptor desc : existingPlugins) {
			final ClassLoader loader = desc.getClass().getClassLoader();
			final Class<? extends PluginMain> main = (Class<? extends PluginMain>) loader
					.loadClass(desc.relativeClassPath(desc.getMainClass()));
			final PluginMain mainInst = main.getDeclaredConstructor(PluginManager.class, PluginDescriptor.class).newInstance(parent, desc);
			desc.setPluginClass(main);
			desc.setLoadId(id);

			result.add(new LoadedPlugin(null, desc, loader, mainInst));

			id++;
		}

		for (final String name : order) {
			final Path jar = pluginJars.get(name);
			final ClassLoader loader = this.createClassLoader(jar);

			loaders.put(name, loader);

			final PluginDescriptor desc = descriptors.get(name);
			final Class<? extends PluginMain> main = (Class<? extends PluginMain>) loader
					.loadClass(desc.relativeClassPath(desc.getMainClass()));
			final PluginMain mainInst = main.getDeclaredConstructor(PluginManager.class, PluginDescriptor.class).newInstance(parent, desc);
			desc.setPluginClass(main);
			desc.setLoadId(id);

			result.add(new LoadedPlugin(jar, desc, loader, mainInst));

			id++;
		}

		return result;
	}

	private PluginDescriptor loadDescriptor(final Path jarPath) throws Exception {
		try (JarFile jar = new JarFile(jarPath.toFile())) {
			final ZipEntry entry = jar.getEntry("plugin.json");
			if (entry == null) {
//				throw new IllegalStateException("plugin.json missing in " + jarPath);
				return null;
			}

			try (InputStream in = jar.getInputStream(entry)) {
				final PluginDescriptor pd = OBJECT_MAPPER.readValue(in, PluginDescriptor.class);
				pd.setSourceJar(jarPath.toString());
				return pd;
			}
		}
	}

	private List<String> resolveLoadOrder(final Map<String, PluginDescriptor> descriptors) {
		for (final Map.Entry<String, PluginDescriptor> entry : descriptors.entrySet()) {
			final String plugin = entry.getKey();
			final PluginDescriptor descriptor = entry.getValue();
			final PluginDescriptor.InternalDependencies deps = descriptor.getInternalDependencies();

			if (deps == null) {
				continue;
			}

			if (deps.getRequired() != null) {
				for (final PluginDescriptor.InternalDependencies.VersionnedPluginDescriptor dep : deps.getRequired()) {
					final PluginDescriptor target = descriptors.get(dep.getInternalName());
					if (target == null) {
						throw new IllegalStateException("Missing required dependency: " + dep.getInternalName() + " for plugin " + plugin);
					}

					if (!dep.getVersion().matches(target.getVersion())) {
						throw new IllegalStateException("Version mismatch: " + plugin + " requires " + dep.getInternalName() + " "
								+ dep.getVersion() + ", found " + target.getVersion());
					}
				}
			}

			if (deps.getOptional() != null) {
				for (final PluginDescriptor.InternalDependencies.VersionnedPluginDescriptor dep : deps.getOptional()) {
					final PluginDescriptor target = descriptors.get(dep.getInternalName());
					if (target == null) {
						continue;
					}

					if (!dep.getVersion().matches(target.getVersion())) {
						throw new IllegalStateException("Version mismatch: " + plugin + " optionally depends on " + dep.getInternalName()
								+ " " + dep.getVersion() + ", found " + target.getVersion());
					}
				}
			}
		}

		return DependencyResolver.of(descriptors.values()).resolve((plugin, dependency) -> {
			final PluginDescriptor descriptor = descriptors.get(plugin);
			if (descriptor == null) {
				return false;
			}

			return descriptor.getInternalDependencies()
					.getOptional()
					.stream()
					.anyMatch(dep -> Objects.equals(dep.getInternalName(), dependency));
		}).stream().map(PluginDescriptor::getInternalName).toList();
	}

	private ClassLoader createClassLoader(final Path jarPath) throws Exception {
		return new URLClassLoader(new URL[] { jarPath.toUri().toURL() }, this.pluginClassLoader);
	}

	@Override
	public String toString() {
		return "PluginJarLoader@" + System.identityHashCode(this) + " [pluginClassLoader=" + this.pluginClassLoader + "]";
	}

}
