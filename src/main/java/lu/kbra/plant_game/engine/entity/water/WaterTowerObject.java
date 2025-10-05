package lu.kbra.plant_game.engine.entity.water;

import org.joml.Vector2i;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.GameObject;
import lu.kbra.plant_game.engine.entity.PlaceableObject;
import lu.kbra.plant_game.engine.entity.TerrainMesh;
import lu.kbra.plant_game.engine.entity.WaterContainer;
import lu.kbra.plant_game.engine.scene.WorldLevelScene;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water_tower.json")
public class WaterTowerObject extends GameObject implements PlaceableObject, WaterContainer {

	protected long waterLevel;

	public WaterTowerObject(String str, Mesh mesh, Transform3D transform, boolean transparent, Vector3i objectId, byte materialId) {
		super(str, mesh, transform, transparent, objectId, materialId);
	}

	public WaterTowerObject(String str, Mesh mesh, Transform3D transform, boolean transparent) {
		super(str, mesh, transform, transparent);
	}

	public WaterTowerObject(String str, Mesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

	public WaterTowerObject(String str, Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public Vector2i getFootprint() {
		return new Vector2i(3, 3);
	}

	@Override
	public boolean isPlaceable(WorldLevelScene scene, TerrainMesh mesh, Vector2i tile, Direction rotation) {
		final int firstLevel = mesh.getCellHeight(tile.x, tile.y);

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (mesh.getCellHeight(tile.x + x, tile.y + y) != firstLevel) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void placeDown(WorldLevelScene scene, TerrainMesh mesh, Vector2i tile, Direction rotation) {

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
