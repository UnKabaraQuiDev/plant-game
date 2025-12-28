package lu.kbra.plant_game.engine.entity.go.obj.water;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.impl.WaterContainer;
import lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/water_tower.json")
public class WaterTowerObject extends PlaceableGameObject implements PlaceableObject, WaterContainer {

	protected long waterLevel;

	public WaterTowerObject(final String str, final Mesh mesh, final Transform3D transform, final Vector3i objectId) {
		super(str, mesh, transform, objectId);
	}

	public WaterTowerObject(final String str, final Mesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

	public WaterTowerObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	protected FootprintComputeMethod getFootprintComputeMethod() {
		return FootprintComputeMethod.CLOSEST;
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
