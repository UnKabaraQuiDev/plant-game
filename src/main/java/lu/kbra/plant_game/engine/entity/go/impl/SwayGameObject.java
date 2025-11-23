package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.render.SwayMeshComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class SwayGameObject extends GameObject implements SwayOwner {

	protected SwayMeshComponent swayMeshComponent;

	private float deformRatio;
	private float speedRatio;

	public SwayGameObject(final String str, final SwayMesh swayMesh, final float deformRatio, final float speedRatio) {
		super(str, null);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
	}

	public SwayGameObject(final String str, final SwayMesh swayMesh, final Transform3D transform) {
		super(str, null, transform);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = 1;
		this.speedRatio = 1;
	}

	public SwayGameObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final float deformRatio,
			final float speedRatio) {
		super(str, null, transform);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
	}

	public SwayGameObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final float deformRatio,
			final float speedRatio) {
		super(str, null, transform, objectId);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
	}

	public SwayGameObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId,
			final float deformRatio,
			final float speedRatio) {
		super(str, null, transform, objectId, materialId);
		super.addComponent(this.swayMeshComponent = new SwayMeshComponent(swayMesh));
		this.deformRatio = deformRatio;
		this.speedRatio = speedRatio;
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
	public void setDeformRatio(final float dr) {
		this.deformRatio = dr;
	}

	@Override
	public void setSpeedRatio(final float sr) {
		this.speedRatio = sr;
	}

	public SwayMeshComponent getSwayMeshComponent() {
		return this.swayMeshComponent;
	}

	public SwayMesh getSwayMesh() {
		return this.swayMeshComponent == null ? null : this.swayMeshComponent.getSwayMesh();
	}

	@Override
	public String toString() {
		return "SwayGameObject [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", materialId=" + this.materialId
				+ ", entityMaterialId=" + this.entityMaterialId + ", objectId=" + this.objectId + ", objectIdLocation="
				+ this.objectIdLocation + ", active=" + this.active + ", name=" + this.name + ", getSwayMesh()=" + this.getSwayMesh()
				+ ", getMesh()=" + this.getMesh() + ", getTransform()=" + this.getTransform() + "]";
	}

}
