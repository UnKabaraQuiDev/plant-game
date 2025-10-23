package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class DirectShader extends RenderShader {

	public static final String TXT0 = "txt0";

	public DirectShader() {
		super(AbstractShaderPart.load("classpath:/shaders/gbuffer.vert"),
				AbstractShaderPart.load("classpath:/shaders/direct.frag"));
	}

	public DirectShader(AbstractShaderPart... parts) {
		super(parts);
	}

	@Override
	public void createUniforms() {
		super.createSceneUniforms();

		super.createUniform(TXT0);
	}

}
