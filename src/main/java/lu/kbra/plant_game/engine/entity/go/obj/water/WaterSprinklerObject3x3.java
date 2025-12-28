package lu.kbra.plant_game.engine.entity.go.obj.water;

import org.joml.Matrix4f;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableAnimatedGameObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water-sprinkler-3x3.json")
public class WaterSprinklerObject3x3 extends PlaceableAnimatedGameObject implements PlaceableObject {

	public WaterSprinklerObject3x3(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId) {
		super(str, mesh, animatedMesh, transform, objectId, materialId);
	}

	public WaterSprinklerObject3x3(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId) {
		super(str, mesh, animatedMesh, transform, objectId);
	}

	public WaterSprinklerObject3x3(final String str, final Mesh mesh, final AnimatedMesh animatedMesh, final Transform3D transform) {
		super(str, mesh, animatedMesh, transform);
	}

	public WaterSprinklerObject3x3(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this
				.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateY((float) Math.toRadians(t * 12)), this.animatedTransform);
		return this.animatedTransform;
	}

}
