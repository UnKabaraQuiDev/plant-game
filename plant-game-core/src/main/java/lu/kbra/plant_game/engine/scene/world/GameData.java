package lu.kbra.plant_game.engine.scene.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.engine.scene.world.data.resource.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public class GameData {

	protected Map<ResourceType, Integer> resources = Collections.synchronizedMap(new HashMap<>());

	public GameData(final Map<ResourceType, Integer> resources) {
		this.resources = resources;
	}

	public GameData() {
		this.resources.put(DefaultResourceType.WATER, 0);
		this.resources.put(DefaultResourceType.ENERGY, 0);
		this.resources.put(DefaultResourceType.MONEY, 0);
	}

	public Map<ResourceType, Integer> getResources() {
		return this.resources;
	}

	public void buyBuilding(final BuildingDefinition<?> buildingDefinition) {
		this.resources.compute(DefaultResourceType.MONEY, (k, v) -> v - buildingDefinition.getPrice());
	}

}
