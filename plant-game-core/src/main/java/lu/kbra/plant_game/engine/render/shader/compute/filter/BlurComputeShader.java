package lu.kbra.plant_game.engine.render.shader.compute.filter;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class BlurComputeShader extends ComputeShader {

	private static final Vector3ic LOCAL_SIZE = new Vector3i(16, 16, 1);

	public static final String TXT0 = "txt0";
	public static final String THRESHOLD = "threshold";
	public static final String RADIUS = "radius";
	public static final String BLEND = "blend";

	public BlurComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/filter_blur.comp", getBaseBuildingDeps(LOCAL_SIZE)),
				LOCAL_SIZE);
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		this.createUniform(TXT0);
		this.createUniform(THRESHOLD);
		this.createUniform(RADIUS);
		this.createUniform(BLEND);
	}

}
