package lu.kbra.plant_game.engine.entity.water;

import org.joml.Vector2i;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.impl.WaterContainer;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water_tower.json")
public class WaterTowerObject extends GameObject implements PlaceableObject, WaterContainer {

	protected long waterLevel;

	public WaterTowerObject(String str, Mesh mesh, Transform3D transform, Vector3i objectId) {
		super(str, mesh, transform, objectId);
	}

	public WaterTowerObject(String str, Mesh mesh, Transform3D transform) {
		super(str, mesh, transform);
		System.err.println("creating: " + str + " with mesh " + mesh);
	}

	public WaterTowerObject(String str, Mesh mesh) {
		super(str, mesh);
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
