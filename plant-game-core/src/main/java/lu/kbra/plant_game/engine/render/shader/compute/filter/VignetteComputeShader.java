package lu.kbra.plant_game.engine.render.shader.compute.filter;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

@Deprecated
public class VignetteComputeShader extends ComputeShader {

	@Deprecated
	protected static final Vector3ic LOCAL_SIZE = new Vector3i(16, 16, 16);

	@Deprecated
	public static final String VIGNETTE_COLOR = "vignetteColor";
	@Deprecated
	public static final String VIGNETTE_RADIUS = "vignetteRadius";
	@Deprecated
	public static final String VIGNETTE_SOFTNESS = "vignetteSoftness";

	@Deprecated
	public VignetteComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/filter_vignette.comp", getBaseBuildingDeps(LOCAL_SIZE)),
				LOCAL_SIZE);
	}

	@Deprecated
	@Override
	public void createUniforms() {
		super.createUniforms();

		this.createUniform(VIGNETTE_COLOR);
		this.createUniform(VIGNETTE_RADIUS);
		this.createUniform(VIGNETTE_SOFTNESS);
	}

}
