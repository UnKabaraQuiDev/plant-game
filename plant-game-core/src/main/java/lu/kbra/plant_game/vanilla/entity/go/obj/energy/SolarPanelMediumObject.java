package lu.kbra.plant_game.vanilla.entity.go.obj.energy;

import lu.kbra.plant_game.engine.entity.go.impl.EnergyContainer;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableMeshGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@Deprecated
@DataPath("classpath:/models/solar-panel-medium.json")
public class SolarPanelMediumObject extends PlaceableMeshGameObject implements PlaceableObject, EnergyContainer {

	@Deprecated
	protected long waterLevel;

	@Deprecated
	public SolarPanelMediumObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Deprecated
	@Override
	public long getMaxPower() {
		return 1000;
	}

	@Deprecated
	@Override
	public void getCurrentPower() {

	}

	@Deprecated
	@Override
	public void removePower(final long power) {

	}

	@Deprecated
	@Override
	public void addPower(final long power) {

	}

}
