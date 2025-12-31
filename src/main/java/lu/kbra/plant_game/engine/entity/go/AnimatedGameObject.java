package lu.kbra.plant_game.engine.entity.go;

import org.joml.Matrix4f;

import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshComponent;
import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshOwner;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class AnimatedGameObject extends GameObject implements AnimatedTransformOwner, AnimatedMeshOwner {

	protected Matrix4f animatedTransform = new Matrix4f().identity();
	protected AnimatedMeshComponent animatedMeshComponent;

	public AnimatedGameObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh);
		this.setAnimatedMesh(animatedMesh);
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		final AnimatedMesh animatedMesh = this.getAnimatedMesh();
		this.getTransform().getMatrix().mul(animatedMesh.computeTransform(this.animatedTransform, t), this.animatedTransform);
		return this.animatedTransform;
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
		return this.animatedMeshComponent == null ? null : this.animatedMeshComponent.getAnimatedMesh();
	}

	public AnimatedMeshComponent getAnimatedMeshComponent() {
//		create(GameObject.class)
//				.set(i -> i.setTransform3D(...))
//				.addTo(scene)
//				.push();
		return this.animatedMeshComponent;
	}

	@Override
	public void setAnimatedMesh(final AnimatedMesh ie) {
		if (this.animatedMeshComponent != null) {
			if (ie == null) {
				super.removeComponent(AnimatedMeshComponent.class);
				this.animatedMeshComponent = null;
			} else {
				this.animatedMeshComponent.setAnimatedMesh(ie);
			}
		} else if (ie != null) {
			super.addComponent(this.animatedMeshComponent = new AnimatedMeshComponent(ie));
		}
	}

	@Override
	public String toString() {
		return "AnimatedGameObject [animatedTransform=" + this.animatedTransform + ", materialId=" + this.materialId + ", entityMaterialId="
				+ this.isEntityMaterialId + ", objectId=" + this.objectId + ", objectIdLocation=" + this.objectIdLocation
				+ ", getComponents()=" + this.getComponents().size() + ", isActive()=" + this.isActive() + "]";
	}

}
