package lu.kbra.plant_game.engine.scene;

import lu.kbra.plant_game.engine.entity.GameObject;
import lu.kbra.plant_game.engine.entity.TerrainObject;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.scene.Scene3D;

public class WorldLevelScene extends Scene3D {

	private LevelData levelData;
	
	private CacheManager worldCache;

	private GameObject waterLevel;
	private TerrainObject terrain;

	public WorldLevelScene(String name, CacheManager parent) {
		super(name);
		this.worldCache = new CacheManager(name, parent);
	}

	public GameObject getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(GameObject waterLevel) {
		this.waterLevel = waterLevel;
		super.addEntity(waterLevel);
	}

	public GameObject getTerrain() {
		return terrain;
	}

	public void setTerrain(TerrainObject terrain) {
		this.terrain = terrain;
		super.addEntity(terrain);
	}

	public CacheManager getCache() {
		return worldCache;
	}

}
