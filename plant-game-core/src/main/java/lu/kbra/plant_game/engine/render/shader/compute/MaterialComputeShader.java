package lu.kbra.plant_game.engine.render.shader.compute;

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

	public static final String VARIATION_MIN = "variationMin";
	public static final String VARIATION_MAX = "variationMax";
	public static final String VARIATION_CELLS_SIZE = "variationCellSize";
	public static final String COLOR_VARIATION = "colorVariation";
	public static final String VARIATION_MAP = "variationMap";
	public static final String VARIATION_MAP_SCALE = "variationMapScale";

	public static final String SINGLE_OBJECT = "singleObject";
	public static final String SINGLE_OBJECT_ID = "singleObjectId";

	public static final String VARIATION_MAP_TEXTURE_NAME = "_VARIATION_MAP";

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

		this.createUniform(VARIATION_MIN);
		this.createUniform(VARIATION_MAX);
		this.createUniform(VARIATION_CELLS_SIZE);
		this.createUniform(COLOR_VARIATION);
		this.createUniform(VARIATION_MAP);

		this.createUniform(SINGLE_OBJECT);
		this.createUniform(SINGLE_OBJECT_ID);
		this.createUniform(VARIATION_MAP_SCALE);
	}

	private static String generateGlslArray(final ColorMaterial[] colors) {
		final StringBuilder sb = new StringBuilder();

		sb.append("vec4[](\n");

		for (int i = 0; i < colors.length; i++) {
			final ColorMaterial mat = colors[i];
			final Vector4fc c = mat.getColor();
			sb.append("\tvec4(")
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
