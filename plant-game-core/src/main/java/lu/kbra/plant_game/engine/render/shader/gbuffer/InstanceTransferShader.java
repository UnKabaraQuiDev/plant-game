package lu.kbra.plant_game.engine.render.shader.gbuffer;

import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonMode;

public class InstanceTransferShader extends TransferShader {

	public InstanceTransferShader() {
		super(true,
				AbstractShaderPart.load("classpath:/shaders/gbuffer_inst.vert"),
				AbstractShaderPart.load("classpath:/shaders/gbuffer.frag"));
		this.setFaceMode(PolygonMode.FRONT_AND_BACK);
	}

	@Override
	public void createUniforms() {
		super.createUniforms();
	}

}