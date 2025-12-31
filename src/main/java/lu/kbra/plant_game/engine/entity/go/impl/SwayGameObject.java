package lu.kbra.plant_game.engine.entity.go.impl;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.render.SwayMeshComponent;

public class SwayGameObject extends GameObject implements SwayOwner, SwayMeshOwner {

	public static final float DEFAULT_DEFORM_RATIO = 0.1f;
	public static final float DEFAULT_SPEED_RATIO = 0.1f;
	public static final float DEFAULT_SCALE_RATIO = 0.1f;

	protected SwayMeshComponent swayMeshComponent;

	protected float deformRatio = DEFAULT_DEFORM_RATIO;
	protected float speedRatio = DEFAULT_SPEED_RATIO;
	protected float scaleRatio = DEFAULT_SCALE_RATIO;

	public SwayGameObject(final String str, final SwayMesh mesh) {
		super(str, null);
		this.setSwayMesh(mesh);
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

	@Override
	public SwayMesh getSwayMesh() {
		return this.swayMeshComponent == null ? null : this.swayMeshComponent.getSwayMesh();
	}

	@Override
	public void setSwayMesh(final SwayMesh ie) {
		if (this.swayMeshComponent != null) {
			if (ie == null) {
				super.removeComponent(SwayMeshComponent.class);
			} else {
				this.swayMeshComponent.setSwayMesh(ie);
			}
		} else if (ie != null) {
			super.addComponent(this.swayMeshComponent = new SwayMeshComponent(ie));
		}
	}

	@Override
	public String toString() {
		return "SwayGameObject [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio
				+ ", materialId=" + this.materialId + ", entityMaterialId=" + this.isEntityMaterialId + ", objectId=" + this.objectId
				+ ", objectIdLocation=" + this.objectIdLocation + ", active=" + this.active + ", name=" + this.name + ", getSwayMesh()="
				+ this.getSwayMesh() + ", getMesh()=" + this.getMesh() + ", getTransform()=" + this.getTransform() + "]";
	}

}
