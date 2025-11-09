package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Matrix4f;

import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshComponent;
import lu.kbra.plant_game.engine.entity.impl.AnimatedMeshOwner;
import lu.kbra.plant_game.engine.entity.impl.AnimatedTransformOwner;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public abstract class AnimatedUIObject extends UIObject implements AnimatedTransformOwner, AnimatedMeshOwner {

	protected Matrix4f animatedTransform = new Matrix4f().identity();
	protected AnimatedMeshComponent animatedMeshComponent;

	public AnimatedUIObject(String str, Mesh mesh, AnimatedMesh animatedMesh) {
		this(str, mesh, animatedMesh, null);
	}

	public AnimatedUIObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform) {
		super(str, animatedMesh, transform);
		super.addComponent(new AnimatedMeshComponent(animatedMesh));
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

}
