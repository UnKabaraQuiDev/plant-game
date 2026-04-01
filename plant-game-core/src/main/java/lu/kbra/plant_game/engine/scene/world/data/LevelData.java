package lu.kbra.plant_game.engine.scene.world.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lu.kbra.plant_game.base.entity.go.obj.StarterPodSmallObject;
import lu.kbra.plant_game.engine.data.json.LevelDataModule.BuildingOverrideDeserializer;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.engine.scene.world.generator.ImageWorldGenerator;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerationStrategy;
import lu.kbra.plant_game.engine.scene.world.generator.WorldGenerator;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.registry.LevelBuildingRegistry;
import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class LevelData {

	@JsonIgnore
	protected PluginDescriptor pluginDescriptor;
	@JsonIgnore
	protected String levelId;
	@JsonIgnore
	protected LevelBuildingRegistry buildingRegistry;

	public static class World {

		public static class StarterPod {

			protected String podClass = StarterPodSmallObject.class.getName();
			protected Vector2i tile;
			protected Direction direction = Direction.DEFAULT;

			public String getPodClass() {
				return this.podClass;
			}

			public Vector2i getTile() {
				return this.tile;
			}

			public Direction getDirection() {
				return this.direction;
			}

		}

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
			@JsonInclude(JsonInclude.Include.NON_NULL)
			protected String materialPath;
			@JsonSetter(nulls = Nulls.SKIP)
			protected boolean colorMaterial = false;

			@Override
			public WorldGenerator getGenerator(final PluginDescriptor pluginDescriptor) {
				return this.materialPath != null
						? new ImageWorldGenerator(pluginDescriptor, this.path, this.materialPath, this.colorMaterial, this.height)
						: new ImageWorldGenerator(pluginDescriptor, this.path, this.height);
			}

			@Override
			public String toString() {
				return "ImageGeneration@" + System.identityHashCode(this) + " [path=" + this.path + ", materialPath=" + this.materialPath
						+ "]";
			}

		}

		protected Generation generation;
		protected WaterLevel waterLevel;
		protected StarterPod starterPod;
		protected Light light;
		protected Wind wind;

		public Generation getGeneration() {
			return this.generation;
		}

		public WaterLevel getWaterLevel() {
			return this.waterLevel;
		}

		public StarterPod getStarterPod() {
			return this.starterPod;
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
					+ ", starterPod=" + this.starterPod + ", light=" + this.light + ", wind=" + this.wind + "]";
		}

	}

	public static class Game {

		@JsonDeserialize(using = BuildingOverrideDeserializer.class)
		public static class BuildingOverride {

			protected Map<ResourceType, Integer> prices;
			protected List<BuildingRequirement> unlockRequirements;
			protected List<BuildingRequirement> buildRequirements;

			public BuildingOverride() {
			}

			public BuildingOverride(
					final Map<ResourceType, Integer> prices,
					final List<BuildingRequirement> unlockRequirements,
					final List<BuildingRequirement> buildRequirements) {
				this.prices = prices;
				this.unlockRequirements = unlockRequirements;
				this.buildRequirements = buildRequirements;
			}

			public Map<ResourceType, Integer> getPrices() {
				return this.prices;
			}

			public List<BuildingRequirement> getUnlockRequirements() {
				return this.unlockRequirements;
			}

			public List<BuildingRequirement> getBuildRequirements() {
				return this.buildRequirements;
			}

			@Override
			public String toString() {
				return "BuildingOverride@" + System.identityHashCode(this) + " [prices=" + this.prices + ", unlockRequirements="
						+ this.unlockRequirements + ", buildRequirements=" + this.buildRequirements + "]";
			}

		}

		protected Map<ResourceType, Integer> startResources;
		protected Set<String> lockedBuildings;
		protected Map<String, BuildingOverride> buildingsOverride;

		public Map<ResourceType, Integer> getStartResources() {
			return this.startResources;
		}

		public Set<String> getLockedBuildings() {
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

	public LevelBuildingRegistry getBuildingRegistry() {
		if (this.buildingRegistry == null) {
			this.createBuildingRegistry();
		}
		return this.buildingRegistry;
	}

	private void createBuildingRegistry() {
		this.buildingRegistry = new LevelBuildingRegistry(this);
	}

	@Override
	public String toString() {
		return "LevelData@" + System.identityHashCode(this) + " [pluginDescriptor=" + this.pluginDescriptor + ", levelId=" + this.levelId
				+ ", buildingRegistry=" + this.buildingRegistry + ", levelName=" + this.levelName + ", author=" + this.author + ", world="
				+ this.world + ", game=" + this.game + "]";
	}

}
