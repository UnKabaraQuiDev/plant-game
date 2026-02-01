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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.json.JSONObject;
import org.reflections.Reflections;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pairs;

@Mojo(name = "gen-go-registry", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class GenerateSourcesMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;

	@Override
	public void execute() throws MojoExecutionException {
		final String generatedSourcesDir = project.getBuild().getDirectory() + "/generated-sources/autogen/";
		final Path path = Paths.get(generatedSourcesDir);
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
				project.addCompileSourceRoot(generatedSourcesDir);
				getLog().info("Created source directory: " + generatedSourcesDir);
			} catch (IOException e) {
				throw new MojoExecutionException("Error creating directory " + generatedSourcesDir, e);
			}
		}

		final String packageIn;
		final String packageOut;

		try {
			final File resourcesDirectory = new File(project.getBuild().getOutputDirectory());
			final File pluginJson = new File(resourcesDirectory, "plugin.json");
			if (!pluginJson.exists()) {
				throw new IOException("plugin.json file not found in resources directory! -> " + pluginJson.getPath());
			}

			final JSONObject pluginsObj = new JSONObject(
					new String(Files.readAllBytes(Paths.get(pluginJson.getPath()))));
			packageIn = pluginsObj.getString("package");
			packageOut = packageIn + ".autogen";
			getLog().info("Plugin generated package: " + packageOut);
		} catch (IOException e) {
			throw new MojoExecutionException("Error reading plugin.json ", e);
		}

		final ClassLoader cl;

		try {
			File outputDirectory = new File(project.getBuild().getOutputDirectory());

			URL url = outputDirectory.toURI().toURL();
			URL[] urls = new URL[] { url };

			cl = new URLClassLoader(urls, getClass().getClassLoader());
		} catch (Exception e) {
			throw new MojoExecutionException("Error loading compiled class", e);
		}

		try {
			genRegistry(cl, packageIn, packageOut, new File(generatedSourcesDir));
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
				| IOException | RuntimeException e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error generating GO registry: ", e);
		}
	}

	public void genRegistry(final ClassLoader scanClassLoader, String scanPackage, String outputPackage,
			final File outputDir) throws IOException, ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		final Class<?> gameObjectClass = Class.forName("lu.kbra.plant_game.engine.entity.go.GameObject");
		final Class<?> internalConstructorFunctionClass = Class
				.forName("lu.kbra.plant_game.engine.util.InternalConstructorFunction");
		final Class<? extends Annotation> dataPathClass = (Class<? extends Annotation>) Class
				.forName("lu.kbra.plant_game.engine.util.annotation.DataPath");
		final Class<?> gameObjectRegistryClass = Class.forName("lu.kbra.plant_game.GameObjectRegistry");
		final Class<? extends Annotation> bufferSizeClass = (Class<? extends Annotation>) Class
				.forName("lu.kbra.plant_game.engine.util.annotation.BufferSize");
		final Class<? extends Annotation> textureOptionClass = (Class<? extends Annotation>) Class
				.forName("lu.kbra.plant_game.engine.util.annotation.TextureOption");
		final Class<? extends Exception> gameObjectNotFoundClass = (Class<? extends Exception>) Class
				.forName("lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound");
		final Class<? extends Exception> gameObjectConstructorNotFoundClass = (Class<? extends Exception>) Class
				.forName("lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound");

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
		final String defaultPriceMap = PCUtils.camelCaseToConstant("defaultPrice");

		final TypeSpec.Builder registry = TypeSpec.classBuilder("GenGORegistry").superclass(gameObjectRegistryClass)
				.addModifiers(Modifier.PUBLIC);

		final Reflections reflections = new Reflections(scanPackage);
		final Set<? extends Class<?>> classes = reflections.getSubTypesOf(gameObjectClass);
//		classes.add(gameObjectClass);

		final CodeBlock.Builder staticCodeBlock = CodeBlock.builder();

		final String spacer = PCUtils.repeatString(" ", 15);

		for (final Class<?> c : classes) {
			if (!c.isAnnotationPresent(dataPathClass)) {
				System.err.println("Missing @DataPath on: " + c.getName());
//				continue;
			}
			if (java.lang.reflect.Modifier.isAbstract(c.getModifiers())) {
				continue;
			}

			final Optional<String> dataPath = c.isAnnotationPresent(dataPathClass)
					? Optional.of(getStringValue(c.getAnnotation(dataPathClass)))
					: Optional.empty();
			final OptionalInt bufferSize = c.isAnnotationPresent(textureOptionClass)
					? OptionalInt.of(getIntValue(c.getAnnotation(textureOptionClass)))
					: OptionalInt.empty();
			final Optional<Annotation> textureOption = Optional.ofNullable(c.getAnnotation(textureOptionClass));
			final String listName = "list" + c.getSimpleName();

			final TypeName specificSubListType = ParameterizedTypeName.get(ClassName.get(List.class),
					ParameterizedTypeName.get(ClassName.get(internalConstructorFunctionClass),
							ClassName.get(gameObjectClass)));

			staticCodeBlock.add("/* $L $T $L */\n", spacer, TypeName.get(c), spacer);
			staticCodeBlock.addStatement("final $T $L = new $T<>()", specificSubListType, listName, ArrayList.class);

			for (final Constructor<?> con : Arrays.stream(c.getConstructors())
					.sorted(Comparator.comparing(Constructor<?>::getParameterCount)).collect(Collectors.toList())) {

				staticCodeBlock.addStatement(
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

			staticCodeBlock.addStatement(gameObjectConstructorNotFoundClass + ".put($T.class, $L)", c, listName);
			bufferSize.ifPresent(i -> staticCodeBlock.addStatement(bufferSizeMap + ".put($T.class, $L)", c, i));
			dataPath.ifPresent(t -> staticCodeBlock.addStatement(dataPathMap + ".put($T.class, $S)", c, t));
			textureOption.ifPresent(t -> {
				staticCodeBlock.addStatement(textureFilterMap + ".put($T.class, $L)", c, getEnumValue(t));
				staticCodeBlock.addStatement(textureWrapMap + ".put($T.class, $L)", c, getEnumValue(t));
			});
//			price.ifPresent(p -> staticCodeBlock.addStatement("$N.put($T.class, $L)", defaultPriceHashMap, c, p));
			staticCodeBlock.add("\n");
		}

		registry.addStaticBlock(staticCodeBlock.build());

		final ClassName exceptionType = ClassName.get(gameObjectNotFoundClass);
		final ClassName exceptionConstructorType = ClassName.get(gameObjectConstructorNotFoundClass);
		final TypeVariableName gameObjectTypeVar = TypeVariableName.get("T", ClassName.get(gameObjectClass));

		registry.addMethod(MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), gameObjectTypeVar), "clazz",
						Modifier.FINAL)
				.addParameter(ArrayTypeName.of(ClassName.get(Object.class)), "args", Modifier.FINAL).varargs()
				.addAnnotation(
						AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build())
				.returns(gameObjectTypeVar).addTypeVariable(gameObjectTypeVar)
				.addStatement("return ($T) get(clazz, args).apply(args)", gameObjectTypeVar).build());

		registry.addMethod(MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), gameObjectTypeVar), "clazz",
						Modifier.FINAL)
				.addParameter(ArrayTypeName.of(ClassName.get(Object.class)), "args", Modifier.FINAL).varargs()
				.returns(functionType).addTypeVariable(gameObjectTypeVar)
				.beginControlFlow("if ($N.containsKey(clazz))", gameObjectConstructorsMap)
				.addStatement(
						"final $T<$T> bestConstructor = " + gameObjectConstructorNotFoundClass
								+ ".get(clazz).parallelStream().filter((v) -> v.matches(args)).findFirst()",
						Optional.class, functionType)
				.beginControlFlow("if (bestConstructor.isPresent())").addStatement("return bestConstructor.get()")
				.nextControlFlow("else").addStatement("throw new $T(clazz, args)", exceptionConstructorType)
				.endControlFlow().nextControlFlow("else").addStatement("throw new $T(clazz, args)", exceptionType)
				.endControlFlow().build());

		JavaFile.builder(outputPackage, registry.build()).addFileComment("@formatter:off").indent("\t").build()
				.writeTo(outputDir);
	}

	private Enum<?> getEnumValue(Annotation annotation) {
		try {
			final Class<?> clazz = annotation.getClass();
			final Method meth = clazz.getMethod("value");
			return (Enum<?>) meth.invoke(annotation);
		} catch (IllegalAccessException | NoSuchMethodException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private int getIntValue(Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (int) meth.invoke(annotation);
	}

	private String getStringValue(Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (String) meth.invoke(annotation);
	}

}