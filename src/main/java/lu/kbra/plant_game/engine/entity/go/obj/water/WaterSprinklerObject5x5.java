package lu.kbra.plant_game.engine.entity.go.obj.water;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water-sprinkler-5x5.json")
public class WaterSprinklerObject5x5 extends AnimatedGameObject implements PlaceableObject {

	public WaterSprinklerObject5x5(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId) {
		super(str, mesh, animatedMesh, transform, objectId, materialId);
	}

	public WaterSprinklerObject5x5(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId) {
		super(str, mesh, animatedMesh, transform, objectId);
	}

	public WaterSprinklerObject5x5(final String str, final Mesh mesh, final AnimatedMesh animatedMesh, final Transform3D transform) {
		super(str, mesh, animatedMesh, transform);
	}

	public WaterSprinklerObject5x5(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
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

	@Override
	public Vector2i getFootprint() {
		return new Vector2i(1, 1);
	}

	@Override
	public Vector2i getOriginOffset() {
		return new Vector2i(0, 0);
	}

}
