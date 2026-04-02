package lu.kbra.plant_game.engine.data.json;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GOCreatingTaskFuture;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.plugin.registry.GameObjectRegistry;
import lu.kbra.standalone.gameengine.utils.json.PostDeserialize;

public class GameObjectModule extends SimpleModule {

	public GameObjectModule() {
		super.addDeserializer(GOCreatingTaskFuture.TaskState.class, new GameObjectTaskFutureStateDeserializer());
		super.addDeserializer(GOCreatingTaskFuture.class, new GameObjectTaskFutureDeserializer());
		super.addSerializer(GameObject.class, new GameObjectSerializer());
	}

	public class GameObjectTaskFutureStateDeserializer
			extends JsonDeserializer<GOCreatingTaskFuture<? extends GameObject>.TaskState<? extends GameObject>> {

		@Override
		public GOCreatingTaskFuture<? extends GameObject>.TaskState<? extends GameObject> deserialize(
				final JsonParser p,
				final DeserializationContext ctxt) throws IOException {
			return ctxt.readValue(p, GOCreatingTaskFuture.class).push();
		}

	}

	public class GameObjectTaskFutureDeserializer extends JsonDeserializer<GOCreatingTaskFuture<? extends GameObject>> {

		@Override
		public GOCreatingTaskFuture<? extends GameObject> deserialize(final JsonParser p, final DeserializationContext ctxt)
				throws IOException {
			final ObjectMapper mapper = (ObjectMapper) p.getCodec();
			final ObjectNode root = mapper.readTree(p);

			final String type = root.get("_type").asText();

			final Class<? extends GameObject> clazz = GameObjectRegistry.findInternalName(type);

			return GameObjectFactory.create(clazz).set((i) -> {
				try {
					mapper.readerForUpdating(i).readValue(root);
				} catch (final IOException e) {
					throw new RuntimeException("Exception while updating: " + type + " = " + clazz.getName(), e);
				}
			}).postInit(i -> {
				if (i instanceof PostDeserialize pd) {
					pd.postDeserialize();
				}
			});
		}

	}

	public class GameObjectSerializer extends JsonSerializer<GameObject> {

		@Override
		public void serialize(final GameObject obj, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartObject();

			final String type = GameObjectRegistry.getInternalName((Class) obj.getClass());
			gen.writeStringField("_type", type);

			final ObjectMapper mapper = (ObjectMapper) gen.getCodec();

			final ObjectNode node = mapper.valueToTree(obj);

			node.remove("_type");

			for (Map.Entry<String, JsonNode> entry : node.properties()) {
				gen.writeFieldName(entry.getKey());
				gen.writeTree(entry.getValue());
			}

			gen.writeEndObject();
		}
	}

}
