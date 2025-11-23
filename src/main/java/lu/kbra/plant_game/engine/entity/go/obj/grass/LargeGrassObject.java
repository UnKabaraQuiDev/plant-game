package lu.kbra.plant_game.engine.entity.go.obj.grass;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/grass_large.json")
public class LargeGrassObject extends SwayGameObject {

	public LargeGrassObject(final String str, final SwayMesh swayMesh, final Transform3D transform) {
		super(str, swayMesh, transform);
	}

	public LargeGrassObject(final String str, final SwayMesh swayMesh, final float deformRatio, final float speedRatio) {
		super(str, swayMesh, deformRatio, speedRatio);
	}

	public LargeGrassObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final float deformRatio,
			final float speedRatio) {
		super(str, swayMesh, transform, deformRatio, speedRatio);
	}

	public LargeGrassObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final float deformRatio,
			final float speedRatio) {
		super(str, swayMesh, transform, objectId, deformRatio, speedRatio);
	}

	public LargeGrassObject(
			final String str,
			final SwayMesh swayMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId,
			final float deformRatio,
			final float speedRatio) {
		super(str, swayMesh, transform, objectId, materialId, deformRatio, speedRatio);
	}

}