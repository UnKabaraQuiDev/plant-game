package lu.kbra.plant_game.engine.render.shader.compute.filter;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.RenderShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;

public class FilterShader extends RenderShader {

	public static final String INPUT_SIZE = ComputeShader.INPUT_SIZE;
	public static final String OUTPUT_SIZE = ComputeShader.OUTPUT_SIZE;
	public static final String TXT0 = "txt0";

	public FilterShader(final AbstractShaderPart... parts) {
		super(parts);
	}

	public FilterShader(final boolean transparent, final AbstractShaderPart... parts) {
		super(transparent, parts);
	}

	public FilterShader(final String name, final AbstractShaderPart... parts) {
		super(name, parts);
	}

	public FilterShader(final String name, final boolean transparent, final AbstractShaderPart... parts) {
		super(name, transparent, parts);
	}

	@Override
	public void createUniforms() {
		super.createSceneUniforms();

		this.createUniform(TXT0);
		super.createUniform(INPUT_SIZE);
		super.createUniform(OUTPUT_SIZE);
	}

}
