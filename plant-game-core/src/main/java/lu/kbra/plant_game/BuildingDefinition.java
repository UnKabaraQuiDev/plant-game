package lu.kbra.plant_game;

import java.util.List;
import java.util.function.Consumer;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.DefaultResourceType;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.ResourceLineUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.stat_line.integer.FixedIntegerStatLine;

public class BuildingDefinition<T extends GameObject & PlaceableObject> implements Consumer<BuildingInfoUIObjectGroup> {

	protected final Class<T> clazz;
	protected final int price;
	protected final List<BuildingRequirement> unlockRequirements;
	protected final List<BuildingRequirement> buildingRequirements;
	protected final int index;

	public BuildingDefinition(final Class<T> clazz, final int price, final List<BuildingRequirement> unlockRequirements,
			final List<BuildingRequirement> buildingRequirements, final int index) {
		this.clazz = clazz;
		this.price = price;
		this.unlockRequirements = unlockRequirements;
		this.buildingRequirements = buildingRequirements;
		this.index = index;
	}

	public boolean isUnlocked(final WorldLevelScene world) {
		return this.unlockRequirements.stream().allMatch(c -> c.isFulfilled(world));
	}

	public boolean canBuild(final WorldLevelScene world) {
		return PGLogic.INSTANCE.getGameData().getResources().get(DefaultResourceType.MONEY) > this.getPrice()
				&& this.buildingRequirements.stream().allMatch(c -> c.isFulfilled(world));
	}

	@Override
	public void accept(final BuildingInfoUIObjectGroup t) {
		t.getContent()
				.getROEntities()
				.stream()
				.filter(ResourceLineUIObjectGroup.class::isInstance)
				.map(ResourceLineUIObjectGroup.class::cast)
				.forEach(b -> {
					if (b.getResourceType() == DefaultResourceType.MONEY) {
						b.stream()
								.filter(FixedIntegerStatLine.class::isInstance)
								.map(FixedIntegerStatLine.class::cast)
								.forEach(f -> f.set(this.price).flushValue());
					} else {
						b.stream()
								.filter(FixedIntegerStatLine.class::isInstance)
								.map(FixedIntegerStatLine.class::cast)
								.forEach(f -> f.set(0).flushValue());
					}
				});
	}

	public String getInternalName() {
		return BuildingDefinitionRegistry.getInternalObjectName(this.clazz);
	}

	public Class<T> getClazz() {
		return this.clazz;
	}

	public int getPrice() {
		return this.price;
	}

	public List<BuildingRequirement> getUnlockRequirements() {
		return this.unlockRequirements;
	}

	public List<BuildingRequirement> getBuildingRequirements() {
		return this.buildingRequirements;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public String toString() {
		return "BuildingDefinition@" + System.identityHashCode(this) + " [clazz=" + this.clazz + ", price=" + this.price
				+ ", unlockRequirements=" + this.unlockRequirements + ", buildingRequirements=" + this.buildingRequirements + ", index="
				+ this.index + "]";
	}

}
