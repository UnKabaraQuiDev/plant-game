package lu.kbra.plant_game.base.entity.go.obj.energy;

import lu.kbra.plant_game.engine.entity.go.impl.EnergyContainer;
import lu.kbra.plant_game.engine.entity.go.impl.EnergyProducer;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableMeshGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/wind-turbine-medium.json")
public class WindTurbineMediumObject extends PlaceableMeshGameObject implements EnergyContainer, EnergyProducer {

	public WindTurbineMediumObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public float getProductionRate() {
		return 1;
	}

	@Override
	public int getMaxEnergy() {
		return 25;
	}

}
