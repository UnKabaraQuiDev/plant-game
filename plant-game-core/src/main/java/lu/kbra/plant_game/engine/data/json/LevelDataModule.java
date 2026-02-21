package lu.kbra.plant_game.engine.data.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.emory.mathcs.backport.java.util.Collections;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.LevelData.Game.BuildingOverride;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public class LevelDataModule extends SimpleModule {

	public LevelDataModule() {
		super.addDeserializer(BuildingOverride.class, new BuildingOverrideDeserializer());
	}

	public static class BuildingOverrideDeserializer extends JsonDeserializer<BuildingOverride> {
		@Override
		public BuildingOverride deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
			final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
			final ObjectNode node = mapper.readTree(parser);

			final Map<ResourceType, Integer> prices;
			final List<BuildingRequirement> unlockRequirements;
			final List<BuildingRequirement> buildRequirements;

			// handle "prices"
			if (node.has("prices")) {
				final JavaType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, ResourceType.class, Integer.class);

				prices = mapper.convertValue(node.get("prices"), mapType);
			} else if (node.has("price")) {
				prices = new HashMap<>();

				final int value = node.get("price").asInt();
				prices.put(DefaultResourceType.MONEY, value);
			} else {
				prices = new HashMap<>();
			}

			// normal fields
			if (node.has("unlockRequirements")) {
				unlockRequirements = mapper.convertValue(node.get("unlockRequirements"),
						mapper.getTypeFactory().constructCollectionType(List.class, BuildingRequirement.class));
			} else {
				unlockRequirements = Collections.emptyList();
			}

			if (node.has("buildRequirements")) {
				buildRequirements = mapper.convertValue(node.get("buildRequirements"),
						mapper.getTypeFactory().constructCollectionType(List.class, BuildingRequirement.class));
			} else {
				buildRequirements = Collections.emptyList();
			}

			return new BuildingOverride(prices, unlockRequirements, buildRequirements);
		}
	}

}
