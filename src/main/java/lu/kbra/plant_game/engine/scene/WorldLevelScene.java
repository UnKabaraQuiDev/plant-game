package lu.kbra.plant_game.engine.scene;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.entity.terrain.TerrainObject;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.scene.Scene3D;

public class WorldLevelScene extends Scene3D {

	private LevelData levelData;

	private CacheManager worldCache;

	private GameObject waterLevel;
	private TerrainObject terrain;

	private Vector3f lightColor = new Vector3f(1), lightDirection = new Vector3f(0.5f, 0.5f, 0.5f).normalize();
	private float ambientLight = 0.25f;

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

	public Vector3f getLightColor() {
		return lightColor;
	}

	public void setLightColor(Vector3f lightColor) {
		this.lightColor = lightColor;
	}

	public Vector3f getLightDirection() {
		return lightDirection;
	}

	public void setLightDirection(Vector3f lightDirection) {
		this.lightDirection = lightDirection;
	}

	public float getAmbientLight() {
		return ambientLight;
	}

	public void setAmbientLight(float ambientLight) {
		this.ambientLight = ambientLight;
	}

}
