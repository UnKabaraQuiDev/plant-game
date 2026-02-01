package lu.kbra.plant_game;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lu.kbra.plant_game.base.scene.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.building.ResourceLineUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.stat_line.integer.FixedIntegerStatLine;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.GameData;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public class BuildingDefinition<T extends GameObject & PlaceableObject> implements Consumer<BuildingInfoUIObjectGroup> {

	protected final Class<T> clazz;
	protected final String internalName;
	protected final Map<ResourceType, Integer> prices;
	protected final List<BuildingRequirement> unlockRequirements;
	protected final List<BuildingRequirement> buildingRequirements;
	protected final int index;

	public BuildingDefinition(final Class<T> clazz, final String internalName, final Map<ResourceType, Integer> prices,
			final List<BuildingRequirement> unlockRequirements, final List<BuildingRequirement> buildingRequirements, final int index) {
		this.clazz = clazz;
		this.internalName = internalName;
		this.prices = prices;
		this.unlockRequirements = unlockRequirements;
		this.buildingRequirements = buildingRequirements;
		this.index = index;
	}

	public boolean isUnlocked(final GameData gameData, final WorldLevelScene worldLevelScene) {
		return this.unlockRequirements.stream().allMatch(c -> c.isFulfilled(worldLevelScene));
	}

	public boolean canBuild(final GameData gameData, final WorldLevelScene worldLevelScene) {
		return this.getPrices().entrySet().stream().allMatch(e -> gameData.getResources().get(e.getKey()) > e.getValue())
				&& this.buildingRequirements.stream().allMatch(c -> c.isFulfilled(worldLevelScene));
	}

	@Override
	public void accept(final BuildingInfoUIObjectGroup t) {
		t.getContent()
				.getROEntities()
				.stream()
				.filter(ResourceLineUIObjectGroup.class::isInstance)
				.map(ResourceLineUIObjectGroup.class::cast)
				.forEach(b -> {
					if (this.prices.containsKey(b.getResourceType())) {
						b.stream()
								.filter(FixedIntegerStatLine.class::isInstance)
								.map(FixedIntegerStatLine.class::cast)
								.forEach(f -> f.set(this.prices.get(b.getResourceType())).flushValue());
					} else {
						b.stream()
								.filter(FixedIntegerStatLine.class::isInstance)
								.map(FixedIntegerStatLine.class::cast)
								.forEach(f -> f.set(0).flushValue());
					}
				});
	}

	public String getInternalName() {
		return this.internalName;
	}

	public Class<T> getClazz() {
		return this.clazz;
	}

	public Map<ResourceType, Integer> getPrices() {
		return this.prices;
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
		return "BuildingDefinition@" + System.identityHashCode(this) + " [clazz=" + this.clazz + ", prices=" + this.prices
				+ ", unlockRequirements=" + this.unlockRequirements + ", buildingRequirements=" + this.buildingRequirements + ", index="
				+ this.index + "]";
	}

}
