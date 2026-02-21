package lu.kbra.plant_game.engine.render.shader.compute.filter;

import java.util.Map;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class BlurComputeShader extends ComputeShader {

	private static final Vector3ic LOCAL_SIZE = new Vector3i(16, 16, 1);
	private static final int MAX_RADIUS = 10;

	public static final String TXT_BLOOM = "txtBloom";
	public static final String HORIZONTAL = "horizontal";
	public static final String RADIUS = "radius";
	public static final String WEIGHTS = "weights";

	public BlurComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/filter_blur.comp", getBuildingDeps(LOCAL_SIZE)), LOCAL_SIZE);
	}

	private static Map<String, Object> getBuildingDeps(final Vector3ic localSize) {
		final Map<String, Object> dep = getBaseBuildingDeps(localSize);
		dep.put("%MAX_RADIUS%", MAX_RADIUS);
		return dep;
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		super.createUniform(TXT_BLOOM);
		super.createUniform(HORIZONTAL);
		super.createUniform(RADIUS);
		super.createUniform(WEIGHTS);
	}

}
