package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class MaterialComputeShader extends ComputeShader {

	public static final String INPUT_SIZE = "inputSize";
	public static final String OUTPUT_SIZE = "outputSize";
	
	public static final String LIGHT_DIR = "lightDir";
	public static final String LIGHT_COLOR = "lightColor";
	public static final String AMBIENT_LIGHT = "ambientLight";

	public MaterialComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/material.comp"));
	}

	@Override
	public void createUniforms() {
		createUniform(INPUT_SIZE);
		createUniform(OUTPUT_SIZE);

		createUniform(MaterialComputeShader.LIGHT_DIR);
		createUniform(MaterialComputeShader.LIGHT_COLOR);
		createUniform(MaterialComputeShader.AMBIENT_LIGHT);
	}

}
