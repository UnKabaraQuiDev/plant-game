package lu.kbra.plant_game.engine.entity.go.obj.energy;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.EnergyContainer;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.go.obj.PlaceableGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/solar-panel-medium.json")
public class SolarPanelObject extends PlaceableGameObject implements PlaceableObject, EnergyContainer {

	protected long waterLevel;

	public SolarPanelObject(final String str, final Mesh mesh, final Transform3D transform, final Vector3i objectId) {
		super(str, mesh, transform, objectId);
	}

	public SolarPanelObject(final String str, final Mesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

	public SolarPanelObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public long getMaxPower() {
		return 1000;
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
