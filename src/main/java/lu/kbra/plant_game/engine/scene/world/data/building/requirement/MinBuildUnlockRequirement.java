package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;

@JsonTypeName("MIN_BUILD")
public class MinBuildUnlockRequirement implements BuildingRequirement {

	private int count;
	private String building;

	public MinBuildUnlockRequirement() {
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
	public boolean isFulfilled(final WorldLevelScene scene) {
		return false;
	}

}
