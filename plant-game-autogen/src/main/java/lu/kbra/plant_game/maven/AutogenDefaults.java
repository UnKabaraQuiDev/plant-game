package lu.kbra.plant_game.maven;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.json.JSONObject;

public abstract class AutogenDefaults extends AbstractMojo {

	public static final String REGISTER_METHOD_NAME = "register";

	public Optional<JSONObject> getPluginDefinition(final MavenProject project) throws MojoExecutionException {
		try {
			final File resourcesDirectory = new File(project.getBuild().getOutputDirectory());
			final File pluginJson = new File(resourcesDirectory, "plugin.json");
			if (!pluginJson.exists()) {
				return Optional.empty();
			}

			final JSONObject pluginsObj = new JSONObject(
					new String(Files.readAllBytes(Paths.get(pluginJson.getPath()))));

			return Optional.of(pluginsObj);
		} catch (final IOException e) {
			throw new MojoExecutionException("Error reading plugin.json ", e);
		}
	}

	public File genOutputDir(final MavenProject project, final String outputDir) throws MojoExecutionException {
		final String generatedSourcesDir = project.getBuild().getDirectory() + outputDir;
		final Path path = Paths.get(generatedSourcesDir);
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
//				project.addCompileSourceRoot(generatedSourcesDir);
				getLog().info("Created source directory: " + generatedSourcesDir);
			} catch (final IOException e) {
				throw new MojoExecutionException("Error creating directory " + generatedSourcesDir, e);
			}
		}

		return new File(generatedSourcesDir);
	}

	public Enum<?> getEnumValue(Annotation annotation, String method) {
		try {
			final Class<?> clazz = annotation.getClass();
			final Method meth = clazz.getMethod(method);
			return (Enum<?>) meth.invoke(annotation);
		} catch (IllegalAccessException | NoSuchMethodException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public int getIntValue(Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (int) meth.invoke(annotation);
	}

	public String getStringValue(Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (String) meth.invoke(annotation);
	}

}
