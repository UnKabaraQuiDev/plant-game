package lu.kbra.plant_game;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
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

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.datastructure.pair.Pairs;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.util.annotation.TextureOption;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectNotFound;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public class UIObjectRegistryGenMain extends GenMainConsts {

	@Test
	public void genRegistry() throws IOException {
		final TypeName functionType = ParameterizedTypeName.get(ClassName.get(InternalConstructorFunction.class),
				ClassName.get(UIObject.class));

		// Map<ClassListKey, Function<Object[], UIObject>>
		final TypeName subListType = ParameterizedTypeName.get(ClassName.get(List.class), functionType);

		// Class<? extends UIObject>
		final TypeName uiObjectClassType = ParameterizedTypeName.get(ClassName.get(Class.class),
				WildcardTypeName.subtypeOf(UIObject.class));

		// Map<Class<? extends UIObject>, Map<ClassListKey, Function<Object[], UIObject>>>
		final FieldSpec constructorHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), uiObjectClassType, subListType),
						PCUtils.camelCaseToConstant("uiObjectConstructors"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec dataPathHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), uiObjectClassType, ClassName.get(String.class)),
						PCUtils.camelCaseToConstant("dataPath"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec bufferSizeHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), uiObjectClassType, ClassName.get(Integer.class)),
						PCUtils.camelCaseToConstant("bufferSize"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec textureFilterHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), uiObjectClassType, ClassName.get(TextureFilter.class)),
						PCUtils.camelCaseToConstant("textureFilter"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final FieldSpec textureWrapHashMap = FieldSpec
				.builder(ParameterizedTypeName.get(ClassName.get(Map.class), uiObjectClassType, ClassName.get(TextureWrap.class)),
						PCUtils.camelCaseToConstant("textureWrap"),
						Modifier.PUBLIC,
						Modifier.STATIC,
						Modifier.FINAL)
				.build();

		final TypeSpec.Builder registry = TypeSpec.classBuilder("UIObjectRegistry")
				.addModifiers(Modifier.PUBLIC)
				.addField(constructorHashMap)
				.addField(dataPathHashMap)
				.addField(bufferSizeHashMap)
				.addField(textureFilterHashMap)
				.addField(textureWrapHashMap);

		final Reflections reflections = new Reflections(MAIN_PACKAGE);
		final Set<Class<? extends UIObject>> classes = reflections.getSubTypesOf(UIObject.class);

		final CodeBlock.Builder staticCodeBlock = CodeBlock.builder();

		final String spacer = PCUtils.repeatString(" ", 15);

		staticCodeBlock.addStatement("$N = new $T<>()", constructorHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", dataPathHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", bufferSizeHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", textureFilterHashMap, HashMap.class);
		staticCodeBlock.addStatement("$N = new $T<>()", textureWrapHashMap, HashMap.class);
		staticCodeBlock.add("\n");

		for (final Class<? extends UIObject> c : classes) {
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

			final TypeName specificSubListType = ParameterizedTypeName.get(ClassName.get(List.class),
					ParameterizedTypeName.get(ClassName.get(InternalConstructorFunction.class), ClassName.get(UIObject.class)));

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
								Stream.of(Object.class, UIObject.class, c),
								IntStream.range(0, con.getParameterCount())
										.mapToObj(i -> Pairs.readOnly(TypeName.get(con.getParameters()[i].getType()), "arr[" + i + "]"))
										.flatMap(z -> Stream.of(z.getKey(), z.getValue())))
								.toArray());

			}

			staticCodeBlock.addStatement("$N.put($T.class, $L)", constructorHashMap, c, listName);
			bufferSize.ifPresent(i -> staticCodeBlock.addStatement("$N.put($T.class, $L)", bufferSizeHashMap, c, i));
			dataPath.ifPresent(t -> staticCodeBlock.addStatement("$N.put($T.class, $S)", dataPathHashMap, c, t));
			textureOption.ifPresent(t -> {
				staticCodeBlock.addStatement("$N.put($T.class, $T.$L)", textureFilterHashMap, c, TextureFilter.class, t.textureFilter());
				staticCodeBlock.addStatement("$N.put($T.class, $T.$L)", textureWrapHashMap, c, TextureWrap.class, t.textureWrap());
			});
			staticCodeBlock.add("\n");
		}

		registry.addStaticBlock(staticCodeBlock.build());

		final ClassName exceptionType = ClassName.get(UIObjectNotFound.class);
		final ClassName exceptionConstructorType = ClassName.get(UIObjectConstructorNotFound.class);
		final TypeVariableName gameObjectTypeVar = TypeVariableName.get("T", ClassName.get(UIObject.class));

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
