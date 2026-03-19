package lu.kbra.plant_game.base.entity.go.obj.water;

import lu.kbra.plant_game.engine.entity.go.impl.WaterContainer;
import lu.kbra.plant_game.engine.entity.go.obj.FootprintComputeMethod;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableMeshGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/water-tower-medium.json")
public class WaterTowerMediumObject extends PlaceableMeshGameObject implements WaterContainer {

	public WaterTowerMediumObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	protected FootprintComputeMethod getFootprintComputeMethod() {
		return FootprintComputeMethod.CLOSEST;
	}

	@Override
	public int getMaxWater() {
		return 500;
	}

}
