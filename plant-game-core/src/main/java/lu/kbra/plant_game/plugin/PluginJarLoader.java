package lu.kbra.plant_game.plugin;

import static lu.kbra.plant_game.PGLogic.OBJECT_MAPPER;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import lu.kbra.pclib.PCUtils;

public class PluginJarLoader {

	public record LoadedPlugin(Path jarPath, PluginDescriptor descriptor, ClassLoader classLoader, PluginMain main) {
	}

	private final ClassLoader pluginClassLoader = PluginJarLoader.class.getClassLoader();

	public List<LoadedPlugin> loadAll(
			final PluginManager parent,
			final List<Path> pluginsDirs,
			final List<PluginDescriptor> existingPlugins) throws Exception {
		final Map<String, Path> pluginJars = new HashMap<>();
		final Map<String, PluginDescriptor> descriptors = new HashMap<>();

		for (Path pluginsDir : pluginsDirs) {
			Files.createDirectories(pluginsDir);

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(pluginsDir, "*.jar")) {
				for (final Path jarPath : stream) {
					final PluginDescriptor desc = this.loadDescriptor(jarPath);

					final String name = desc.getInternalName();
					if (pluginJars.putIfAbsent(name, jarPath) != null) {
						throw new IllegalStateException("Duplicate plugin: " + name);
					}

					descriptors.put(name, desc);
				}
			}
		}

		final Set<String> duplicates = PCUtils
				.computeDuplicates(descriptors.values(), PluginDescriptor::getInternalName, PluginDescriptor::toString);
		if (duplicates.size() > 0) {
			throw new IllegalStateException("Duplicate plugins found: " + duplicates);
		}

		final List<String> order = this.resolveLoadOrder(descriptors);

		final Map<String, ClassLoader> loaders = new HashMap<>();
		final List<LoadedPlugin> result = new ArrayList<>();

		for (PluginDescriptor desc : existingPlugins) {
			final ClassLoader loader = desc.getClass().getClassLoader();
			final Class<? extends PluginMain> main = (Class<? extends PluginMain>) loader.loadClass(desc.relativePath(desc.getMainClass()));
			final PluginMain mainInst = main.getDeclaredConstructor(PluginManager.class, PluginDescriptor.class).newInstance(parent, desc);
			desc.setPluginClass(main);

			result.add(new LoadedPlugin(null, desc, loader, mainInst));
		}

		for (final String name : order) {
			final Path jar = pluginJars.get(name);
			final ClassLoader loader = this.createClassLoader(jar);

			loaders.put(name, loader);

			final PluginDescriptor desc = descriptors.get(name);
			final Class<? extends PluginMain> main = (Class<? extends PluginMain>) loader.loadClass(desc.relativePath(desc.getMainClass()));
			final PluginMain mainInst = main.getDeclaredConstructor(PluginManager.class, PluginDescriptor.class).newInstance(parent, desc);
			desc.setPluginClass(main);

			result.add(new LoadedPlugin(jar, desc, loader, mainInst));
		}

		return result;
	}

	private PluginDescriptor loadDescriptor(final Path jarPath) throws Exception {
		try (JarFile jar = new JarFile(jarPath.toFile())) {
			final ZipEntry entry = jar.getEntry("plugin.json");
			if (entry == null) {
				throw new IllegalStateException("plugin.json missing in " + jarPath);
			}

			try (InputStream in = jar.getInputStream(entry)) {
				return OBJECT_MAPPER.readValue(in, PluginDescriptor.class);
			}
		}
	}

	private List<String> resolveLoadOrder(final Map<String, PluginDescriptor> descriptors) {

		final Map<String, Set<String>> graph = new HashMap<>();

		for (final var e : descriptors.entrySet()) {
			final String plugin = e.getKey();
			graph.putIfAbsent(plugin, new HashSet<>());

			final PluginDescriptor.Dependencies deps = e.getValue().getDependencies();
			if (deps == null) {
				continue;
			}

			/* Required dependencies */

			if (deps.getRequired() != null) {
				for (final var dep : deps.getRequired()) {

					final PluginDescriptor target = descriptors.get(dep.getInternalName());
					if (target == null) {
						throw new IllegalStateException("Missing required dependency: " + dep.getInternalName() + " for plugin " + plugin);
					}

					if (!dep.getVersion().matches(target.getVersion())) {
						throw new IllegalStateException("Version mismatch: " + plugin + " requires " + dep.getInternalName() + " "
								+ dep.getVersion() + ", found " + target.getVersion());
					}

					graph.get(plugin).add(dep.getInternalName());
				}
			}

			/* Optional dependencies */

			if (deps.getOptional() != null) {
				for (final var dep : deps.getOptional()) {

					final PluginDescriptor target = descriptors.get(dep.getInternalName());
					if (target == null) {
						continue;
					}

					if (!dep.getVersion().matches(target.getVersion())) {
						throw new IllegalStateException("Version mismatch: " + plugin + " optionally depends on " + dep.getInternalName()
								+ " " + dep.getVersion() + ", found " + target.getVersion());
					}

					graph.get(plugin).add(dep.getInternalName());
				}
			}
		}

		return this.topoSort(graph);
	}

	private List<String> topoSort(final Map<String, Set<String>> graph) {
		final Map<String, State> state = new HashMap<>();
		final List<String> result = new ArrayList<>();

		for (final String node : graph.keySet()) {
			if (state.get(node) == null) {
				this.dfs(node, graph, state, result, new ArrayDeque<>());
			}
		}

		Collections.reverse(result);
		return result;
	}

	private void dfs(
			final String node,
			final Map<String, Set<String>> graph,
			final Map<String, State> state,
			final List<String> out,
			final Deque<String> stack) {
		state.put(node, State.VISITING);
		stack.push(node);

		for (final String dep : graph.getOrDefault(node, Set.of())) {
			final State s = state.get(dep);

			if (s == State.VISITING) {
				throw new IllegalStateException("Dependency cycle: " + this.buildCycle(dep, stack));
			}

			if (s == null) {
				this.dfs(dep, graph, state, out, stack);
			}
		}

		stack.pop();
		state.put(node, State.VISITED);
		out.add(node);
	}

	private String buildCycle(final String start, final Deque<String> stack) {
		final StringBuilder sb = new StringBuilder();
		for (final String s : stack) {
			sb.append(s).append(" -> ");
			if (s.equals(start)) {
				break;
			}
		}
		return sb.append(start).toString();
	}

	private enum State {
		VISITING, VISITED
	}

	private ClassLoader createClassLoader(final Path jarPath) throws Exception {
		return new URLClassLoader(new URL[] { jarPath.toUri().toURL() }, this.pluginClassLoader);
	}

	@Override
	public String toString() {
		return "PluginJarLoader@" + System.identityHashCode(this) + " [pluginClassLoader=" + this.pluginClassLoader + "]";
	}

}
