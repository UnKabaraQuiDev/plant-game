package lu.kbra.plant_game.engine.render.shader.compute.filter;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class VignetteShader extends FilterShader {

	public static final String VIGNETTE_COLOR = "vignetteColor";
	public static final String VIGNETTE_RADIUS = "vignetteRadius";
	public static final String VIGNETTE_SOFTNESS = "vignetteSoftness";
	public static final String VIGNETTE_STRENGTH = "vignetteStrength";
	public static final String VIGNETTE_CENTER = "vignetteCenter";
	public static final String FOLLOW_ASPECT_RATIO = "followAspectRatio";

	public static final String EDGE_STYLE = "edgeStyle";

	public static final String POSTERIZE = "posterize";
	public static final String POSTERIZE_LEVELS = "posterizeLevels";

	public VignetteShader() {
		super(AbstractShaderPart.load("classpath:/shaders/filter.vert"),
				AbstractShaderPart.load("classpath:/shaders/filter_vignette.frag"));
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		super.createUniform(VIGNETTE_COLOR);
		super.createUniform(VIGNETTE_RADIUS);
		super.createUniform(VIGNETTE_SOFTNESS);
		super.createUniform(VIGNETTE_STRENGTH);
		super.createUniform(VIGNETTE_CENTER);
		super.createUniform(FOLLOW_ASPECT_RATIO);
		super.createUniform(EDGE_STYLE);
		super.createUniform(POSTERIZE);
		super.createUniform(POSTERIZE_LEVELS);
	}

}
