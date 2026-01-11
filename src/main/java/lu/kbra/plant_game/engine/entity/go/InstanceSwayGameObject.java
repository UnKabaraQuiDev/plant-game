package lu.kbra.plant_game.engine.entity.go;

import static lu.kbra.plant_game.engine.entity.go.SwayGameObject.DEFAULT_DEFORM_RATIO;
import static lu.kbra.plant_game.engine.entity.go.SwayGameObject.DEFAULT_SCALE_RATIO;
import static lu.kbra.plant_game.engine.entity.go.SwayGameObject.DEFAULT_SPEED_RATIO;

import lu.kbra.plant_game.engine.entity.impl.SwayOwner;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

public class InstanceSwayGameObject extends InstanceGameObject implements SwayOwner {

	protected float deformRatio = DEFAULT_DEFORM_RATIO;
	protected float speedRatio = DEFAULT_SPEED_RATIO;
	protected float scaleRatio = DEFAULT_SCALE_RATIO;

	public InstanceSwayGameObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
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

	@Override
	public String toString() {
		return "InstanceSwayGameObject [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio="
				+ this.scaleRatio + ", instanceEmitter=" + this.instanceEmitter + ", objectId=" + this.objectId + ", objectIdLocation="
				+ this.objectIdLocation + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
