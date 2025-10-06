package lu.kbra.plant_game.engine.entity.electric;

import org.joml.Vector2i;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.DataPath;
import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.scene.WorldLevelScene;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/solar_panel.json")
public class SolarPanelObject extends GameObject implements PlaceableObject, ElectricityContainer {

	protected long waterLevel;

	public SolarPanelObject(String str, Mesh mesh, Transform3D transform, Vector3i objectId) {
		super(str, mesh, transform, false, objectId);
	}

	public SolarPanelObject(String str, Mesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

	public SolarPanelObject(String str, Mesh mesh) {
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
	public long getMaxPower() {
		return 1000;
	}

	@Override
	public void getCurrentPower() {

	}

	@Override
	public void removePower(long power) {

	}

	@Override
	public void addPower(long power) {

	}

}
