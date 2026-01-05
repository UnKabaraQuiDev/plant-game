package lu.kbra.plant_game;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

import org.junit.Test;
import org.reflections.Reflections;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureOption;
import lu.kbra.plant_game.engine.util.BuildingOption;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public class GameObjectRegistryGenMain extends GenMainConsts {

	@Test
	public void genRegistry() throws IOException {
		final TypeName functionType = ParameterizedTypeName.get(ClassName.get(InternalConstructorFunction.class),
				ClassName.get(GameObject.class));

		// Map<ClassListKey, Function<Object[], GameObject>>
		final TypeName subListType = ParameterizedTypeName.get(ClassName.get(List.class), functionType);

		// Class<? extends GameObject>
		final TypeName gameObjectClassType = ParameterizedTypeName.get(ClassName.get(Class.class),
				WildcardTypeName.subtypeOf(GameObject.class));

		// Map<Class<? extends GameObject>, Map<ClassListKey, Function<Object[], GameObject>>>
		final FieldSpec constructorHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), gameObjectClassType, subListType),
						PCUtils.camelCaseToConstant("gameObjectConstructors"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec dataPathHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), gameObjectClassType, ClassName.get(String.class)),
						PCUtils.camelCaseToConstant("dataPath"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec bufferSizeHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), gameObjectClassType, ClassName.get(Integer.class)),
						PCUtils.camelCaseToConstant("bufferSize"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec textureFilterHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), gameObjectClassType, ClassName.get(TextureFilter.class)),
						PCUtils.camelCaseToConstant("textureFilter"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec textureWrapHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), gameObjectClassType, ClassName.get(TextureWrap.class)),
						PCUtils.camelCaseToConstant("textureWrap"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final TypeSpec.Builder registry = TypeSpec.classBuilder("GameObjectRegistry")
				.addModifiers(Modifier.PUBLIC)
				.addField(constructorHashMap)
				.addField(dataPathHashMap)
				.addField(bufferSizeHashMap)
				.addField(textureFilterHashMap)
				.addField(textureWrapHashMap);

		final Reflections reflections = new Reflections(MAIN_PACKAGE);
		final Set<Class<? extends GameObject>> classes = reflections.getSubTypesOf(GameObject.class);
		classes.add(GameObject.class);

		final CodeBlock.Builder staticCodeBlock = CodeBlock.builder();

		final String spacer = PCUtils.repeatString(" ", 15);

		staticCodeBlock.addStatement("$N = new $T<>()", constructorHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", dataPathHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", bufferSizeHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", textureFilterHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", textureWrapHashMap, HashMap.class);
		staticCodeBlock.add("\n");

		for (final Class<? extends GameObject> c : classes) {
//			if (!c.isAnnotationPresent(DataPath.class)) {
//				System.err.println("Missing @DataPath on: " + c.getName());
//				continue;
//			}
			if (java.lang.reflect.Modifier.isAbstract(c.getModifiers())) {
				continue;
			}

//			final Optional<DataPath> classDataPath = c.isAnnotationPresent(DataPath.class) ? Optional.of(c.getAnnotation(DataPath.class))
//					: Optional.empty();
			final Optional<BufferSize> classBufferSize = c.isAnnotationPresent(BufferSize.class)
					? Optional.of(c.getAnnotation(BufferSize.class))
					: Optional.empty();
			final Optional<TextureOption> classTextureOption = Optional.ofNullable(c.getAnnotation(TextureOption.class));
			final String listName = "list" + c.getSimpleName();

			final TypeName specificSubListType = ParameterizedTypeName.get(ClassName.get(List.class),
					ParameterizedTypeName.get(ClassName.get(InternalConstructorFunction.class), ClassName.get(GameObject.class)));

			staticCodeBlock.add("/* $L $T $L */\n", spacer, TypeName.get(c), spacer);
			staticCodeBlock.addStatement("final $T $L = new $T<>()", specificSubListType, listName, ArrayList.class);

			boolean invalidClass = false;

			for (final Constructor<?> con : c.getConstructors()) {

				final Parameter[] parameters = con.getParameters();

				if (parameters.length == 0 || parameters[0].getType() != String.class) {
					continue;
				}

				final int paramCount = parameters.length - 1;

				if (paramCount < 1) {
					System.err.println("not enough params: " + con);
					continue;
				}

				final Class<?>[] paramTypes = new Class<?>[paramCount];
				final BuildingOption[] options = new BuildingOption[paramCount];

				for (int i = 1; i < parameters.length; i++) {
					final Parameter p = parameters[i];

					paramTypes[i - 1] = p.getType();

					final Optional<DataPath> dataPath = p.isAnnotationPresent(DataPath.class) ? Optional.of(p.getAnnotation(DataPath.class))
							: Optional.empty();
					final Optional<BufferSize> bufferSize = p.isAnnotationPresent(BufferSize.class)
							? Optional.of(p.getAnnotation(BufferSize.class))
							: classBufferSize;
					final Optional<TextureOption> textureOption = p.isAnnotationPresent(TextureOption.class)
							? Optional.ofNullable(p.getAnnotation(TextureOption.class))
							: classTextureOption;

					if (dataPath.isEmpty()) {
						System.err.println("err: no datapath on " + c);
						invalidClass = true;
					}

					if (invalidClass) {
						break;
					}

					options[i - 1] = new BuildingOption(dataPath.get().value(),
							textureOption.map(TextureOption::textureFilter).orElse(TextureFilter.NEAREST),
							textureOption.map(TextureOption::textureWrap).orElse(TextureWrap.REPEAT),
							bufferSize.map(BufferSize::value).orElse(0));
				}

				if (invalidClass) {
					break;
				}

//				System.err.println(Arrays.toString(options));
//
//				System.err.println("$L.add(new $T(new $T[] {"
//						+ Arrays.stream(paramTypes).map(clazz -> "$T.class").collect(Collectors.joining(", ")) + "}, new $T[] {"
//						+ Arrays.stream(paramTypes).map(clazz -> "new $T($S, $T.$L, $T.$L, $L)").collect(Collectors.joining(", "))
//						+ "}, (arr) -> ($T) new $T("
//						+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "($T) arr[" + i + "]").collect(Collectors.joining(", "))
//						+ ")))");
//
//				System.err.println(Arrays.toString(Arrays.stream(options)
//						.flatMap(option -> Stream.of(TypeName.get(BuildingOption.class),
//								option.getDataPath(),
//								option.getTextureFilter(),
//								option.getTextureWrap(),
//								option.getBufferSize()))
//						.toArray()));
//
//				System.err.println(Arrays.toString(PCUtils
//						.concatStreams(Stream.of(listName, TypeName.get(InternalConstructorFunction.class), TypeName.get(Object.class)),
//								Arrays.stream(paramTypes),
//								Stream.of(TypeName.get(BuildingOption.class)),
//								Arrays.stream(options)
//										.flatMap(option -> Stream.of(TypeName.get(BuildingOption.class),
//												option.getDataPath(),
//												TypeName.get(TextureFilter.class),
//												option.getTextureFilter(),
//												TypeName.get(TextureWrap.class),
//												option.getTextureWrap(),
//												option.getBufferSize())),
//								Stream.of(TypeName.get(GameObject.class), TypeName.get(c)),
//								IntStream.range(0, con.getParameterCount()).mapToObj(i -> TypeName.get(con.getParameters()[i].getType())))
//						.toArray()));

				staticCodeBlock.addStatement(
						"$L.add(new $T(new $T[] {" + Arrays.stream(paramTypes).map(clazz -> "$T.class").collect(Collectors.joining(", "))
								+ "}, new $T[] {"
								+ Arrays.stream(paramTypes).map(clazz -> "new $T<>($S, $T.$L, $T.$L, $L)").collect(Collectors.joining(", "))
								+ "}, (arr) -> ($T) new $T("
								+ IntStream.range(0, con.getParameterCount())
										.mapToObj(i -> "($T) arr[" + i + "]")
										.collect(Collectors.joining(", "))
								+ ")))",
						PCUtils.concatStreams(
								Stream.of(listName,
										TypeName.get(InternalConstructorFunction.class),
										ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class))),
								Arrays.stream(paramTypes),
								Stream.of(TypeName.get(BuildingOption.class)),
								Arrays.stream(options)
										.flatMap(option -> Stream.of(TypeName.get(BuildingOption.class),
												option.getDataPath(),
												TypeName.get(TextureFilter.class),
												option.getTextureFilter(),
												TypeName.get(TextureWrap.class),
												option.getTextureWrap(),
												option.getBufferSize())),
								Stream.of(TypeName.get(GameObject.class), TypeName.get(c)),
								IntStream.range(0, con.getParameterCount()).mapToObj(i -> TypeName.get(con.getParameters()[i].getType())))
								.toArray());
			}

			staticCodeBlock.addStatement("$N.put($T.class, $L)", constructorHashMap, c, listName);
			staticCodeBlock.add("\n");
		}

		registry.addStaticBlock(staticCodeBlock.build());

		final ClassName exceptionType = ClassName.get(GameObjectNotFound.class);
		final ClassName exceptionConstructorType = ClassName.get(GameObjectConstructorNotFound.class);
		final TypeVariableName gameObjectTypeVar = TypeVariableName.get("T", ClassName.get(GameObject.class));

		registry.addMethod(MethodSpec.methodBuilder("create")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), gameObjectTypeVar), "clazz", Modifier.FINAL)
				.addParameter(ArrayTypeName.of(ClassName.get(Object.class)), "args", Modifier.FINAL)
				.varargs()
				.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build())
				.returns(gameObjectTypeVar)
				.addTypeVariable(gameObjectTypeVar)
				.addStatement("return ($T) get(clazz, args).apply(args)", gameObjectTypeVar)
				.build());

		registry.addMethod(MethodSpec.methodBuilder("get")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), gameObjectTypeVar), "clazz", Modifier.FINAL)
				.addParameter(ArrayTypeName.of(ClassName.get(Object.class)), "args", Modifier.FINAL)
				.varargs()
				.returns(functionType)
				.addTypeVariable(gameObjectTypeVar)
				.beginControlFlow("if ($N.containsKey(clazz))", constructorHashMap)
				.addStatement("final $T<$T> bestConstructor = $N.get(clazz).parallelStream().filter((v) -> v.matches(args)).findFirst()",
						Optional.class,
						functionType,
						constructorHashMap)
				.beginControlFlow("if (bestConstructor.isPresent())")
				.addStatement("return bestConstructor.get()")
				.nextControlFlow("else")
				.addStatement("throw new $T(clazz, args)", exceptionConstructorType)
				.endControlFlow()
				.nextControlFlow("else")
				.addStatement("throw new $T(clazz, args)", exceptionType)
				.endControlFlow()
				.build());

		JavaFile.builder(GEN_PACKAGE, registry.build()).addFileComment("@formatter:off").indent("\t").build().writeTo(SRC_MAIN_JAVA_DIR);
	}

}
