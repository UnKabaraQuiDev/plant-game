package lu.kbra.plant_game.engine.entity.go.impl;

import static lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject.DEFAULT_DEFORM_RATIO;
import static lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject.DEFAULT_SCALE_RATIO;
import static lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject.DEFAULT_SPEED_RATIO;

import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class InstanceSwayGameObject extends InstanceGameObject implements SwayOwner {

	protected SwayInstanceEmitterComponent swayMeshComponent;

	protected float deformRatio;
	protected float speedRatio;
	protected float scaleRatio;

	public InstanceSwayGameObject(final String str, final SwayInstanceEmitter instanceEmitter) {
		this(str, instanceEmitter, null, getRandomObjectId(), (short) -1, DEFAULT_DEFORM_RATIO, DEFAULT_SPEED_RATIO, DEFAULT_SCALE_RATIO);
	}

	public InstanceSwayGameObject(final String str, final SwayInstanceEmitter instanceEmitter, final Transform3D transform) {
		this(str,
				instanceEmitter,
				transform,
				getRandomObjectId(),
				(short) -1,
				DEFAULT_DEFORM_RATIO,
				DEFAULT_SPEED_RATIO,
				DEFAULT_SCALE_RATIO);
	}

	public InstanceSwayGameObject(final String str, final SwayInstanceEmitter instanceEmitter, final short materialId) {
		this(str, instanceEmitter, null, getRandomObjectId(), materialId, DEFAULT_DEFORM_RATIO, DEFAULT_SPEED_RATIO, DEFAULT_SCALE_RATIO);
	}

	public InstanceSwayGameObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final Transform3D transform,
			final short materialId) {
		this(str,
				instanceEmitter,
				transform,
				getRandomObjectId(),
				materialId,
				DEFAULT_DEFORM_RATIO,
				DEFAULT_SPEED_RATIO,
				DEFAULT_SCALE_RATIO);
	}

	public InstanceSwayGameObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		this(str, instanceEmitter, null, getRandomObjectId(), (short) -1, DEFAULT_DEFORM_RATIO, DEFAULT_SPEED_RATIO, DEFAULT_SCALE_RATIO);
	}

	public InstanceSwayGameObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final Transform3D transform,
			final Vector3ic objectId,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		this(str, instanceEmitter, transform, objectId, (short) -1, DEFAULT_DEFORM_RATIO, DEFAULT_SPEED_RATIO, DEFAULT_SCALE_RATIO);
	}

	public InstanceSwayGameObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final Transform3D transform,
			final Vector3ic objectId,
			final short materialId,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, null, transform, objectId, materialId);
		super.addComponent(this.swayMeshComponent = new SwayInstanceEmitterComponent(instanceEmitter));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
		this.scaleRatio = scaleRatio;
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

	public SwayInstanceEmitterComponent getSwayMeshComponent() {
		return this.swayMeshComponent;
	}

	@Override
	public InstanceEmitter getInstanceEmitter() {
		return this.swayMeshComponent == null ? null : this.swayMeshComponent.getInstanceEmitter();
	}

	@Override
	public String toString() {
		return "InstancedSwayGameObject [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio="
				+ this.scaleRatio + ", materialId=" + this.materialId + ", entityMaterialId=" + this.entityMaterialId + ", objectId="
				+ this.objectId + ", objectIdLocation=" + this.objectIdLocation + ", active=" + this.active + ", name=" + this.name
				+ ", getInstanceEmitter()=" + this.getInstanceEmitter() + ", getMesh()=" + this.getMesh() + ", getTransform()="
				+ this.getTransform() + "]";
	}

}
