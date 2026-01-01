package lu.kbra.plant_game.engine.entity.go.obj_inst.particles;

import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

public class GravityParticleGameObject extends ParticleGameObject {

	public static final int VELOCITY_BUFFER_INDEX = InstanceEmitter.FIRST_BUFFER_INDEX + 1;
	public static final int ACCELERATION_BUFFER_INDEX = VELOCITY_BUFFER_INDEX + 1;

	public static final float IRON_DENSITY = 7800;

	protected boolean applyAcceleration = true;
	protected boolean enforceMinY = false;
	protected boolean applyDrag = false;
	protected float minY = 0;
	protected float density = 1;

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

	public float getMinY() {
		return this.minY;
	}

	public void setMinY(final float minY) {
		this.minY = minY;
	}

	public boolean isApplyAcceleration() {
		return this.applyAcceleration;
	}

	public void isApplyAcceleration(final boolean applyAcceleration) {
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

	public boolean isApplyDrag() {
		return this.applyDrag;
	}

	public void setApplyDrag(final boolean applyDrag) {
		this.applyDrag = applyDrag;
	}

	@Override
	public String toString() {
		return "GravityParticleGameObject [applyAcceleration=" + this.applyAcceleration + ", enforceMinY=" + this.enforceMinY
				+ ", applyDrag=" + this.applyDrag + ", minY=" + this.minY + ", density=" + this.density + ", isEntityMaterialId="
				+ this.isEntityMaterialId + ", materialId=" + this.materialId + ", instanceEmitter=" + this.instanceEmitter + ", objectId="
				+ this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
