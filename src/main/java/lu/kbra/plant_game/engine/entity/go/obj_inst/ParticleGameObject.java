package lu.kbra.plant_game.engine.entity.go.obj_inst;

import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/cube.json")
public class ParticleGameObject extends InstanceGameObject {

	public static final int VELOCITY_BUFFER_INDEX = InstanceEmitter.FIRST_BUFFER_INDEX;
	public static final int ACCELERATION_BUFFER_INDEX = VELOCITY_BUFFER_INDEX + 1;

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final short materialId) {
		super(str, instanceEmitter, materialId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId, final short materialId) {
		super(str, instanceEmitter, transform, objectId, materialId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform,
			final Vector3ic objectId) {
		super(str, instanceEmitter, transform, objectId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform, final short materialId) {
		super(str, instanceEmitter, transform, materialId);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter, final Transform3D transform) {
		super(str, instanceEmitter, transform);
	}

	public ParticleGameObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

}
