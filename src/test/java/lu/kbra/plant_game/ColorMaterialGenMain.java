package lu.kbra.plant_game;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Modifier;

import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.junit.Test;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class ColorMaterialGenMain extends GenMainConsts {

	public static final Map<String, Color> COLORS;
	public static final Map<String, Color> COLORS_WITH_SHADES;

	static {
		final Map<String, Color> map = new HashMap<>();

		map.put("BLACK", Color.BLACK);
		map.put("BLUE", Color.BLUE);
		map.put("CYAN", Color.CYAN);
		map.put("DARK_GRAY", Color.DARK_GRAY);
		map.put("GRAY", Color.GRAY);
		map.put("GREEN", Color.GREEN);
		map.put("LIGHT_GRAY", Color.LIGHT_GRAY);
		map.put("MAGENTA", Color.MAGENTA);
		map.put("ORANGE", Color.ORANGE);
		map.put("PINK", Color.PINK);
		map.put("RED", Color.RED);
		map.put("WHITE", Color.WHITE);
		map.put("YELLOW", Color.YELLOW);

		map.put("BROWN", new Color(137, 81, 41));

		COLORS = Collections.unmodifiableMap(map);

		final Map<String, Color> shadeMap = new HashMap<>(COLORS);
		for (final Map.Entry<String, Color> entry : map.entrySet()) {
			final String name = entry.getKey();
			final Color color = entry.getValue();

			// Lighter variant
			final Color lighter = color.brighter();
			shadeMap.put("LIGHT_" + name, lighter);

			// Darker variant
			final Color darker = color.darker();
			shadeMap.put("DARK_" + name, darker);
		}
		COLORS_WITH_SHADES = Collections.unmodifiableMap(shadeMap);
	}

	@Test
	public void main() throws IOException, IllegalArgumentException, IllegalAccessException {
		COLORS_WITH_SHADES.entrySet().forEach(System.err::println);

		final TypeSpec enumType = this.buildColorEnum("ColorMaterial", COLORS_WITH_SHADES);
		JavaFile.builder(GEN_PACKAGE, enumType).addFileComment("@formatter:off").indent("\t").build().writeTo(SRC_MAIN_JAVA_DIR);
	}

	private TypeSpec buildColorEnum(final String name, final Map<String, Color> colors) {
		final TypeSpec.Builder builder = TypeSpec.enumBuilder(name).addModifiers(Modifier.PUBLIC);

		// store the RGBA as fields
		builder.addField(float.class, "r", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(float.class, "g", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(float.class, "b", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(float.class, "a", Modifier.PRIVATE, Modifier.FINAL);
		builder.addField(Vector4fc.class, "color", Modifier.PRIVATE, Modifier.FINAL);

		// constructor
		final MethodSpec ctor = MethodSpec
				.constructorBuilder()
				.addParameter(float.class, "r")
				.addParameter(float.class, "g")
				.addParameter(float.class, "b")
				.addParameter(float.class, "a")
				.addStatement("this.r = r")
				.addStatement("this.g = g")
				.addStatement("this.b = b")
				.addStatement("this.a = a")
				.addStatement("this.color = new $T(r, g, b, a)", Vector4f.class)
				.addModifiers(Modifier.PRIVATE)
				.build();

		builder.addMethod(ctor);

		// constants
		for (final Entry<String, Color> e : colors.entrySet()) {
			final String constantName = e.getKey();
			final Color c = e.getValue();

			builder
					.addEnumConstant(constantName,
							TypeSpec
									.anonymousClassBuilder("$Lf, $Lf, $Lf, 1f", c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f)
									.build());
		}

		// darker()
		final MethodSpec darker = MethodSpec
				.methodBuilder("darker")
				.addModifiers(Modifier.PUBLIC)
				.returns(ClassName.bestGuess(name))
				.addStatement("float dr = Math.max(0f, r * 0.7f)")
				.addStatement("float dg = Math.max(0f, g * 0.7f)")
				.addStatement("float db = Math.max(0f, b * 0.7f)")
				.addStatement("final $T c = $T.valueOf(dr, dg, db, a)", ClassName.bestGuess(name), ClassName.bestGuess(name))
				.addStatement("return c != null ? c : $T.BLACK", ClassName.bestGuess(name))
				.build();

		// lighter()
		final MethodSpec lighter = MethodSpec
				.methodBuilder("lighter")
				.addModifiers(Modifier.PUBLIC)
				.returns(ClassName.bestGuess(name))
				.addStatement("float lr = Math.min(1f, r * 1.3f)")
				.addStatement("float lg = Math.min(1f, g * 1.3f)")
				.addStatement("float lb = Math.min(1f, b * 1.3f)")
				.addStatement("final $T c = $T.valueOf(lr, lg, lb, a)", ClassName.bestGuess(name), ClassName.bestGuess(name))
				.addStatement("return c != null ? c : $T.WHITE", ClassName.bestGuess(name))
				.build();

		final MethodSpec getId = MethodSpec
				.methodBuilder("getId")
				.addModifiers(Modifier.PUBLIC)
				.returns(TypeName.SHORT)
				.addStatement("return (short) (ordinal() + 1)")
				.build();

		final MethodSpec getColor = MethodSpec
				.methodBuilder("getColor")
				.addModifiers(Modifier.PUBLIC)
				.returns(TypeName.get(Vector4fc.class))
				.addStatement("return this.color")
				.build();

		builder.addMethod(this.createValueOf(name));
		builder.addMethod(darker);
		builder.addMethod(lighter);
		builder.addMethod(getId);
		builder.addMethod(getColor);

		return builder.build();
	}

	private MethodSpec createValueOf(final String enumName) {
		return MethodSpec
				.methodBuilder("valueOf")
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
