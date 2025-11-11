package lu.kbra.plant_game;

import java.io.IOException;
import java.lang.reflect.Constructor;
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
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;

public class GameObjectRegistryGenMain extends GenMainConsts {

	@Test
	public void genRegistry() throws IOException {
		final TypeName functionType = ParameterizedTypeName
				.get(ClassName.get(InternalConstructorFunction.class), ClassName.get(GameObject.class));

		// Map<ClassListKey, Function<Object[], GameObject>>
		final TypeName subListType = ParameterizedTypeName.get(ClassName.get(List.class), functionType);

		// Class<? extends GameObject>
		final TypeName gameObjectClassType = ParameterizedTypeName
				.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(GameObject.class));

		// Map<Class<? extends GameObject>, Map<ClassListKey, Function<Object[], GameObject>>>
		final FieldSpec hashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), gameObjectClassType, subListType),
						PCUtils.camelCaseToConstant("gameObjectConstructors"),
						Modifier.PRIVATE,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final TypeSpec.Builder registry = TypeSpec.classBuilder("GameObjectRegistry").addModifiers(Modifier.PUBLIC).addField(hashMap);

		System.out.println(JavaFile.builder(GEN_PACKAGE, registry.build()).indent("\t").build());

		final Reflections reflections = new Reflections(MAIN_PACKAGE);
		final Set<Class<? extends GameObject>> classes = reflections.getSubTypesOf(GameObject.class);

		final CodeBlock.Builder staticCodeBlock = CodeBlock.builder();

		final String spacer = PCUtils.repeatString(" ", 15);

		staticCodeBlock.addStatement("$N = new $T<>()", hashMap, HashMap.class);
		staticCodeBlock.add("\n");

		for (Class<? extends GameObject> c : classes) {
			if (!c.isAnnotationPresent(DataPath.class)) {
				continue;
			}

			final String listName = "list" + c.getSimpleName();

			final TypeName specificSubListType = ParameterizedTypeName
					.get(ClassName.get(List.class),
							ParameterizedTypeName.get(ClassName.get(InternalConstructorFunction.class), ClassName.get(GameObject.class)));

			staticCodeBlock.add("/* $L $T $L */\n", spacer, TypeName.get(c), spacer);
			staticCodeBlock.addStatement("final $T $L = new $T<>()", specificSubListType, listName, ArrayList.class);

			for (Constructor<?> con : Arrays
					.stream(c.getConstructors())
					.sorted((c1, c2) -> Integer.compare(c1.getParameterCount(), c2.getParameterCount()))
					.toList()) {

				staticCodeBlock
						.addStatement("$L.add(new $T<>(new $T {"
								+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "$T.class").collect(Collectors.joining(", "))
								+ "}, ($T[] arr) -> ($T) new $T("
								+ IntStream.range(0, con.getParameterCount()).mapToObj(i -> "($T) $L").collect(Collectors.joining(", "))
								+ ")))",
								PCUtils
										.concatStreams(
												Stream.of(listName, InternalConstructorFunction.class, ArrayTypeName.of(Class.class)),
												Arrays.stream(con.getParameterTypes()).map(TypeName::get),
												Stream.of(Object.class, GameObject.class, c),
												IntStream
														.range(0, con.getParameterCount())
														.mapToObj(i -> Pairs
																.readOnly(TypeName.get(con.getParameters()[i].getType()), "arr[" + i + "]"))
														.flatMap(z -> Stream.of(z.getKey(), z.getValue())))
										.toArray());

			}

			staticCodeBlock.addStatement("$N.put($T.class, $L)", hashMap, c, listName);
			staticCodeBlock.add("\n");
		}

		registry.addStaticBlock(staticCodeBlock.build());

		final ClassName exceptionType = ClassName.get(GameObjectNotFound.class);
		final ClassName exceptionConstructorType = ClassName.get(GameObjectConstructorNotFound.class);
		final TypeVariableName gameObjectTypeVar = TypeVariableName.get("T", ClassName.get(GameObject.class));

		registry
				.addMethod(MethodSpec
						.methodBuilder("create")
						.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
						.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), gameObjectTypeVar), "clazz", Modifier.FINAL)
						.addParameter(ArrayTypeName.of(ClassName.get(Object.class)), "args", Modifier.FINAL)
						.varargs()
						.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build())
						.returns(gameObjectTypeVar)
						.addTypeVariable(gameObjectTypeVar)
						.addStatement("return ($T) get(clazz, args).apply(args)", gameObjectTypeVar)
						.build());

		registry
				.addMethod(MethodSpec
						.methodBuilder("get")
						.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
						.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), gameObjectTypeVar), "clazz", Modifier.FINAL)
						.addParameter(ArrayTypeName.of(ClassName.get(Object.class)), "args", Modifier.FINAL)
						.varargs()
						.returns(functionType)
						.addTypeVariable(gameObjectTypeVar)
						.beginControlFlow("if ($N.containsKey(clazz))", hashMap)
						.addStatement(
								"final $T<$T> bestConstructor = $N.get(clazz).parallelStream().filter((v) -> v.matches(args)).findFirst()",
								Optional.class,
								functionType,
								hashMap)
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
