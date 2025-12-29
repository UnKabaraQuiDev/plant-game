package lu.kbra.plant_game.engine.entity.go.obj_inst.grass;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/grass-medium.json")
public class InstanceMediumGrassObject extends InstanceSwayGameObject {

	public InstanceMediumGrassObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, instanceEmitter, deformRatio, speedRatio, scaleRatio);
		// TODO Auto-generated constructor stub
	}

	public InstanceMediumGrassObject(final String str, final SwayInstanceEmitter instanceEmitter, final short materialId) {
		super(str, instanceEmitter, materialId);
		// TODO Auto-generated constructor stub
	}

	public InstanceMediumGrassObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final Transform3D transform,
			final short materialId) {
		super(str, instanceEmitter, transform, materialId);
		// TODO Auto-generated constructor stub
	}

	public InstanceMediumGrassObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final Transform3D transform,
			final Vector3i objectId,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, instanceEmitter, transform, objectId, deformRatio, speedRatio, scaleRatio);
		// TODO Auto-generated constructor stub
	}

	public InstanceMediumGrassObject(
			final String str,
			final SwayInstanceEmitter instanceEmitter,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId,
			final float deformRatio,
			final float speedRatio,
			final float scaleRatio) {
		super(str, instanceEmitter, transform, objectId, materialId, deformRatio, speedRatio, scaleRatio);
		// TODO Auto-generated constructor stub
	}

	public InstanceMediumGrassObject(final String str, final SwayInstanceEmitter instanceEmitter, final Transform3D transform) {
		super(str, instanceEmitter, transform);
		// TODO Auto-generated constructor stub
	}

	public InstanceMediumGrassObject(final String str, final SwayInstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
		// TODO Auto-generated constructor stub
	}

}
