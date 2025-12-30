package lu.kbra.plant_game.engine.render.shader;

import java.util.Map;

import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.joml.Vector4fc;

import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class MaterialComputeShader extends ComputeShader {

	public static final Vector3ic LOCAL_SIZE = new Vector3i(16, 16, 1);

	public static final String NUM_COLORS = "%NUM_COLORS%";
	public static final String COLORS = "%COLORS%";

	public static final String LIGHT_DIR = "lightDir";
	public static final String LIGHT_COLOR = "lightColor";
	public static final String AMBIENT_LIGHT = "ambientLight";

	public MaterialComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/material.comp", getBuildingDeps()), LOCAL_SIZE);
	}

	protected static Map<String, Object> getBuildingDeps() {
		final Map<String, Object> objs = getBaseBuildingDeps(LOCAL_SIZE);
		objs.put(NUM_COLORS, Integer.toString(ColorMaterial.values().length));
		objs.put(COLORS, generateGlslArray(ColorMaterial.values()));
		return objs;
	}

	public MaterialComputeShader(final ComputeShaderPart part) {
		super(part, LOCAL_SIZE);
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		this.createUniform(LIGHT_DIR);
		this.createUniform(LIGHT_COLOR);
		this.createUniform(AMBIENT_LIGHT);
	}

	private static String generateGlslArray(final ColorMaterial[] colors) {
		final StringBuilder sb = new StringBuilder();

		sb.append("vec4[](\n");

		for (int i = 0; i < colors.length; i++) {
			final ColorMaterial mat = colors[i];
			final Vector4fc c = mat.getColor();
			sb
					.append("\tvec4(")
					.append(c.x())
					.append("f, ")
					.append(c.y())
					.append("f, ")
					.append(c.z())
					.append("f, ")
					.append(c.w())
					.append("f)");
			if (i < colors.length - 1) {
				sb.append(",");
			}
			sb.append(" // ").append(mat.name()).append("\n");
		}

		sb.append(");");
		return sb.toString();
	}

}
