package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class MaterialComputeShader extends ComputeShader {

	public static final String LIGHT_DIR = "lightDir";
	public static final String LIGHT_COLOR = "lightColor";
	public static final String AMBIENT_LIGHT = "ambientLight";

	public MaterialComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/material.comp"));
	}

	public MaterialComputeShader(ComputeShaderPart part) {
		super(part);
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		createUniform(LIGHT_DIR);
		createUniform(LIGHT_COLOR);
		createUniform(AMBIENT_LIGHT);
	}

}
