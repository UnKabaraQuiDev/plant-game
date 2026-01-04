package lu.kbra.plant_game.engine.render.shader.compute;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class TextureMaterialComputeShader extends MaterialComputeShader {

	public static final String TXT0 = "txt0";
	public static final String CURRENT_MATERIAL_ID = "currentMaterialId";

	public TextureMaterialComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/texture_material.comp"));
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		this.createUniform(TXT0);
		this.createUniform(CURRENT_MATERIAL_ID);
	}

}
