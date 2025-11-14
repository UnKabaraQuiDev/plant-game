package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class BlitShader extends RenderShader {

	public static final String TXT0 = "txt0";

	public BlitShader() {
		this(null);
	}

	public BlitShader(String name) {
		super(name, AbstractShaderPart.load("classpath:/shaders/blit.frag"), AbstractShaderPart.load("classpath:/shaders/blit.vert"));
	}

	@Override
	public void createUniforms() {
		super.createUniform(TXT0);
	}

}
