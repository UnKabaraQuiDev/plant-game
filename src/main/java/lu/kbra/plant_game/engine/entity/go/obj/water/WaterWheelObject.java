package lu.kbra.plant_game.engine.entity.go.obj.water;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.impl.WaterContainer;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water_wheel.json")
public class WaterWheelObject extends AnimatedGameObject implements PlaceableObject, WaterContainer {

	protected long waterLevel;

	public WaterWheelObject(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId) {
		super(str, mesh, animatedMesh, transform, objectId, materialId);
	}

	public WaterWheelObject(
			final String str,
			final Mesh mesh,
			final AnimatedMesh animatedMesh,
			final Transform3D transform,
			final Vector3i objectId) {
		super(str, mesh, animatedMesh, transform, objectId);
	}

	public WaterWheelObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh, final Transform3D transform) {
		super(str, mesh, animatedMesh, transform);
	}

	public WaterWheelObject(final String str, final Mesh mesh, final AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public Matrix4f computeAnimatedTransform(final float t) {
		this
				.getTransform()
				.getMatrix()
				.mul(this.animatedTransform.identity().rotateZ((float) Math.toRadians(t * 25)), this.animatedTransform);
		return this.animatedTransform;
	}

	@Override
	public Vector2i getFootprint() {
		return new Vector2i(3, 3);
	}

	@Override
	public Vector2i getOriginOffset() {
		return new Vector2i(1, 1);
	}

	@Override
	public long getWater() {
		return this.waterLevel;
	}

	@Override
	public boolean hasWater(final long val) {
		return this.waterLevel >= val;
	}

	@Override
	public void addWater(final long val) {
		this.waterLevel += val;
	}

	@Override
	public void removeWater(final long val) {
		this.waterLevel -= val;
	}

}
