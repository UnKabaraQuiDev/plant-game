package lu.kbra.plant_game.engine.scene.world.data;

import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONObject;

import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerationStrategy;

public class LevelData {

	public static class World {

		public static class WaterLevel {

			protected float max;
			protected float min;

			public float getMax() {
				return this.max;
			}

			public float getMin() {
				return this.min;
			}

		}

		public static class Light {

			protected Vector3f color;
			protected float ambient;
			protected Vector3f direction;

			public Vector3f getColor() {
				return this.color;
			}

			public float getAmbient() {
				return this.ambient;
			}

			public Vector3f getDirection() {
				return this.direction;
			}

		}

		public static class Wind {

			protected float strength;
			protected Vector2f direction;

			public float getStrength() {
				return this.strength;
			}

			public Vector2f getDirection() {
				return this.direction;
			}

		}

		protected WorldGenerationStrategy generationStrategy;
		protected JSONObject generationData;
		protected WaterLevel waterLevel;
		protected Light light;
		protected Wind wind;

		public JSONObject getGenerationData() {
			return this.generationData;
		}

		public WorldGenerationStrategy getGenerationStrategy() {
			return this.generationStrategy;
		}

		public WaterLevel getWaterLevel() {
			return this.waterLevel;
		}

		public Light getLight() {
			return this.light;
		}

		public Wind getWind() {
			return this.wind;
		}

	}

	public static class Game {

		public static class BuildingOverride {

			protected int price;
			protected List<BuildingRequirement> unlockRequirements;
			protected List<BuildingRequirement> buildRequirements;

			public int getPrice() {
				return this.price;
			}

			public List<BuildingRequirement> getUnlockRequirements() {
				return this.unlockRequirements;
			}

			public List<BuildingRequirement> getBuildRequirements() {
				return this.buildRequirements;
			}

		}

		protected Map<ResourceType, Integer> startResources;
		protected List<String> lockedBuildings;
		protected Map<String, BuildingOverride> buildingsOverride;

		public Map<ResourceType, Integer> getStartResources() {
			return this.startResources;
		}

		public List<String> getLockedBuildings() {
			return this.lockedBuildings;
		}

		public Map<String, BuildingOverride> getBuildingsOverride() {
			return this.buildingsOverride;
		}

	}

	protected String levelName;
	protected String author;
	protected World world;
	protected Game game;

	public LevelData() {
	}

	public LevelData(final String levelName, final String author, final World world) {
		this.levelName = levelName;
		this.author = author;
		this.world = world;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public String getAuthor() {
		return this.author;
	}

	public World getWorld() {
		return this.world;
	}

	public Game getGame() {
		return this.game;
	}

}
