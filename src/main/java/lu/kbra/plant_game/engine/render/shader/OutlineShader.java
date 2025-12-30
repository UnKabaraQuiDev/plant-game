package lu.kbra.plant_game.engine.render.shader;

import java.util.Map;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class OutlineShader extends ComputeShader {

	public static final Vector3ic LOCAL_SIZE = new Vector3i(16, 16, 1);

	public static final String MAX_COUNT = "%MAX_COUNT%";

	public static final int MAX_OUTLINE_COUNT = 64;
	public static final String CURRENT_TARGET_COUNT = "currentTargetCount";
	public static final String TARGET_IDS = "targetIds";
	public static final String TARGET_COLORS = "targetColors";

	public OutlineShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/outline.comp", getBuildingDeps(LOCAL_SIZE)), LOCAL_SIZE);
	}

	protected static Map<String, Object> getBuildingDeps(final Vector3ic localSize) {
		final Map<String, Object> objs = getBaseBuildingDeps(LOCAL_SIZE);
		objs.put(MAX_COUNT, MAX_OUTLINE_COUNT);
		return objs;
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		super.createUniform(CURRENT_TARGET_COUNT);
		super.createUniform(TARGET_IDS);
		super.createUniform(TARGET_COLORS);
	}

}