package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class TextureMaterialComputeShader extends ComputeShader {

	public static final String TXT0 = "txt0";
	public static final String CURRENT_MATERIAL_ID = "currentMaterialId";
	public static final String LIGHT_DIR = "lightDir";
	public static final String LIGHT_COLOR = "lightColor";
	public static final String AMBIENT_LIGHT = "ambientLight";

	public TextureMaterialComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/texture_material.comp"));
	}

	@Override
	public void createUniforms() {
		createUniform(INPUT_SIZE);
		createUniform(OUTPUT_SIZE);

		createUniform(TXT0);
		createUniform(CURRENT_MATERIAL_ID);
		createUniform(LIGHT_DIR);
		createUniform(LIGHT_COLOR);
		createUniform(AMBIENT_LIGHT);
	}

}
