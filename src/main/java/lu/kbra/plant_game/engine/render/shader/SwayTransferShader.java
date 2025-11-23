package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class SwayTransferShader extends RenderShader {

	public static final String DEFORM_RATIO = "deformRatio";
	public static final String SPEED_RATIO = "speedRatio";
	public static final String SCALE_RATIO = "scaleRatio";
	public static final String TIME = "time";
	public static final String SWAY_MAP = "swayMap";
	public static final String SCROLL_DIRECTION = "scrollDirection";

	public static final String SWAY_MAP_TEXTURE_NAME = "_SWAY_MAP";

	public SwayTransferShader() {
		super(true,
				AbstractShaderPart.load("classpath:/shaders/gbuffer_sway.vert"),
				AbstractShaderPart.load("classpath:/shaders/gbuffer.frag"));
	}

	SwayTransferShader(final AbstractShaderPart... parts) {
		super(parts);
	}

	SwayTransferShader(final boolean transparent, final AbstractShaderPart... parts) {
		super(transparent, parts);
	}

	@Override
	public void createUniforms() {
		this.createSceneUniforms();

		this.createUniform(DEFORM_RATIO);
		this.createUniform(SPEED_RATIO);
		this.createUniform(SCALE_RATIO);
		this.createUniform(SWAY_MAP);
		this.createUniform(TIME);
		this.createUniform(SCROLL_DIRECTION);
	}

}
