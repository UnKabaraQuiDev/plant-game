package lu.kbra.plant_game.engine.render.shader;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class GradientShader extends DirectShader {

	public static final String GRADIENT_DIRECTION = "gradientDirection";
	public static final String GRADIENT_RANGE = "gradientRange";
	public static final String START_COLOR = "startColor";
	public static final String END_COLOR = "endColor";

	public static final GradientDirection DEFAULT_DIRECTION = GradientDirection.UV_X;
	public static final Vector2f DEFAULT_RANGE = new Vector2f(0, 1);
	public static final Vector4f DEFAULT_START_COLOR = new Vector4f(1);
	public static final Vector4f DEFAULT_END_COLOR = new Vector4f(0);

	public GradientShader() {
		super(AbstractShaderPart.load("classpath:/shaders/gbuffer.vert"),
				AbstractShaderPart.load("classpath:/shaders/direct_gradient.frag"));
	}

	public GradientShader(AbstractShaderPart... parts) {
		super(parts);
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		super.createUniform(GRADIENT_DIRECTION);
		super.createUniform(GRADIENT_RANGE);
		super.createUniform(START_COLOR);
		super.createUniform(END_COLOR);
	}

}
