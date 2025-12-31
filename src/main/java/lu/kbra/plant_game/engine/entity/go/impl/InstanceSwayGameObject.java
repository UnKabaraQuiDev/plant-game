package lu.kbra.plant_game.engine.entity.go.impl;

import static lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject.DEFAULT_DEFORM_RATIO;
import static lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject.DEFAULT_SCALE_RATIO;
import static lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject.DEFAULT_SPEED_RATIO;

public class InstanceSwayGameObject extends InstanceGameObject implements SwayOwner, SwayInstanceEmitterOwner {

	protected SwayInstanceEmitterComponent swayInstanceEmitterComponent;

	protected float deformRatio = DEFAULT_DEFORM_RATIO;
	protected float speedRatio = DEFAULT_SPEED_RATIO;
	protected float scaleRatio = DEFAULT_SCALE_RATIO;

	public InstanceSwayGameObject(final String str, final SwayInstanceEmitter ie) {
		super(str, null);
		this.setSwayInstanceEmitter(ie);
	}

	@Override
	public float getDeformRatio() {
		return this.deformRatio;
	}

	@Override
	public float getSpeedRatio() {
		return this.speedRatio;
	}

	@Override
	public float getScaleRatio() {
		return this.scaleRatio;
	}

	@Override
	public void setDeformRatio(final float dr) {
		this.deformRatio = dr;
	}

	@Override
	public void setSpeedRatio(final float sr) {
		this.speedRatio = sr;
	}

	@Override
	public void setScaleRatio(final float scaleRatio) {
		this.scaleRatio = scaleRatio;
	}

	public SwayInstanceEmitterComponent getSwayInstanceEmitterComponent() {
		return this.swayInstanceEmitterComponent;
	}

	@Override
	public SwayInstanceEmitter getSwayInstanceEmitter() {
		return this.swayInstanceEmitterComponent == null ? null : this.swayInstanceEmitterComponent.getSwayInstanceEmitter();
	}

	@Override
	public void setSwayInstanceEmitter(final SwayInstanceEmitter ie) {
		if (this.swayInstanceEmitterComponent != null) {
			if (ie == null) {
				super.removeComponent(SwayInstanceEmitterComponent.class);
			} else {
				this.swayInstanceEmitterComponent.setSwayInstanceEmitter(ie);
			}
		} else if (ie != null) {
			super.addComponent(this.swayInstanceEmitterComponent = new SwayInstanceEmitterComponent(ie));
		}
	}

	@Override
	public String toString() {
		return "InstancedSwayGameObject [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio="
				+ this.scaleRatio + ", materialId=" + this.materialId + ", entityMaterialId=" + this.isEntityMaterialId + ", objectId="
				+ this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", active=" + this.active + ", name=" + this.name
				+ ", getInstanceEmitter()=" + this.getInstanceEmitter() + ", getMesh()=" + this.getMesh() + ", getTransform()="
				+ this.getTransform() + "]";
	}

}
