package lu.kbra.plant_game.engine.entity;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.scene.Scene3D;

public class WorldLevelScene extends Scene3D {

	private CacheManager worldCache;

	private GameObject waterLevel;
	private GameObject terrain;

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

	public void setTerrain(GameObject terrain) {
		this.terrain = terrain;
		super.addEntity(terrain);
	}

	public CacheManager getWorldCache() {
		return worldCache;
	}

}
