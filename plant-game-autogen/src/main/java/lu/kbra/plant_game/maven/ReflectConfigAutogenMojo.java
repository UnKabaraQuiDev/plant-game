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

import lu.kbra.pclib.PCUtils;

@Mojo(name = "gen-reflect-config", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ReflectConfigAutogenMojo extends AutogenDefaults {

	private static final String MARKER_INTERFACE = "lu.kbra.plant_game.engine.util.NeedsFieldReflection";

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter(property = "failOnMissingFiles", defaultValue = "false")
	private boolean failOnMissingFiles;

	@Override
	public void execute() throws MojoExecutionException {

		final Optional<String> packageInOpt = this.getPluginPackage(this.project, this.failOnMissingFiles);
		if (!packageInOpt.isPresent()) {
			return;
		}

		final String basePackage = packageInOpt.get();

		final URL[] urls = this.buildClassPathUrls(this.project);
		final ClassLoader cl = this.buildClassLoader(urls);

		try {
			this.generateReflectConfig(this.project, cl, basePackage);
		} catch (final Exception e) {
			throw new MojoExecutionException("Error generating reflect-config.json", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void generateReflectConfig(final MavenProject project, final ClassLoader cl, final String basePackage)
			throws IOException, ClassNotFoundException {

		// ===== Output file =====
		final File outDir = new File(project.getBuild().getOutputDirectory(), "META-INF/native-image");

		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		final File reflectFile = new File(outDir, "reflect-config.json");

		JSONArray array;

		// ===== Load existing =====
		if (reflectFile.exists()) {
			try {
				final String content = PCUtils.readStringFile(reflectFile);
				array = new JSONArray(content);
			} catch (final Exception e) {
				this.getLog().warn("Invalid reflect-config.json, recreating");
				array = new JSONArray();
			}
		} else {
			array = new JSONArray();
		}

		// ===== Existing entries =====
		final Set<String> existing = new HashSet<>();
		for (int i = 0; i < array.length(); i++) {
			existing.add(array.getJSONObject(i).getString("name"));
		}

		// ===== Load marker interface =====
		final Class<?> marker = cl.loadClass(ReflectConfigAutogenMojo.MARKER_INTERFACE);

		// ===== Scan =====
		final ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoaders(cl).forPackage(basePackage, cl);

		final Reflections reflections = new Reflections(builder);

		final Set<Class<?>> classes = reflections.getSubTypesOf((Class<Object>) marker);

		// ===== Filter =====
		final Set<Class<?>> filtered = classes.stream()
				.filter(c -> !c.isInterface())
				.filter(c -> !java.lang.reflect.Modifier.isAbstract(c.getModifiers()))
				.filter(c -> !c.getName().contains("$"))
				.collect(Collectors.toSet());

		int added = 0;

		for (final Class<?> c : filtered) {

			if (existing.contains(c.getName())) {
				continue;
			}

			final JSONObject entry = new JSONObject();
			entry.put("name", c.getName());
			entry.put("allDeclaredFields", true);

			array.put(entry);
			added++;
		}

		// ===== Write =====
		try (FileWriter writer = new FileWriter(reflectFile)) {
			writer.write(array.toString(4));
		}

		// ===== Logging =====
		this.getLog().info("Reflect config updated:");
		this.getLog().info(" + Added: " + added + " classes");
		filtered.forEach(c -> this.getLog().info("   * " + c.getName()));
	}
}
