package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class MaterialComputeShader extends ComputeShader {

	public static final String INPUT_SIZE = "inputSize";
	public static final String OUTPUT_SIZE = "outputSize";

	public MaterialComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/material.comp"));
	}

}
