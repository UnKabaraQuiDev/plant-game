package lu.kbra.plant_game.engine.entity.go.obj_inst.particles;

import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GravityParticleGameObject extends ParticleGameObject {

	public static final int VELOCITY_BUFFER_INDEX = InstanceEmitter.FIRST_BUFFER_INDEX + 1;
	public static final int ACCELERATION_BUFFER_INDEX = VELOCITY_BUFFER_INDEX + 1;

	public static final float IRON_DENSITY = 7800;

	protected boolean applyAcceleration = true;
	protected boolean enforceMinY = false;
	protected float minY = 0;
	protected float density = 1;

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final short materialId, final boolean applyAcceleration, final boolean enforceMinY, final float minY, final float density) {
		super(str, instanceEmitter, transform, materialId);
		this.applyAcceleration = applyAcceleration;
		this.enforceMinY = enforceMinY;
		this.minY = minY;
		this.density = density;
	}

	public float getMinY() {
		return this.minY;
	}

	public void setMinY(final float minY) {
		this.minY = minY;
	}

	public boolean isApplyGravity() {
		return this.applyAcceleration;
	}

	public void isApplyGravity(final boolean applyAcceleration) {
		this.applyAcceleration = applyAcceleration;
	}

	public boolean isEnforceMinY() {
		return this.enforceMinY;
	}

	public void setEnforceMinY(final boolean enforceMinY) {
		this.enforceMinY = enforceMinY;
	}

	public float getDensity() {
		return this.density;
	}

	public void setDensity(final float density) {
		this.density = density;
	}

}
