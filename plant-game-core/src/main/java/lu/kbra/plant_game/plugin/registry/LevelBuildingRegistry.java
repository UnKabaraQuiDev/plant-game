package lu.kbra.plant_game.plugin.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingCategory;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingDefinition;

public class LevelBuildingRegistry {

	protected final Map<BuildingCategory, List<BuildingDefinition<?>>> buildingDefinitions;

	public LevelBuildingRegistry(final Map<BuildingCategory, List<BuildingDefinition<?>>> buildingDefinitions) {
		this.buildingDefinitions = buildingDefinitions;
	}

	public LevelBuildingRegistry(final LevelData ld) {
		this.buildingDefinitions = new ConcurrentHashMap<>();

		BuildingRegistry.BUILDING_DEFS.forEach((k, registered) -> {
			if (registered.isEmpty()) {
				return;
			}

			final List<BuildingDefinition<?>> list = this.buildingDefinitions.computeIfAbsent(k, k2 -> new ArrayList<>());
			registered.forEach(bd -> {
				if (ld.getGame().getLockedBuildings().stream().anyMatch(rgx -> bd.getInternalName().matches(rgx))) {
					return;
				}
				final BuildingDefinition<?> newBd = bd.clone();
				ld.getGame()
						.getBuildingsOverride()
						.entrySet()
						.stream()
						.filter((e) -> newBd.getInternalName().matches(e.getKey()))
						.forEach(bo -> {
							newBd.getPrices().putAll(bo.getValue().getPrices());
							newBd.getBuildingRequirements().addAll(bo.getValue().getBuildRequirements());
							newBd.getUnlockRequirements().addAll(bo.getValue().getUnlockRequirements());
						});
				list.add(newBd);
			});

			if (list.isEmpty()) {
				this.buildingDefinitions.remove(k);
			}
		});
	}

	public Map<BuildingCategory, List<BuildingDefinition<?>>> getBuildingDefinitions() {
		return this.buildingDefinitions;
	}

	@Override
	public String toString() {
		return "LevelBuildingRegistry@" + System.identityHashCode(this) + " [buildingDefinitions=" + this.buildingDefinitions + "]";
	}

}
