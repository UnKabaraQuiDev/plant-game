package lu.kbra.plant_game.engine.scene.world.data.building;

import java.util.List;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;

public class BuildingDeclaration<T extends GameObject & PlaceableObject> {

	protected Class<T> clazz;

	protected int price;
	protected boolean unlocked = false;

	protected List<BuildingRequirement> buildRequirements;

	public BuildingDeclaration(final Class<T> clazz, final int price, final List<BuildingRequirement> buildRequirements) {
		this.clazz = clazz;
		this.price = price;
		this.buildRequirements = buildRequirements;
	}

	public BuildingDeclaration(final Class<T> clazz, final int price, final boolean unlocked,
			final List<BuildingRequirement> buildRequirements) {
		this.clazz = clazz;
		this.price = price;
		this.unlocked = unlocked;
		this.buildRequirements = buildRequirements;
	}

	public Class<T> getClazz() {
		return this.clazz;
	}

	public void setClazz(final Class<T> clazz) {
		this.clazz = clazz;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(final int price) {
		this.price = price;
	}

	public boolean isUnlocked() {
		return this.unlocked;
	}

	public void setUnlocked(final boolean unlocked) {
		this.unlocked = unlocked;
	}

	public List<BuildingRequirement> getBuildRequirements() {
		return this.buildRequirements;
	}

	public void setBuildRequirements(final List<BuildingRequirement> buildRequirements) {
		this.buildRequirements = buildRequirements;
	}

	@Override
	public String toString() {
		return "BuildingDeclaration@" + System.identityHashCode(this) + " [clazz=" + this.clazz + ", price=" + this.price + ", unlocked="
				+ this.unlocked + ", buildRequirements=" + this.buildRequirements + "]";
	}

}
