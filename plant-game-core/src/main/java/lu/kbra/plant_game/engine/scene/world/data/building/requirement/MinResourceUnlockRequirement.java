package lu.kbra.plant_game.engine.scene.world.data.building.requirement;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lu.kbra.plant_game.engine.scene.world.ActiveModalController;

@JsonTypeName("MIN_RESOURCE")
public class MinResourceUnlockRequirement implements BuildingRequirement {

	private int count;
	private String resource;

	public MinResourceUnlockRequirement() {
	}

	public int getCount() {
		return this.count;
	}

	public String getResource() {
		return this.resource;
	}

	public void setCount(final int count) {
		this.count = count;
	}

	public void setResource(final String resource) {
		this.resource = resource;
	}

	@Override
	public boolean isFulfilled(final ActiveModalController scene) {
		return false;
	}

}
