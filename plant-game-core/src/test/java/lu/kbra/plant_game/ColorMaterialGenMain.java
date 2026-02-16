package lu.kbra.plant_game;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Modifier;

import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.junit.Test;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import lu.kbra.plant_game.generated.ColorMaterial;

public class ColorMaterialGenMain extends GenMainConsts {

	public static class XEntry implements Entry<String, Color> {

		String key;
		Color value;

		public XEntry(final String key, final Color value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String getKey() {
			return this.key;
		}

		@Override
		public Color getValue() {
			return this.value;
		}

		@Override
		public Color setValue(final Color value) {
			final Color oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		@Override
		public String toString() {
			return "XEntry@" + System.identityHashCode(this) + " [key=" + this.key + ", value=" + this.value + "]";
		}

	}

	public static final Map<String, Color> COLORS;
	public static final Map<String, Color> COLORS_WITH_SHADES;

	static {
		final Map<String, Color> map = new HashMap<>();

//		map.put("BLACK", Color.BLACK);
		map.put("BLUE", Color.BLUE);
		map.put("CYAN", Color.CYAN);
//		map.put("DARK_GRAY", Color.DARK_GRAY);
		map.put("GRAY", Color.GRAY);
		map.put("GREEN", Color.GREEN);
//		map.put("LIGHT_GRAY", Color.LIGHT_GRAY);
		map.put("MAGENTA", Color.MAGENTA);
		map.put("ORANGE", Color.ORANGE);
		map.put("PINK", Color.PINK);
		map.put("RED", Color.RED);
//		map.put("WHITE", Color.WHITE);
		map.put("YELLOW", Color.YELLOW);
		map.put("BROWN", new Color(137, 81, 41));

		// Sort by hue
		List<Map.Entry<String, Color>> sorted = new ArrayList<>(map.entrySet());
		sorted.sort((a, b) -> {
			float[] hsbA = Color.RGBtoHSB(a.getValue().getRed(), a.getValue().getGreen(), a.getValue().getBlue(), null);
			float[] hsbB = Color.RGBtoHSB(b.getValue().getRed(), b.getValue().getGreen(), b.getValue().getBlue(), null);
			return Float.compare(hsbA[0], hsbB[0]); // compare Hue
		});
		sorted.add(0, new XEntry("WHITE", Color.WHITE));
		sorted.add(1, new XEntry("BLACK", Color.BLACK));

		// Preserve order in LinkedHashMap
		final Map<String, Color> ordered = new LinkedHashMap<>();
		for (Map.Entry<String, Color> e : sorted) {
			ordered.put(e.getKey(), e.getValue());
		}

		COLORS = Collections.unmodifiableMap(ordered);

		// Add shades
		final Map<String, Color> shadeMap = new LinkedHashMap<>(COLORS);
		for (Map.Entry<String, Color> entry : COLORS.entrySet()) {
			String name = entry.getKey();
			Color color = entry.getValue();

			shadeMap.put("LIGHT_" + name, color.brighter());
		}
		for (Map.Entry<String, Color> entry : COLORS.entrySet()) {
			String name = entry.getKey();
			Color color = entry.getValue();

			shadeMap.put("DARK_" + name, color.darker());
		}

		COLORS_WITH_SHADES = Collections.unmodifiableMap(shadeMap);
	}

	@Test
	public void main() throws IOException, IllegalArgumentException, IllegalAccessException {
		COLORS_WITH_SHADES.entrySet().forEach(System.err::println);

		final TypeSpec enumType = this.buildColorEnum("ColorMaterial");
		JavaFile.builder(GEN_PACKAGE, enumType).addFileComment("@formatter:off").indent("\t").build().writeTo(SRC_MAIN_JAVA_DIR);
	}

	private TypeSpec buildColorEnum(final String name) {
		final TypeSpec.Builder builder = TypeSpec.enumBuilder(name).addModifiers(Modifier.PUBLIC);

		builder.addField(FieldSpec.builder(TypeName.INT, "BASE_COLOR_COUNT", Modifier.FINAL, Modifier.STATIC)
				.initializer("$L", COLORS.size())
				.build());

		final FieldSpec colorsById = FieldSpec
				.builder(
						ParameterizedTypeName
								.get(ClassName.get(Map.class), ClassName.get(Integer.class), ClassName.get(ColorMaterial.class)),
						"COLORS_BY_ID",
						Modifier.PRIVATE,
						Modifier.STATIC,
						Modifier.FINAL)
				.initializer("new $T<>()", HashMap.class)
				.build();
		builder.addField(colorsById);

		final CodeBlock staticBlock = CodeBlock.builder()
				.beginControlFlow("for ($T cm : $T.values())", ColorMaterial.class, ColorMaterial.class)
				.addStatement("COLORS_BY_ID.put((int) cm.getId(), cm)")
				.endControlFlow()
				.build();
		builder.addStaticBlock(staticBlock);

		final MethodSpec byId = MethodSpec.methodBuilder("byId")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(ColorMaterial.class)
				.addParameter(int.class, "id")
				.addStatement("return COLORS_BY_ID.get(id)")
				.build();
		builder.addMethod(byId);

		final MethodSpec next = MethodSpec.methodBuilder("next")
				.addModifiers(Modifier.PUBLIC)
				.returns(ColorMaterial.class)
				.addStatement("int nextId = getId() % $T.values().length + 1", ColorMaterial.class)
				.addStatement("return byId(nextId)")
				.build();
		builder.addMethod(next);

		final MethodSpec previous = MethodSpec.methodBuilder("previous")
				.addModifiers(Modifier.PUBLIC)
				.returns(ColorMaterial.class)
				.addStatement("int prevId = (getId() - 2 + $T.values().length) % $T.values().length + 1",
						ColorMaterial.class,
						ColorMaterial.class)
				.addStatement("return byId(prevId)")
				.build();
		builder.addMethod(previous);

		// store the RGBA as fields
		builder.addField(float.class, "r", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(float.class, "g", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(float.class, "b", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(float.class, "a", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(boolean.class, "light", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(boolean.class, "dark", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(Vector4fc.class, "color", Modifier.PRIVATE, Modifier.FINAL);

		// constructor
		final MethodSpec ctor = MethodSpec.constructorBuilder()
				.addParameter(float.class, "r")
				.addParameter(float.class, "g")
				.addParameter(float.class, "b")
				.addParameter(float.class, "a")
				.addParameter(boolean.class, "l")
				.addParameter(boolean.class, "d")
				.addStatement("this.r = r")
				.addStatement("this.g = g")
				.addStatement("this.b = b")
				.addStatement("this.a = a")
				.addStatement("this.light = l")
				.addStatement("this.dark = d")
				.addStatement("this.color = new $T(r, g, b, a)", Vector4f.class)
				.addModifiers(Modifier.PRIVATE)
				.build();

		builder.addMethod(ctor);

		// constants
		for (final Entry<String, Color> e : COLORS_WITH_SHADES.entrySet()) {
			final String constantName = e.getKey();
			final Color c = e.getValue();

			builder.addEnumConstant(constantName,
					TypeSpec.anonymousClassBuilder("$Lf, $Lf, $Lf, 1f, $L, $L",
							c.getRed() / 255f,
							c.getGreen() / 255f,
							c.getBlue() / 255f,
							constantName.contains("LIGHT_"),
							constantName.contains("DARK_")).build());
		}

		// darker()
		final MethodSpec darker = MethodSpec.methodBuilder("darker").addModifiers(Modifier.PUBLIC).returns(ClassName.bestGuess(name))
//				.addStatement("float dr = Math.max(0f, r * 0.7f)")
//				.addStatement("float dg = Math.max(0f, g * 0.7f)")
//				.addStatement("float db = Math.max(0f, b * 0.7f)")
//				.addStatement("final $T c = $T.valueOf(dr, dg, db, a)", ClassName.bestGuess(name), ClassName.bestGuess(name))
//				.addStatement("return c != null ? c : $T.BLACK", ClassName.bestGuess(name))
				.addStatement(
						"return dark ? ColorMaterial.BLACK : light ? byId(getId() - BASE_COLOR_COUNT) : byId(getId() + 2 * BASE_COLOR_COUNT)")
				.build();

		// lighter()
		final MethodSpec lighter = MethodSpec.methodBuilder("lighter").addModifiers(Modifier.PUBLIC).returns(ClassName.bestGuess(name))
//				.addStatement("float lr = Math.min(1f, r * 1.3f)")
//				.addStatement("float lg = Math.min(1f, g * 1.3f)")
//				.addStatement("float lb = Math.min(1f, b * 1.3f)")
//				.addStatement("final $T c = $T.valueOf(lr, lg, lb, a)", ClassName.bestGuess(name), ClassName.bestGuess(name))
//				.addStatement("return c != null ? c : $T.WHITE", ClassName.bestGuess(name))
				.addStatement(
						"return light ? ColorMaterial.WHITE : dark ? byId(getId() - 2 * BASE_COLOR_COUNT) : byId(getId() + BASE_COLOR_COUNT)")

				.build();

		final MethodSpec getId = MethodSpec.methodBuilder("getId")
				.addModifiers(Modifier.PUBLIC)
				.returns(TypeName.SHORT)
				.addStatement("return (short) (ordinal() + 1)")
				.build();

		final MethodSpec getColor = MethodSpec.methodBuilder("getColor")
				.addModifiers(Modifier.PUBLIC)
				.returns(TypeName.get(Vector4fc.class))
				.addStatement("return this.color")
				.build();

		final MethodSpec getClosest = MethodSpec.methodBuilder("getClosest")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(ClassName.bestGuess("ColorMaterial"))
				.addParameter(ParameterSpec.builder(ClassName.get("org.joml", "Vector4fc"), "c", Modifier.FINAL).build())
				.addStatement("$T closest = null", ClassName.bestGuess("ColorMaterial"))
				.addStatement("float bestDistance = $T.MAX_VALUE", Float.class)
				.beginControlFlow("for ($T material : values())", ClassName.bestGuess("ColorMaterial"))
				.addStatement("$T mc = material.color", ClassName.get("org.joml", "Vector4fc"))
				.addStatement("float distance = mc.distance(c)")
				.beginControlFlow("if (distance < bestDistance)")
				.addStatement("bestDistance = distance")
				.addStatement("closest = material")
				.endControlFlow()
				.endControlFlow()
				.addStatement("return closest")
				.build();
		builder.addMethod(this.createValueOf(name));
		builder.addMethod(darker);
		builder.addMethod(lighter);
		builder.addMethod(getId);
		builder.addMethod(getColor);
		builder.addMethod(getClosest);

		return builder.build();
	}

	private MethodSpec createValueOf(final String enumName) {
		return MethodSpec.methodBuilder("valueOf")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(ClassName.bestGuess(enumName))
				.addParameter(float.class, "r")
				.addParameter(float.class, "g")
				.addParameter(float.class, "b")
				.addParameter(float.class, "a")
				.beginControlFlow("for ($T m : values())", ClassName.bestGuess(enumName))
				.beginControlFlow("if (m.r == r && m.g == g && m.b == b && m.a == a)")
				.addStatement("return m")
				.endControlFlow()
				.endControlFlow()
				.addStatement("return null")
				.build();
	}

}
