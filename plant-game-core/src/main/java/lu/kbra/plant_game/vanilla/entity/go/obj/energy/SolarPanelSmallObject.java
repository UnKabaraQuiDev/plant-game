package lu.kbra.plant_game.vanilla.entity.go.obj.energy;

import lu.kbra.plant_game.engine.entity.go.impl.EnergyContainer;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableMeshGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/solar-panel-small.json")
public class SolarPanelSmallObject extends PlaceableMeshGameObject implements PlaceableObject, EnergyContainer {

	public SolarPanelSmallObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public long getMaxPower() {
		return 100;
	}

	@Override
	public void getCurrentPower() {

	}

	@Override
	public void removePower(final long power) {

	}

	@Override
	public void addPower(final long power) {

	}

}
