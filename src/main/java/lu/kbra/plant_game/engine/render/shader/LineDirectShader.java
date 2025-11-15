package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class LineDirectShader extends DirectShader {

	public LineDirectShader() {
		super(AbstractShaderPart.load("classpath:/shaders/gbuffer.vert"), AbstractShaderPart.load("classpath:/shaders/line.frag"));
	}

}
