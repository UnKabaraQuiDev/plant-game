package lu.kbra.plant_game.engine.render.shader.render;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class LineInstanceDirectShader extends DirectShader {

	public LineInstanceDirectShader() {
		super(AbstractShaderPart.load("classpath:/shaders/gbuffer_inst.vert"), AbstractShaderPart.load("classpath:/shaders/line.frag"));
	}

}
