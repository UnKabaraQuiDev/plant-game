package lu.kbra.plant_game.engine.entity.water;

import org.joml.Matrix4f;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnimatedGameObject extends GameObject {

	protected Matrix4f animatedTransform = new Matrix4f().identity();
	protected AnimatedMeshComponent animatedMeshComponent;

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh) {
		this(str, mesh, animatedMesh, null);
	}

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform) {
		this(str, mesh, animatedMesh, transform, false);
	}

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform,
			boolean transparent) {
		this(str, mesh, animatedMesh, transform, transparent,
				new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255));
	}

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform,
			boolean transparent, Vector3i objectId) {
		this(str, mesh, animatedMesh, transform, transparent, objectId, (short) -1);
	}

	public AnimatedGameObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform,
			boolean transparent, Vector3i objectId, short materialId) {
		super(str, mesh, transform, transparent, objectId, materialId);
		super.addComponent(new AnimatedMeshComponent(animatedMesh));
		this.animatedMeshComponent = super.getComponent(AnimatedMeshComponent.class);
	}

	public void computeAnimatedTransform(float t) {
		final AnimatedMesh animatedMesh = getAnimatedMesh();
		getTransform().getMatrix().mul(animatedMesh.computeTransform(animatedTransform, t), animatedTransform);
	}

	public void setAnimatedTransform(Matrix4f animatedTransform) {
		this.animatedTransform = animatedTransform;
	}

	public Matrix4f getAnimatedTransform() {
		return animatedTransform;
	}

	public AnimatedMesh getAnimatedMesh() {
		return animatedMeshComponent == null ? null : animatedMeshComponent.getMesh();
	}

	public AnimatedMeshComponent getAnimatedMeshComponent() {
		return animatedMeshComponent;
	}

}
