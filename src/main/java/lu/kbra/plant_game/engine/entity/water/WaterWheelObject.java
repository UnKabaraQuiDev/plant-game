package lu.kbra.plant_game.engine.entity.water;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.impl.WaterContainer;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water_wheel.json")
public class WaterWheelObject extends AnimatedGameObject implements PlaceableObject, WaterContainer {

	protected long waterLevel;

	public WaterWheelObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform, Vector3i objectId,
			short materialId) {
		super(str, mesh, animatedMesh, transform, objectId, materialId);
	}

	public WaterWheelObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform,
			Vector3i objectId) {
		super(str, mesh, animatedMesh, transform, objectId);
	}

	public WaterWheelObject(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform) {
		super(str, mesh, animatedMesh, transform);
	}

	public WaterWheelObject(String str, Mesh mesh, AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
	}

	@Override
	public Matrix4f computeAnimatedTransform(float t) {
		getTransform().getMatrix().mul(animatedTransform.identity().rotateZ((float) Math.toRadians(t * 25)),
				animatedTransform);
		return animatedTransform;
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
		return waterLevel;
	}

	@Override
	public boolean hasWater(long val) {
		return waterLevel >= val;
	}

	@Override
	public void addWater(long val) {
		this.waterLevel += val;
	}

	@Override
	public void removeWater(long val) {
		this.waterLevel -= val;
	}

}
