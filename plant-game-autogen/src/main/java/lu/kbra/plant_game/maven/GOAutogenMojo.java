package lu.kbra.plant_game.maven;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
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
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
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
import com.squareup.javapoet.WildcardTypeName;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.datastructure.pair.Pairs;

@Mojo(name = "gen-go-registry", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GOAutogenMojo extends AutogenDefaults {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter(property = "outputDir", defaultValue = "/generated-sources/autogen/", required = false)
	private String outputDir;

	@Parameter(property = "failOnMissingFiles", defaultValue = "false")
	private boolean failOnMissingFiles;

	@Override
	public void execute() throws MojoExecutionException {
		final File generatedSourcesDir = genOutputDir(project, outputDir);

		final String packageIn;
		final String packageOut;

		final Optional<JSONObject> pluginReg = getPluginDefinition(project);
		if (!pluginReg.isPresent()) {
			if (failOnMissingFiles) {
				throw new MojoExecutionException("plugin.json file not found in resources directory!");
			} else {
				getLog().warn(
						"plugin.json file not found, skipping. Use failOnMissingFiles=true to fail at this point");
				return;
			}
		}

		packageIn = pluginReg.get().getString("package");
		packageOut = packageIn + ".autogen";
		this.getLog().info("Plugin generated package: " + packageOut);

		final ClassLoader cl;
		final URL[] urls;

		try {
			final File outputDirectory = new File(this.project.getBuild().getOutputDirectory());

			final URL url = outputDirectory.toURI().toURL();

			final List<URL> urlList = new ArrayList<>();

			urlList.add(url);

			for (final Artifact artifact : (Set<Artifact>) this.project.getArtifacts()) {
				if (artifact.getScope().equals(Artifact.SCOPE_COMPILE)
						|| artifact.getScope().equals(Artifact.SCOPE_RUNTIME)) {
					urlList.add(artifact.getFile().toURI().toURL());
				}
			}

			urls = urlList.toArray(new URL[0]);
			cl = new URLClassLoader(urls, this.getClass().getClassLoader());
		} catch (final Exception e) {
			throw new MojoExecutionException("Error loading compiled class", e);
		}

		try {
			this.genRegistry(cl, urls, packageIn, packageOut, generatedSourcesDir);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
				| IOException | RuntimeException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error generating GO registry: ", e);
		}

//		this.project.addCompileSourceRoot(generatedSourcesDir);
//		project.getCompileSourceRoots().add(generatedSourcesDir);
	}

	public void genRegistry(final ClassLoader scanClassLoader, final URL[] urls, final String scanPackage,
			final String outputPackage, final File outputDir) throws IOException, ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> gameObjectClass = scanClassLoader.loadClass("lu.kbra.plant_game.engine.entity.go.GameObject");
		final Class<?> internalConstructorFunctionClass = scanClassLoader
				.loadClass("lu.kbra.plant_game.engine.util.InternalConstructorFunction");
		final Class<? extends Annotation> dataPathClass = (Class<? extends Annotation>) scanClassLoader
				.loadClass("lu.kbra.plant_game.engine.util.annotation.DataPath");
		final Class<?> gameObjectRegistryClass = scanClassLoader
				.loadClass("lu.kbra.plant_game.plugin.registry.GameObjectRegistry");
		final Class<? extends Annotation> bufferSizeClass = (Class<? extends Annotation>) scanClassLoader
				.loadClass("lu.kbra.plant_game.engine.util.annotation.BufferSize");
		final Class<? extends Annotation> textureOptionClass = (Class<? extends Annotation>) scanClassLoader
				.loadClass("lu.kbra.plant_game.engine.util.annotation.TextureOption");
		final Class<? extends Exception> gameObjectNotFoundClass = (Class<? extends Exception>) scanClassLoader
				.loadClass("lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound");
		final Class<? extends Exception> gameObjectConstructorNotFoundClass = (Class<? extends Exception>) scanClassLoader
				.loadClass("lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound");
		final Class<?> pluginDescriptorClass = scanClassLoader.loadClass("lu.kbra.plant_game.plugin.PluginDescriptor");
		final Class<?> textureFilterClass = scanClassLoader
				.loadClass("lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter");
		final Class<?> textureWrapClass = scanClassLoader
				.loadClass("lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap");

		final TypeName functionType = ParameterizedTypeName.get(ClassName.get(internalConstructorFunctionClass),
				ClassName.get(gameObjectClass));

		// Map<ClassListKey, Function<Object[], GameObject>>
		final TypeName subListType = ParameterizedTypeName.get(ClassName.get(List.class), functionType);

		// Class<? extends GameObject>
		final TypeName gameObjectClassType = ParameterizedTypeName.get(ClassName.get(Class.class),
				WildcardTypeName.subtypeOf(gameObjectClass));

		final String gameObjectConstructorsMap = PCUtils.camelCaseToConstant("gameObjectConstructors");
		final String dataPathMap = PCUtils.camelCaseToConstant("dataPath");
		final String bufferSizeMap = PCUtils.camelCaseToConstant("bufferSize");
		final String textureFilterMap = PCUtils.camelCaseToConstant("textureFilter");
		final String textureWrapMap = PCUtils.camelCaseToConstant("textureWrap");
//		final String defaultPriceMap = PCUtils.camelCaseToConstant("defaultPrice");

		final TypeSpec.Builder registry = TypeSpec.classBuilder("GenGORegistry").superclass(gameObjectRegistryClass)
				.addModifiers(Modifier.PUBLIC);

		registry.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(pluginDescriptorClass, "pd").addStatement("super($N)", "pd").build());

		final ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.addClassLoaders(scanClassLoader);
		builder.forPackage(scanPackage, scanClassLoader);
		final Reflections reflections = new Reflections(builder);
		final Set<? extends Class<?>> classes = reflections.getSubTypesOf(gameObjectClass);
//		classes.add(gameObjectClass);

		final Builder initMethod = MethodSpec.methodBuilder(AutogenDefaults.REGISTER_METHOD_NAME).returns(TypeName.VOID).addModifiers(Modifier.PUBLIC)
				.addAnnotation(Override.class);

		final String spacer = PCUtils.repeatString(" ", 15);

		for (final Class<?> c : classes) {
			if (!c.isAnnotationPresent(dataPathClass)) {
				System.err.println("Missing @DataPath on: " + c.getName());
//				continue;
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
							ClassName.get(gameObjectClass)));

			initMethod.addCode("/* $L $T $L */\n", spacer, TypeName.get(c), spacer);
			initMethod.addStatement("final $T $L = new $T<>()", specificSubListType, listName, ArrayList.class);

			for (final Constructor<?> con : Arrays.stream(c.getConstructors())
					.sorted(Comparator.comparing(Constructor<?>::getParameterCount)).collect(Collectors.toList())) {

				initMethod.addStatement(
						"$L.add(new $T<>(new $T {" + IntStream.range(0, con.getParameterCount())
								.mapToObj(i -> "$T.class").collect(Collectors.joining(", "))
								+ "}, ($T[] arr) -> ($T) new $T("
								+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "($T) $L")
										.collect(Collectors.joining(", "))
								+ ")))",
						PCUtils.concatStreams(
								Stream.of(listName, internalConstructorFunctionClass, ArrayTypeName.of(Class.class)),
								Arrays.stream(con.getParameterTypes()).map(TypeName::get),
								Stream.of(Object.class, gameObjectClass, c),
								IntStream.range(0, con.getParameterCount())
										.mapToObj(i -> Pairs.readOnly(TypeName.get(con.getParameters()[i].getType()),
												"arr[" + i + "]"))
										.flatMap(z -> Stream.of(z.getKey(), z.getValue())))
								.toArray());

			}

			initMethod.addStatement(gameObjectConstructorsMap + ".put($T.class, $L)", c, listName);
			bufferSize.ifPresent(i -> initMethod.addStatement(bufferSizeMap + ".put($T.class, $L)", c, i));
			dataPath.ifPresent(t -> initMethod.addStatement(dataPathMap + ".put($T.class, $S)", c, t));
			textureOption.ifPresent(t -> {
				initMethod.addStatement(textureFilterMap + ".put($T.class, $T.$L)", c, textureFilterClass,
						this.getEnumValue(t, "textureFilter"));
				initMethod.addStatement(textureWrapMap + ".put($T.class, $T.$L)", c, textureWrapClass,
						this.getEnumValue(t, "textureWrap"));
			});
//			price.ifPresent(p -> staticCodeBlock.addStatement("$N.put($T.class, $L)", defaultPriceHashMap, c, p));
			initMethod.addCode("\n");
		}

		registry.addMethod(initMethod.build());

		JavaFile.builder(outputPackage, registry.build()).addFileComment("@formatter:off").indent("\t").build()
				.writeTo(outputDir);
	}

}