package lu.kbra.plant_game.engine.scene.world.data;

import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONObject;

public class LevelData {

	static class World {

		static class WaterLevel {

			protected float max;
			protected float min;

		}

		static class Light {

			protected Vector4f color;
			protected float ambient;
			protected Vector3f direction;

		}

		static class Wind {

			protected float strength;
			protected Vector2f direction;

		}

		protected WorldGenerationStrategy worldGenerationStrategy;
		protected JSONObject worldGenerationData;
		protected WaterLevel waterLevel;
		protected Light light;
		protected Wind wind;

	}

	static class Game {

		static class StartResources {

			protected int water;
			protected int energy;
			protected int money;

		}

		static class BuildingOverride {

			protected int price;
			protected List<BuildingRequirement> unlockRequirements;
			protected List<BuildingRequirement> buildRequirements;

		}

		protected StartResources startResources;
		protected List<String> lockedBuildings;
		protected Map<String, BuildingOverride> offeredBuildings;

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
