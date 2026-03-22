package lu.kbra.plant_game.engine.scene.world.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.Math;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingDefinition;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public class GameData {

	@JsonIgnore
	protected LevelData levelData;

	protected byte progress = 0;
	protected LevelState levelState = LevelState.NOT_STARTED;
	protected Map<ResourceType, Float> resources = new ConcurrentHashMap<>();

	public GameData() {
	}

	public GameData(final Map<ResourceType, Float> resources) {
		this.resources = resources;
	}

	public GameData(final LevelData levelData) {
		this.levelData = levelData;
		this.resources.put(DefaultResourceType.WATER, 0f);
		this.resources.put(DefaultResourceType.ENERGY, 0f);
		this.resources.put(DefaultResourceType.MONEY, 0f);
	}

	public LevelData getLevelData() {
		return this.levelData;
	}

	public Map<ResourceType, Float> getResources() {
		return this.resources;
	}

	public void buyBuilding(final BuildingDefinition<?> buildingDefinition) {
		buildingDefinition.getPrices().forEach((k, v) -> this.resources.compute(k, (k2, v2) -> v2 - v));
	}

	public float getCurrentWaterLevel() {
		return Math.lerp(this.levelData.getWorld().getWaterLevel().getMin(),
				this.levelData.getWorld().getWaterLevel().getMax(),
				this.progress / 100f);
	}

	public byte getProgress() {
		return this.progress;
	}

	public void setProgress(final byte progress) {
		this.progress = progress;
	}

	public LevelState getLevelState() {
		return this.levelState;
	}

	public void setLevelState(final LevelState levelState) {
		this.levelState = levelState;
	}

	public void setLevelData(final LevelData levelData) {
		this.levelData = levelData;
	}

	public static GameData fromBlankLevel(final LevelData levelData) {
		final GameData gd = new GameData(levelData);
		levelData.getGame().getStartResources().forEach((k, v) -> gd.getResources().put(k, (float) v));
		return gd;
	}

	@Override
	public String toString() {
		return "GameData@" + System.identityHashCode(this) + " [levelData=" + this.levelData + ", progress=" + this.progress
				+ ", levelState=" + this.levelState + ", resources=" + this.resources + "]";
	}

}
