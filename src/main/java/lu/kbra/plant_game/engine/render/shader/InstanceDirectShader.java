package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class InstanceDirectShader extends DirectShader {

	public InstanceDirectShader() {
		super(AbstractShaderPart.load("classpath:/shaders/gbuffer_inst.vert"), AbstractShaderPart.load("classpath:/shaders/direct.frag"));
	}

}
