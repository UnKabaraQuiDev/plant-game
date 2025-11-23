package lu.kbra.plant_game.engine.render.shader;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class SwayInstanceTransferShader extends SwayTransferShader {

	public SwayInstanceTransferShader() {
		super(true,
				AbstractShaderPart.load("classpath:/shaders/gbuffer_sway_inst.vert"),
				AbstractShaderPart.load("classpath:/shaders/gbuffer.frag"));
	}

}
