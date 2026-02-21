package lu.kbra.plant_game.engine.render.shader.compute;

import java.util.Map;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class TextureMaterialComputeShader extends MaterialComputeShader {

	public static final String TXT_DIFFUSE = "txtDiffuse";
	public static final String TXT_BLOOM = "txtBloom";
	public static final String CURRENT_MATERIAL_ID = "currentMaterialId";
	public static final String BLOOM_STRENGTH = "bloomStrength";
	public static final String APPLY_BLOOM = "applyBloom";

	public TextureMaterialComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/texture_material.comp", getBuildingDeps()));
	}

	protected static Map<String, Object> getBuildingDeps() {
		final Map<String, Object> objs = getBaseBuildingDeps(LOCAL_SIZE);
		return objs;
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		this.createUniform(TXT_DIFFUSE);
		this.createUniform(TXT_BLOOM);
		this.createUniform(CURRENT_MATERIAL_ID);
		this.createUniform(BLOOM_STRENGTH);
		this.createUniform(APPLY_BLOOM);
	}

}
