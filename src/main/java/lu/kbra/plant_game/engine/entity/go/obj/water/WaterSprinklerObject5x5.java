package lu.kbra.plant_game.engine.entity.go.obj.water;

import org.joml.Vector2i;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.AnimatedGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water-sprinkler-5x5.json")
public class WaterSprinklerObject5x5 extends AnimatedGameObject implements PlaceableObject {

	public WaterSprinklerObject5x5(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform, Vector3i objectId,
			short materialId) {
		super(str, mesh, animatedMesh, transform, objectId, materialId);
		// TODO Auto-generated constructor stub
	}

	public WaterSprinklerObject5x5(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform, Vector3i objectId) {
		super(str, mesh, animatedMesh, transform, objectId);
		// TODO Auto-generated constructor stub
	}

	public WaterSprinklerObject5x5(String str, Mesh mesh, AnimatedMesh animatedMesh, Transform3D transform) {
		super(str, mesh, animatedMesh, transform);
		// TODO Auto-generated constructor stub
	}

	public WaterSprinklerObject5x5(String str, Mesh mesh, AnimatedMesh animatedMesh) {
		super(str, mesh, animatedMesh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector2i getFootprint() {
		return new Vector2i(3, 3);
	}

	@Override
	public Vector2i getOriginOffset() {
		return new Vector2i(0, 0);
	}

}
