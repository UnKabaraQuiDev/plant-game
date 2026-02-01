package lu.kbra.plant_game.base.entity.go.obj.water;

import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.impl.WaterContainer;
import lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableMeshGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/water-tower-medium.json")
public class WaterTowerObject extends PlaceableMeshGameObject implements PlaceableObject, WaterContainer {

	protected long waterLevel;

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
