package lu.kbra.plant_game.engine.scene.world.particle.shader;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.graph.shader.ComputeShader;
import lu.kbra.standalone.gameengine.graph.shader.part.AbstractShaderPart;
import lu.kbra.standalone.gameengine.graph.shader.part.ComputeShaderPart;

public class GravityParticleComputeShader extends ComputeShader {

	public static final Vector3ic LOCAL_SIZE = new Vector3i(32, 1, 1);

	public static final String ACCELERATION = "acceleration";
	public static final String APPLY_ACCELERATION = "applyAcceleration";
	public static final String RESET_ACCELERATION = "resetAcceleration";
	public static final String APPLY_DRAG = "applyDrag";
	public static final String COUNT = "count";
	public static final String D_TIME = "dTime";
	public static final String ENFORCE_MIN_Y = "enforceMinY";
	public static final String MIN_Y = "minY";
	public static final String REBOUND_COEFFICIENT = "reboundCoefficient";
	public static final String FRICTION_COEFFICIENT = "frictionCoefficient";
	public static final String DRAG_COEFFICIENT = "dragCoefficient";
	public static final String DENSITY = "density";

	public GravityParticleComputeShader() {
		super((ComputeShaderPart) AbstractShaderPart.load("classpath:/shaders/gravity_particle.comp", getBaseBuildingDeps(LOCAL_SIZE)),
				LOCAL_SIZE);
	}

	@Override
	public void createUniforms() {
		super.createUniforms();

		this.createUniform(ACCELERATION);
		this.createUniform(APPLY_ACCELERATION);
		this.createUniform(RESET_ACCELERATION);
		this.createUniform(COUNT);
		this.createUniform(D_TIME);
		this.createUniform(ENFORCE_MIN_Y);
		this.createUniform(MIN_Y);
		this.createUniform(REBOUND_COEFFICIENT);
		this.createUniform(FRICTION_COEFFICIENT);
		this.createUniform(DRAG_COEFFICIENT);
		this.createUniform(DENSITY);
	}

}
