package lu.kbra.plant_game.engine.shader;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class MaskComputeShader extends ComputeShader {

	public MaskComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/mask_objects.comp"));
	}

}
