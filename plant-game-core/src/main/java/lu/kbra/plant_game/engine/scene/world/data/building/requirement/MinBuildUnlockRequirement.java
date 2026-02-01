package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lu.kbra.plant_game.BuildingRegistry;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.scene.world.ActiveModalController;

@JsonTypeName("MIN_BUILD")
public class MinBuildUnlockRequirement implements BuildingRequirement {

	private int count;
	private String building;

	public MinBuildUnlockRequirement() {
	}

	public MinBuildUnlockRequirement(final int count, final String building) {
		this.count = count;
		this.building = building;
	}

	public MinBuildUnlockRequirement(final int count, final Class<? extends GameObject> building) {
		this.count = count;
		this.building = BuildingRegistry.getInternalObjectName(building);
	}

	public int getCount() {
		return this.count;
	}

	public String getBuilding() {
		return this.building;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public void setBuilding(final String building) {
		this.building = building;
	}

	@Override
	public boolean isFulfilled(final ActiveModalController scene) {
		return false;
	}

}
