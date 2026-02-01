package lu.kbra.plant_game.engine.scene.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;
import lu.kbra.plant_game.engine.scene.world.data.resource.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public class GameData {

	protected LevelData levelData;
	protected float currentWaterLevel;
	protected Map<ResourceType, Integer> resources = Collections.synchronizedMap(new HashMap<>());

	public GameData(final Map<ResourceType, Integer> resources) {
		this.resources = resources;
	}

	public GameData(final LevelData levelData) {
		this.levelData = levelData;
		this.resources.put(DefaultResourceType.WATER, 0);
		this.resources.put(DefaultResourceType.ENERGY, 0);
		this.resources.put(DefaultResourceType.MONEY, 0);
	}

	public LevelData getLevelData() {
		return this.levelData;
	}

	public Map<ResourceType, Integer> getResources() {
		return this.resources;
	}

	public void buyBuilding(final BuildingDefinition<?> buildingDefinition) {
		buildingDefinition.getPrices().forEach((k, v) -> this.resources.compute(k, (k2, v2) -> v2 - v));
	}

	public void setCurrentWaterLevel(final float currentWaterLevel) {
		this.currentWaterLevel = currentWaterLevel;
	}

	public float getCurrentWaterLevel() {
		return this.currentWaterLevel;
	}

	public static GameData fromBlankLevel(final LevelData levelData) {
		final GameData gd = new GameData(levelData);
		gd.setCurrentWaterLevel(levelData.getWorld().getWaterLevel().getMin());
		levelData.getGame().getStartResources().forEach((k, v) -> gd.getResources().put(k, v));
		return gd;
	}

}
