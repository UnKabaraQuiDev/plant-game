package lu.kbra.plant_game.maven;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Optional;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import lu.kbra.pclib.PCUtils;

@Mojo(name = "gen-ui-registry", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class UIAutogenMojo extends AutogenDefaults {

	private static final RegistrySpec SPEC = new RegistrySpec("lu.kbra.plant_game.engine.entity.ui.UIObject",
			"lu.kbra.plant_game.plugin.registry.UIObjectRegistry",
			"GenUIRegistry",
			PCUtils.camelCaseToConstant("uiObjectConstructors"));

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter(property = "outputDir", defaultValue = DEFAULT_OUTPUT_DIR, required = false)
	private String outputDir;

	@Parameter(property = "failOnMissingFiles", defaultValue = "false")
	private boolean failOnMissingFiles;

	@Override
	public void execute() throws MojoExecutionException {
		final File generatedSourcesDir = genOutputDir(project, outputDir);

		final Optional<String> packageInOpt = getPluginPackage(project, failOnMissingFiles);
		if (!packageInOpt.isPresent()) {
			return;
		}

		final String packageIn = packageInOpt.get();
		final String packageOut = packageIn + GENERATED_PACKAGE_SUFFIX;
		this.getLog().info("Plugin generated package: " + packageOut);

		final URL[] urls = buildClassPathUrls(project);
		final ClassLoader cl = buildClassLoader(urls);

		try {
			this.generateRegistry(cl, packageIn, packageOut, generatedSourcesDir, SPEC);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException
				| RuntimeException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error generating UI registry: ", e);
		}
	}
}
