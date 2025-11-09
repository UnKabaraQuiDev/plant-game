package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Matrix4f;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshComponent;
import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshOwner;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnimatedGameObject extends GameObject implements AnimatedTransformOwner, AnimatedMeshOwner {

	protected Matrix4f animatedTransform = new Matrix4f().identity();
	protected AnimatedMeshComponent animatedMeshComponent;

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh) {
		this(str, mesh, animatedMesh, null);
	}

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform) {
		this(str, mesh, animatedMesh, transform,
				new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255));
	}

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform,
			Vector3i objectId) {
		this(str, mesh, animatedMesh, transform, objectId, (short) -1);
	}

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform,
			Vector3i objectId, short materialId) {
		super(str, mesh, transform, objectId, materialId);
		super.addComponent(new AnimatedMeshComponent(animatedMesh));
		this.animatedMeshComponent = super.getComponent(AnimatedMeshComponent.class);
	}

	@Override
	public Matrix4f computeAnimatedTransform(float t) {
		final AnimatedMesh animatedMesh = getAnimatedMesh();
		getTransform().getMatrix().mul(animatedMesh.computeTransform(animatedTransform, t), animatedTransform);
		return animatedTransform;
	}

	@Override
	public void setAnimatedTransform(Matrix4f animatedTransform) {
		this.animatedTransform = animatedTransform;
	}

	@Override
	public Matrix4f getAnimatedTransform() {
		return animatedTransform;
	}

	@Override
	public AnimatedMesh getAnimatedMesh() {
		return animatedMeshComponent == null ? null : animatedMeshComponent.getAnimatedMesh();
	}

	public AnimatedMeshComponent getAnimatedMeshComponent() {
		return animatedMeshComponent;
	}

	@Override
	public String toString() {
		return "AnimatedGameObject [animatedTransform=" + animatedTransform + ", materialId=" + materialId
				+ ", entityMaterialId=" + entityMaterialId + ", objectId=" + objectId + ", objectIdLocation="
				+ objectIdLocation + ", getComponents()=" + getComponents().size() + ", isActive()=" + isActive() + "]";
	}

}
