package lu.kbra.plant_game.engine.entity.go.obj.flower.round;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/flower_round_large.json")
public class LargeRoundFlowerObject extends SwayGameObject {

	public LargeRoundFlowerObject(final String str, final SwayMesh swayMesh, final Transform3D transform) {
		super(str, swayMesh, transform);
		// TODO Auto-generated constructor stub
	}

	public LargeRoundFlowerObject(final String str, final SwayMesh swayMesh, final float deformRatio, final float speedRatio) {
		super(str, swayMesh, deformRatio, speedRatio);
		// TODO Auto-generated constructor stub
	}

	public LargeRoundFlowerObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final float deformRatio,
			final float speedRatio) {
		super(str, swayMesh, transform, deformRatio, speedRatio);
		// TODO Auto-generated constructor stub
	}

	public LargeRoundFlowerObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final float deformRatio,
			final float speedRatio) {
		super(str, swayMesh, transform, objectId, deformRatio, speedRatio);
		// TODO Auto-generated constructor stub
	}

	public LargeRoundFlowerObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId,
			final float deformRatio,
			final float speedRatio) {
		super(str, swayMesh, transform, objectId, materialId, deformRatio, speedRatio);
		// TODO Auto-generated constructor stub
	}

}
