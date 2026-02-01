package lu.kbra.plant_game;

import java.io.IOException;
import java.lang.reflect.Constructor;
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

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.util.annotation.DefaultPrice;
import lu.kbra.plant_game.engine.util.annotation.TextureOption;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;

public class GameObjectRegistryGenMain extends GenMainConsts {

	public void genRegistry(String ) throws IOException {
		final TypeName functionType = ParameterizedTypeName.get(ClassName.get(InternalConstructorFunction.class),
				ClassName.get(GameObject.class));

		// Map<ClassListKey, Function<Object[], GameObject>>
		final TypeName subListType = ParameterizedTypeName.get(ClassName.get(List.class), functionType);

		// Class<? extends GameObject>
		final TypeName gameObjectClassType = ParameterizedTypeName.get(ClassName.get(Class.class),
				WildcardTypeName.subtypeOf(GameObject.class));

		final String gameObjectConstructors = PCUtils.camelCaseToConstant("gameObjectConstructors");
		final String dataPath = PCUtils.camelCaseToConstant("dataPath");
		final String bufferSize = PCUtils.camelCaseToConstant("bufferSize");
		final String textureFilter = PCUtils.camelCaseToConstant("textureFilter");
		final String textureWrap = PCUtils.camelCaseToConstant("textureWrap");
		final String defaultPrice = PCUtils.camelCaseToConstant("defaultPrice");

		final TypeSpec.Builder registry = TypeSpec.classBuilder("GenGORegistry")
				.superclass(GameObjectRegistry.class)
				.addModifiers(Modifier.PUBLIC);

		final Reflections reflections = new Reflections(MAIN_PACKAGE);
		final Set<Class<? extends GameObject>> classes = reflections.getSubTypesOf(GameObject.class);
		classes.add(GameObject.class);

		final CodeBlock.Builder staticCodeBlock = CodeBlock.builder();

		final String spacer = PCUtils.repeatString(" ", 15);

		for (final Class<? extends GameObject> c : classes) {
			if (!c.isAnnotationPresent(DataPath.class)) {
				System.err.println("Missing @DataPath on: " + c.getName());
//				continue;
			}
			if (java.lang.reflect.Modifier.isAbstract(c.getModifiers())) {
				continue;
			}

			final Optional<String> dataPath = c.isAnnotationPresent(DataPath.class) ? Optional.of(c.getAnnotation(DataPath.class).value())
					: Optional.empty();
			final OptionalInt bufferSize = c.isAnnotationPresent(BufferSize.class)
					? OptionalInt.of(c.getAnnotation(BufferSize.class).value())
					: OptionalInt.empty();
			final Optional<TextureOption> textureOption = Optional.ofNullable(c.getAnnotation(TextureOption.class));
			final String listName = "list" + c.getSimpleName();
			final Optional<Integer> price = Optional.ofNullable(c.getAnnotation(DefaultPrice.class)).map(DefaultPrice::value);

			final TypeName specificSubListType = ParameterizedTypeName.get(ClassName.get(List.class),
					ParameterizedTypeName.get(ClassName.get(InternalConstructorFunction.class), ClassName.get(GameObject.class)));

			staticCodeBlock.add("/* $L $T $L */\n", spacer, TypeName.get(c), spacer);
			staticCodeBlock.addStatement("final $T $L = new $T<>()", specificSubListType, listName, ArrayList.class);

			for (final Constructor<?> con : Arrays.stream(c.getConstructors())
					.sorted(Comparator.comparing(Constructor<?>::getParameterCount))
					.toList()) {

				staticCodeBlock.addStatement("$L.add(new $T<>(new $T {"
						+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "$T.class").collect(Collectors.joining(", "))
						+ "}, ($T[] arr) -> ($T) new $T("
						+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "($T) $L").collect(Collectors.joining(", ")) + ")))",
						PCUtils.concatStreams(Stream.of(listName, InternalConstructorFunction.class, ArrayTypeName.of(Class.class)),
								Arrays.stream(con.getParameterTypes()).map(TypeName::get),
								Stream.of(Object.class, GameObject.class, c),
								IntStream.range(0, con.getParameterCount())
										.mapToObj(i -> Pairs.readOnly(TypeName.get(con.getParameters()[i].getType()), "arr[" + i + "]"))
										.flatMap(z -> Stream.of(z.getKey(), z.getValue())))
								.toArray());

			}

			staticCodeBlock.addStatement("$N.put($T.class, $L)", constructorHashMap, c, listName);
			bufferSize.ifPresent(i -> staticCodeBlock.addStatement("$N.put($T.class, $L)", bufferSizeHashMap, c, i));
			dataPath.ifPresent(t -> staticCodeBlock.addStatement("$N.put($T.class, $S)", dataPathHashMap, c, t));
			textureOption.ifPresent(t -> {
				staticCodeBlock.addStatement("$N.put($T.class, $L)", textureFilterHashMap, c, t.textureFilter());
				staticCodeBlock.addStatement("$N.put($T.class, $L)", textureWrapHashMap, c, t.textureWrap());
			});
			price.ifPresent(p -> staticCodeBlock.addStatement("$N.put($T.class, $L)", defaultPriceHashMap, c, p));
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
