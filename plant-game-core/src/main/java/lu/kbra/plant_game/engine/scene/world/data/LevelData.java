package lu.kbra.plant_game.engine.scene.world.data;

import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.engine.scene.world.generator.ImageWorldGenerator;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerationStrategy;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public class LevelData {

	@JsonIgnore
	protected PluginDescriptor pluginDescriptor;
	@JsonIgnore
	protected String levelId;

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

			@Override
			public String toString() {
				return "WaterLevel@" + System.identityHashCode(this) + " [max=" + this.max + ", min=" + this.min + "]";
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

			@Override
			public String toString() {
				return "Light@" + System.identityHashCode(this) + " [color=" + this.color + ", ambient=" + this.ambient + ", direction="
						+ this.direction + "]";
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

			@Override
			public String toString() {
				return "Wind@" + System.identityHashCode(this) + " [strength=" + this.strength + ", direction=" + this.direction + "]";
			}

		}

		@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "strategy")
		@JsonSubTypes({ @JsonSubTypes.Type(value = ImageGeneration.class, name = "IMAGE") })
		sealed public abstract static class Generation permits ImageGeneration {

			protected WorldGenerationStrategy strategy;
			protected int height;

			public abstract WorldGenerator getGenerator(PluginDescriptor pluginDescriptor);

			@Override
			public String toString() {
				return "Generation@" + System.identityHashCode(this) + " [strategy=" + this.strategy + ", height=" + this.height + "]";
			}

		}

		public static final class ImageGeneration extends Generation {

			protected String path;
			protected String materialPath;

			@Override
			public WorldGenerator getGenerator(final PluginDescriptor pluginDescriptor) {
				return new ImageWorldGenerator(pluginDescriptor, this.path, this.height);
			}

			@Override
			public String toString() {
				return "ImageGeneration@" + System.identityHashCode(this) + " [path=" + this.path + ", materialPath=" + this.materialPath
						+ "]";
			}

		}

//		protected WorldGenerationStrategy generationStrategy;
		protected Generation generation;
		protected WaterLevel waterLevel;
		protected Light light;
		protected Wind wind;

		public Generation getGeneration() {
			return this.generation;
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

		@Override
		public String toString() {
			return "World@" + System.identityHashCode(this) + " [generation=" + this.generation + ", waterLevel=" + this.waterLevel
					+ ", light=" + this.light + ", wind=" + this.wind + "]";
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

			@Override
			public String toString() {
				return "BuildingOverride@" + System.identityHashCode(this) + " [price=" + this.price + ", unlockRequirements="
						+ this.unlockRequirements + ", buildRequirements=" + this.buildRequirements + "]";
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

		@Override
		public String toString() {
			return "Game@" + System.identityHashCode(this) + " [startResources=" + this.startResources + ", lockedBuildings="
					+ this.lockedBuildings + ", buildingsOverride=" + this.buildingsOverride + "]";
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

	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

	public void setPluginDescriptor(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	public String getLevelId() {
		return this.levelId;
	}

	public void setLevelId(final String levelId) {
		this.levelId = levelId;
	}

	public String getInternalName() {
		return this.pluginDescriptor.getInternalName() + "." + this.levelId;
	}

	public WorldGenerator getWorldGenerator() {
		return this.world.generation.getGenerator(this.pluginDescriptor);
	}

	@Override
	public String toString() {
		return "LevelData@" + System.identityHashCode(this) + " [pluginDescriptor=" + this.pluginDescriptor + ", levelId=" + this.levelId
				+ ", levelName=" + this.levelName + ", author=" + this.author + ", world=" + this.world + ", game=" + this.game + "]";
	}

}
