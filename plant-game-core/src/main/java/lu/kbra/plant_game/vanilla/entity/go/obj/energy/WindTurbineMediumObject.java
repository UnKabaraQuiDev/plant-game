package lu.kbra.plant_game.vanilla.entity.go.obj.energy;

import lu.kbra.plant_game.engine.entity.go.impl.EnergyContainer;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableMeshGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/wind-turbine-medium.json")
public class WindTurbineMediumObject extends PlaceableMeshGameObject implements PlaceableObject, EnergyContainer {

	public WindTurbineMediumObject(final String str, final Mesh mesh) {
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
