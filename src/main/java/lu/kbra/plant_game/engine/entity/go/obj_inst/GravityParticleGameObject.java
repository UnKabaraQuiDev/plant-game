package lu.kbra.plant_game.engine.entity.go.obj_inst;

import org.joml.Vector3f;
import org.joml.Vector3ic;

import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GravityParticleGameObject extends ParticleGameObject implements GravityOwner {

	protected Vector3f gravity;

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final short materialId, final Vector3f gravity) {
		super(str, instanceEmitter, transform, materialId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId, final Vector3f gravity) {
		super(str, instanceEmitter, transform, objectId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId, final short materialId, final Vector3f gravity) {
		super(str, instanceEmitter, transform, objectId, materialId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final short materialId,
			final Vector3f gravity) {
		super(str, instanceEmitter, materialId);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3f gravity) {
		super(str, instanceEmitter, transform);
		this.gravity = gravity;
	}

	public GravityParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Vector3f gravity) {
		super(str, instanceEmitter);
		this.gravity = gravity;
	}

	@Override
	public Vector3f getGravity() {
		return this.gravity;
	}

}
