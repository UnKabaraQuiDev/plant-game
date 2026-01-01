package lu.kbra.plant_game.engine.entity.go.impl;

import lu.kbra.plant_game.engine.entity.go.MeshGameObject;
import lu.kbra.plant_game.engine.entity.impl.SwayOwner;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class SwayGameObject extends MeshGameObject implements SwayOwner {

	public static final float DEFAULT_DEFORM_RATIO = 0.1f;
	public static final float DEFAULT_SPEED_RATIO = 0.1f;
	public static final float DEFAULT_SCALE_RATIO = 0.1f;

	protected float deformRatio = DEFAULT_DEFORM_RATIO;
	protected float speedRatio = DEFAULT_SPEED_RATIO;
	protected float scaleRatio = DEFAULT_SCALE_RATIO;

	public SwayGameObject(final String str, final Mesh mesh) {
		super(str, mesh);
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
		return "SwayGameObject [deformRatio=" + this.deformRatio + ", speedRatio=" + this.speedRatio + ", scaleRatio=" + this.scaleRatio
				+ ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId + ", objectId=" + this.objectId
				+ ", objectIdLocation=" + this.objectIdLocation + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
