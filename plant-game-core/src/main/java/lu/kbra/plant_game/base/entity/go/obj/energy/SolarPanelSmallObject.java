package lu.kbra.plant_game.base.entity.go.obj.energy;

import lu.kbra.plant_game.engine.entity.go.impl.EnergyContainer;
import lu.kbra.plant_game.engine.entity.go.impl.EnergyProducer;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableMeshGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/solar-panel-small.json")
public class SolarPanelSmallObject extends PlaceableMeshGameObject implements EnergyContainer, EnergyProducer {

	public SolarPanelSmallObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public float getProductionRate() {
		return 4.15f;
	}

	@Override
	public int getMaxEnergy() {
		return 50;
	}

}
