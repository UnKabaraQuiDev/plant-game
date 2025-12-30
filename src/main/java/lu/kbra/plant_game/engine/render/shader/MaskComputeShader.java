package lu.kbra.plant_game.engine.render.shader;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class MaskComputeShader extends ComputeShader {

	public static final Vector3ic LOCAL_SIZE = new Vector3i(16, 16, 1);

	public static final String INPUT_SIZE = "inputSize";
	public static final String OUTPUT_SIZE = "outputSize";

	public MaskComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/mask_objects.comp", getBaseBuildingDeps(LOCAL_SIZE)),
				LOCAL_SIZE);
	}

}
