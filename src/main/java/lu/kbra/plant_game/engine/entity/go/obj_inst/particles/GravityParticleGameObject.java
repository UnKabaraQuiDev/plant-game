package lu.kbra.plant_game.engine.entity.go.obj_inst.particles;

import org.joml.Vector3f;
import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.impl.AccelerationOwner;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GravityParticleGameObject extends ParticleGameObject implements AccelerationOwner {

	public static final int VELOCITY_BUFFER_INDEX = InstanceEmitter.FIRST_BUFFER_INDEX + 1;
	public static final int ACCELERATION_BUFFER_INDEX = VELOCITY_BUFFER_INDEX + 1;

	protected Vector3f gravity;
	protected boolean resetAcceleration = true;
	protected boolean applyAcceleration = true;

	protected int velocityGlId, accelerationGlId;

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final short materialId, final Vector3f gravity) {
		super(str, instanceEmitter, transform, materialId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId, final Vector3f gravity) {
		super(str, instanceEmitter, transform, objectId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId, final short materialId, final Vector3f gravity) {
		super(str, instanceEmitter, transform, objectId, materialId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final short materialId,
			final Vector3f gravity) {
		super(str, instanceEmitter, materialId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3f gravity) {
		super(str, instanceEmitter, transform);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Vector3f gravity) {
		super(str, instanceEmitter);
		this.gravity = gravity;
	}

	@Override
	public Vector3f getAcceleration() {
		return this.gravity;
	}

	public Vector3f getGravity() {
		return this.gravity;
	}

	public void setGravity(final Vector3f gravity) {
		this.gravity = gravity;
	}

	@Override
	public boolean isResetAcceleration() {
		return this.resetAcceleration;
	}

	public void setResetAcceleration(final boolean resetAcceleration) {
		this.resetAcceleration = resetAcceleration;
	}

	@Override
	public boolean isApplyAcceleration() {
		return this.applyAcceleration;
	}

	public void setApplyAcceleration(final boolean applyAcceleration) {
		this.applyAcceleration = applyAcceleration;
	}

	public int getVelocityGlId() {
		return this.velocityGlId;
	}

	public void setVelocityGlId(final int velocityGlId) {
		this.velocityGlId = velocityGlId;
	}

	public int getAccelerationGlId() {
		return this.accelerationGlId;
	}

	public void setAccelerationGlId(final int accelerationGlId) {
		this.accelerationGlId = accelerationGlId;
	}

}
