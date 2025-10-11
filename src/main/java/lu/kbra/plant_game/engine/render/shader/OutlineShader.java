package lu.kbra.plant_game.engine.render.shader;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class OutlineShader extends ComputeShader {

	public static final int MAX_COUNT = 64;
	public static final String CURRENT_TARGET_COUNT = "currentTargetCount";
	public static final String TARGET_IDS = "targetIds";
	public static final String TARGET_COLORS = "targetColors";

	public OutlineShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/outline.comp",
				PCUtils.hashMap("{MAX_COUNT}", MAX_COUNT)));
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		super.createUniform(CURRENT_TARGET_COUNT);
		super.createUniform(TARGET_IDS);
		super.createUniform(TARGET_COLORS);
	}

}