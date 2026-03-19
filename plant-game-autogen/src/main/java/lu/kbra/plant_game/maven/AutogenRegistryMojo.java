package lu.kbra.plant_game.maven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

@Mojo(name = "gen-plugin-registries", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class AutogenRegistryMojo extends AutogenDefaults {

	private static final String PLUGIN_REGISTRY_CLASS = "lu.kbra.plant_game.plugin.registry.PluginRegistry";

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter(property = "failOnMissingFiles", defaultValue = "false")
	private boolean failOnMissingFiles;

	@Override
	public void execute() throws MojoExecutionException {
		final Optional<JSONObject> pluginOpt = getPluginDefinition(project);
		if (!pluginOpt.isPresent()) {
			if (failOnMissingFiles) {
				throw new MojoExecutionException("plugin.json not found");
			}
			getLog().warn("plugin.json not found, skipping registry generation");
			return;
		}

		final JSONObject pluginJson = pluginOpt.get();
		final String basePackage = pluginJson.getString("package");

		final URL[] urls = buildClassPathUrls(project);
		final ClassLoader cl = buildClassLoader(urls);

		try {
			final Class<?> registryBase = cl.loadClass(PLUGIN_REGISTRY_CLASS);

			final ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoaders(cl).forPackage(basePackage,
					cl);

			final Reflections reflections = new Reflections(builder);

			final Set<Class<?>> registries = (Set<Class<?>>) reflections.getSubTypesOf((Class) registryBase);

			final Set<String> relativeNames = registries.stream().filter(c -> !c.isInterface())
					.filter(c -> !java.lang.reflect.Modifier.isAbstract(c.getModifiers()))
					.filter(c -> !c.getName().contains("$")).map(Class::getName)
					.map(name -> toRelative(basePackage, name)).filter(c -> !c.startsWith(GENERATED_PACKAGE_NAME))
					.collect(Collectors.toSet());

			// existing registries
			final Set<String> merged = new HashSet<>();

			if (pluginJson.has("registries")) {
				final JSONArray arr = pluginJson.getJSONArray("registries");
				for (int i = 0; i < arr.length(); i++) {
					merged.add(arr.getString(i));
				}
			}

			merged.addAll(relativeNames);

			final JSONArray newArray = new JSONArray();
			merged.stream().sorted().forEach(newArray::put);

			pluginJson.put("registries", newArray);

			writePluginJson(pluginJson);

			getLog().info("Registered " + relativeNames.size() + " registries:");
			relativeNames.forEach(name -> getLog().info(" * " + name));
		} catch (Exception e) {
			throw new MojoExecutionException("Error generating plugin registries", e);
		}
	}

	private String toRelative(String basePackage, String fullName) {
		if (fullName.startsWith(basePackage)) {
			String rel = fullName.substring(basePackage.length());
			if (rel.startsWith("."))
				rel = rel.substring(1);
			return rel;
		}
		return fullName; // fallback
	}

	private void writePluginJson(JSONObject json) throws IOException {
		final File resourcesDirectory = new File(project.getBuild().getOutputDirectory());
		final File pluginFile = new File(resourcesDirectory, "plugin.json");

		try (FileWriter writer = new FileWriter(pluginFile)) {
			writer.write(json.toString(4)); // pretty print
		}
	}
}