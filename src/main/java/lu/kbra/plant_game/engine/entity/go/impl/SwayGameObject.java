package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.render.SwayMeshComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class SwayGameObject extends GameObject implements SwayOwner {

	public static final float DEFAULT_DEFORM_RATIO = 0.1f;
	public static final float DEFAULT_SPEED_RATIO = 0.1f;
	public static final float DEFAULT_SCALE_RATIO = 0.1f;

	protected SwayMeshComponent swayMeshComponent;

	protected float deformRatio;
	protected float speedRatio;
	protected float scaleRatio;

	public SwayGameObject(
			final String str,
			final SwayMesh swayMesh,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, null);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
		this.scaleRatio = scaleRatio;
	}

	public SwayGameObject(final String str, final SwayMesh swayMesh, final Transform3D transform) {
		super(str, null, transform);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = DEFAULT_DEFORM_RATIO;
		this.speedRatio = DEFAULT_SPEED_RATIO;
		this.scaleRatio = DEFAULT_SCALE_RATIO;
	}

	public SwayGameObject(final String str, final SwayMesh swayMesh, final Transform3D transform, final short materialId) {
		super(str, null, transform, getRandomObjectId(), materialId);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = DEFAULT_DEFORM_RATIO;
		this.speedRatio = DEFAULT_SPEED_RATIO;
		this.scaleRatio = DEFAULT_SCALE_RATIO;
	}

	public SwayGameObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, null, transform);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
		this.scaleRatio = scaleRatio;
	}

	public SwayGameObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, null, transform, objectId);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
		this.scaleRatio = scaleRatio;
	}

	public SwayGameObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, null, transform, objectId, materialId);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
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

	public SwayMeshComponent getSwayMeshComponent() {
		return this.swayMeshComponent;
	}

	public SwayMesh getSwayMesh() {
		return this.swayMeshComponent == null ? null : this.swayMeshComponent.getSwayMesh();
	}

	@Override
	public String toString() {
		return "SwayGameObject [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio
				+ ", materialId=" + this.materialId + ", entityMaterialId=" + this.entityMaterialId + ", objectId=" + this.objectId
				+ ", objectIdLocation=" + this.objectIdLocation + ", active=" + this.active + ", name=" + this.name + ", getSwayMesh()="
				+ this.getSwayMesh() + ", getMesh()=" + this.getMesh() + ", getTransform()=" + this.getTransform() + "]";
	}

}
