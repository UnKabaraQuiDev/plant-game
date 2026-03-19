package lu.kbra.plant_game.maven;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.datastructure.pair.Pairs;

public abstract class AutogenDefaults extends AbstractMojo {

	public static final String REGISTER_METHOD_NAME = "register";

	public static final String DATA_PATH_ANNOTATION = "lu.kbra.plant_game.engine.util.annotation.DataPath";
	public static final String BUFFER_SIZE_ANNOTATION = "lu.kbra.plant_game.engine.util.annotation.BufferSize";
	public static final String TEXTURE_OPTION_ANNOTATION = "lu.kbra.plant_game.engine.util.annotation.TextureOption";
	public static final String INTERNAL_CONSTRUCTOR_FUNCTION_CLASS = "lu.kbra.plant_game.engine.util.InternalConstructorFunction";
	public static final String PLUGIN_DESCRIPTOR_CLASS = "lu.kbra.plant_game.plugin.PluginDescriptor";
	public static final String TEXTURE_FILTER_CLASS = "lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter";
	public static final String TEXTURE_WRAP_CLASS = "lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap";

	public static final String DATA_PATH_MAP = PCUtils.camelCaseToConstant("dataPath");
	public static final String BUFFER_SIZE_MAP = PCUtils.camelCaseToConstant("bufferSize");
	public static final String TEXTURE_FILTER_MAP = PCUtils.camelCaseToConstant("textureFilter");
	public static final String TEXTURE_WRAP_MAP = PCUtils.camelCaseToConstant("textureWrap");

	protected static final String GENERATED_PACKAGE_NAME = "autogen";
	protected static final String GENERATED_PACKAGE_SUFFIX = "." + GENERATED_PACKAGE_NAME;
	protected static final String DEFAULT_OUTPUT_DIR = "/generated-sources/autogen/";
	protected static final String SPACER = PCUtils.repeatString(" ", 15);

	public static class RegistrySpec {

		private final String baseClassName;
		private final String registryClassName;
		private final String generatedClassName;
		private final String constructorsMapName;

		public RegistrySpec(final String baseClassName, final String registryClassName, final String generatedClassName,
				final String constructorsMapName) {
			this.baseClassName = baseClassName;
			this.registryClassName = registryClassName;
			this.generatedClassName = generatedClassName;
			this.constructorsMapName = constructorsMapName;
		}

		public String baseClassName() {
			return this.baseClassName;
		}

		public String registryClassName() {
			return this.registryClassName;
		}

		public String generatedClassName() {
			return this.generatedClassName;
		}

		public String constructorsMapName() {
			return this.constructorsMapName;
		}

		@Override
		public String toString() {
			return "RegistrySpec@" + System.identityHashCode(this) + " [baseClassName=" + this.baseClassName
					+ ", registryClassName=" + this.registryClassName + ", generatedClassName="
					+ this.generatedClassName + ", constructorsMapName=" + this.constructorsMapName + "]";
		}

	}

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
				this.getLog().info("Created source directory: " + generatedSourcesDir);
			} catch (final IOException e) {
				throw new MojoExecutionException("Error creating directory " + generatedSourcesDir, e);
			}
		}

		return new File(generatedSourcesDir);
	}

	protected URL[] buildClassPathUrls(final MavenProject project) throws MojoExecutionException {
		try {
			final File outputDirectory = new File(project.getBuild().getOutputDirectory());
			final URL outputUrl = outputDirectory.toURI().toURL();

			final List<URL> urlList = new ArrayList<>();
			urlList.add(outputUrl);

			for (final Artifact artifact : (Set<Artifact>) project.getArtifacts()) {
				if (Artifact.SCOPE_COMPILE.equals(artifact.getScope())
						|| Artifact.SCOPE_RUNTIME.equals(artifact.getScope())) {
					urlList.add(artifact.getFile().toURI().toURL());
				}
			}

			return urlList.toArray(new URL[0]);
		} catch (final Exception e) {
			throw new MojoExecutionException("Error loading compiled classpath", e);
		}
	}

	protected ClassLoader buildClassLoader(final URL[] urls) {
		return new URLClassLoader(urls, this.getClass().getClassLoader());
	}

	protected Optional<String> getPluginPackage(final MavenProject project, final boolean failOnMissingFiles)
			throws MojoExecutionException {
		final Optional<JSONObject> pluginReg = this.getPluginDefinition(project);
		if (!pluginReg.isPresent()) {
			if (failOnMissingFiles) {
				throw new MojoExecutionException("plugin.json file not found in resources directory!");
			}

			this.getLog()
					.warn("plugin.json file not found, skipping. Use failOnMissingFiles=true to fail at this point");
			return Optional.empty();
		}

		return Optional.of(pluginReg.get().getString("package"));
	}

	protected void generateRegistry(final ClassLoader scanClassLoader, final String scanPackage,
			final String outputPackage, final File outputDir, final RegistrySpec spec) throws IOException,
			ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		final Class<?> baseClass = scanClassLoader.loadClass(spec.baseClassName());
		final Class<?> internalConstructorFunctionClass = scanClassLoader
				.loadClass(INTERNAL_CONSTRUCTOR_FUNCTION_CLASS);
		final Class<? extends Annotation> dataPathClass = (Class<? extends Annotation>) scanClassLoader
				.loadClass(DATA_PATH_ANNOTATION);
		final Class<?> registryClass = scanClassLoader.loadClass(spec.registryClassName());
		final Class<? extends Annotation> bufferSizeClass = (Class<? extends Annotation>) scanClassLoader
				.loadClass(BUFFER_SIZE_ANNOTATION);
		final Class<? extends Annotation> textureOptionClass = (Class<? extends Annotation>) scanClassLoader
				.loadClass(TEXTURE_OPTION_ANNOTATION);
		final Class<?> pluginDescriptorClass = scanClassLoader.loadClass(PLUGIN_DESCRIPTOR_CLASS);
		final Class<?> textureFilterClass = scanClassLoader.loadClass(TEXTURE_FILTER_CLASS);
		final Class<?> textureWrapClass = scanClassLoader.loadClass(TEXTURE_WRAP_CLASS);

		final TypeSpec.Builder registry = TypeSpec.classBuilder(spec.generatedClassName()).superclass(registryClass)
				.addModifiers(Modifier.PUBLIC);

		registry.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(pluginDescriptorClass, "pd").addStatement("super($N)", "pd").build());

		final ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.addClassLoaders(scanClassLoader);
		builder.forPackage(scanPackage, scanClassLoader);

		final Reflections reflections = new Reflections(builder);
		final Set<? extends Class<?>> classes = reflections.getSubTypesOf((Class<Object>) baseClass);

		final Builder initMethod = MethodSpec.methodBuilder(REGISTER_METHOD_NAME).returns(TypeName.VOID)
				.addModifiers(Modifier.PUBLIC).addAnnotation(Override.class);

		for (final Class<?> c : classes) {
			if (!c.isAnnotationPresent(dataPathClass)) {
				System.err.println("Missing @DataPath on: " + c.getName());
			}
			if (java.lang.reflect.Modifier.isAbstract(c.getModifiers())) {
				continue;
			}
			if (c.getName().contains("$")) {
				System.err.println("Skipping anonymous class: " + c.getName());
				continue;
			}

			final Optional<String> dataPath = c.isAnnotationPresent(dataPathClass)
					? Optional.of(this.getStringValue(c.getAnnotation(dataPathClass)))
					: Optional.empty();
			final OptionalInt bufferSize = c.isAnnotationPresent(bufferSizeClass)
					? OptionalInt.of(this.getIntValue(c.getAnnotation(bufferSizeClass)))
					: OptionalInt.empty();
			final Optional<Annotation> textureOption = Optional.ofNullable(c.getAnnotation(textureOptionClass));
			final String listName = "list" + c.getSimpleName();

			final TypeName specificSubListType = ParameterizedTypeName.get(ClassName.get(List.class),
					ParameterizedTypeName.get(ClassName.get(internalConstructorFunctionClass),
							ClassName.get(baseClass)));

			initMethod.addCode("/* $L $T $L */\n", SPACER, TypeName.get(c), SPACER);
			initMethod.addStatement("final $T $L = new $T<>()", specificSubListType, listName, ArrayList.class);

			this.addConstructors(initMethod, c, listName, internalConstructorFunctionClass, baseClass);

			initMethod.addStatement("$L.put($T.class, $L)", spec.constructorsMapName(), c, listName);
			bufferSize.ifPresent(i -> initMethod.addStatement("$L.put($T.class, $L)", BUFFER_SIZE_MAP, c, i));
			dataPath.ifPresent(t -> initMethod.addStatement("$L.put($T.class, $S)", DATA_PATH_MAP, c, t));
			textureOption.ifPresent(t -> {
				initMethod.addStatement("$L.put($T.class, $T.$L)", TEXTURE_FILTER_MAP, c, textureFilterClass,
						this.getEnumValue(t, "textureFilter"));
				initMethod.addStatement("$L.put($T.class, $T.$L)", TEXTURE_WRAP_MAP, c, textureWrapClass,
						this.getEnumValue(t, "textureWrap"));
			});

			initMethod.addCode("\n");
		}

		registry.addMethod(initMethod.build());

		JavaFile.builder(outputPackage, registry.build()).addFileComment("@formatter:off").indent("\t").build()
				.writeTo(outputDir);
	}

	protected void addConstructors(final MethodSpec.Builder initMethod, final Class<?> targetClass,
			final String listName, final Class<?> internalConstructorFunctionClass, final Class<?> baseClass) {

		for (final Constructor<?> con : Arrays.stream(targetClass.getConstructors())
				.sorted(Comparator.comparing(Constructor<?>::getParameterCount)).collect(Collectors.toList())) {

			initMethod.addStatement(
					"$L.add(new $T<>(new $T {"
							+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "$T.class").collect(
									Collectors.joining(", "))
							+ "}, ($T[] arr) -> ($T) new $T("
							+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "($T) $L")
									.collect(Collectors.joining(", "))
							+ ")))",
					PCUtils.concatStreams(
							Stream.of(listName, internalConstructorFunctionClass, ArrayTypeName.of(Class.class)),
							Arrays.stream(con.getParameterTypes()).map(TypeName::get),
							Stream.of(Object.class, baseClass, targetClass),
							IntStream.range(0, con.getParameterCount())
									.mapToObj(i -> Pairs.readOnly(TypeName.get(con.getParameters()[i].getType()),
											"arr[" + i + "]"))
									.flatMap(z -> Stream.of(z.getKey(), z.getValue())))
							.toArray());
		}
	}

	public Enum<?> getEnumValue(final Annotation annotation, final String method) {
		try {
			final Class<?> clazz = annotation.getClass();
			final Method meth = clazz.getMethod(method);
			return (Enum<?>) meth.invoke(annotation);
		} catch (IllegalAccessException | NoSuchMethodException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public int getIntValue(final Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (int) meth.invoke(annotation);
	}

	public String getStringValue(final Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (String) meth.invoke(annotation);
	}

}