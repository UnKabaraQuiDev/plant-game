package lu.kbra.plant_game.engine.entity.go;

import org.joml.Matrix4f;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshOwner;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.entity.impl.FixedAnimationOwner;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class AnimatedMeshGameObject extends MeshGameObject implements AnimatedTransformOwner, AnimatedMeshOwner, FixedAnimationOwner {

	@JsonIgnore
	protected AnimatedMesh animatedMesh;

	@JsonIgnore
	protected Matrix4f animatedTransform = new Matrix4f().identity();

	protected float animationTime = 0;

	public AnimatedMeshGameObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh);
		this.setAnimatedMesh(animatedMesh);
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		return FixedAnimationOwner.super.computeAnimatedTransform(t);
	}

	@Override
	public void setAnimatedTransform(final Matrix4f animatedTransform) {
		this.animatedTransform = animatedTransform;
	}

	@Override
	public Matrix4f getAnimatedTransform() {
		return this.animatedTransform;
	}

	@Override
	public AnimatedMesh getAnimatedMesh() {
		return this.animatedMesh;
	}

	@Override
	public void setAnimatedMesh(final AnimatedMesh ie) {
		this.animatedMesh = ie;
	}

	@Override
	public float getAnimationTime() {
		return this.animationTime;
	}

	@Override
	public void setAnimationTime(final float animationTime) {
		this.animationTime = animationTime;
	}

	@Override
	public float getAnimationDuration() {
		return 1;
	}

	@Override
	public boolean isAnimationPingPong() {
		return true;
	}

	@Override
	public String toString() {
		return "AnimatedMeshGameObject [animatedTransform=" + this.animatedTransform + ", animatedMesh=" + this.animatedMesh
				+ ", materialId=" + this.materialId + ", isEntityMaterialId=" + this.isEntityMaterialId + ", objectId=" + this.objectId
				+ ", objectIdLocation=" + this.objectIdLocation + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
