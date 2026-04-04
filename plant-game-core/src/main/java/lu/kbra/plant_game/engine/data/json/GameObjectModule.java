package lu.kbra.plant_game.engine.data.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import lu.kbra.pclib.concurrency.GenericTriggerLatch;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GOCreatingTaskFuture;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.plugin.registry.GameObjectRegistry;
import lu.kbra.standalone.gameengine.scene.EntityContainer;
import lu.kbra.standalone.gameengine.utils.json.PostDeserialize;

public class GameObjectModule extends SimpleModule {

	public GameObjectModule() {
		super.addDeserializer(GOCreatingTaskFuture.TaskState.class, new GameObjectTaskFutureStateDeserializer());
		super.addDeserializer(GOCreatingTaskFuture.class, new GameObjectTaskFutureDeserializer());
	}

	@Override
	public void setupModule(final SetupContext context) {
		context.addBeanSerializerModifier(new BeanSerializerModifier() {

			@Override
			public JsonSerializer<?> modifySerializer(
					final SerializationConfig config,
					final BeanDescription beanDesc,
					final JsonSerializer<?> serializer) {

				if (GameObject.class.isAssignableFrom(beanDesc.getBeanClass())) {
					return new GameObjectSerializer((JsonSerializer<Object>) serializer);
				}

				return serializer;
			}
		});
	}

	@Deprecated
	public static class GameObjectTaskFutureStateDeserializer
			extends JsonDeserializer<GOCreatingTaskFuture<? extends GameObject>.TaskState<? extends GameObject>> {

		@Deprecated
		@Override
		public GOCreatingTaskFuture<? extends GameObject>.TaskState<? extends GameObject> deserialize(
				final JsonParser p,
				final DeserializationContext ctxt) throws IOException {

			final EntityContainer<? extends GameObject> parent = (EntityContainer<? extends GameObject>) ctxt.getAttribute("parent");

			final IntPointer count = (IntPointer) ctxt.getAttribute("count");
			if (count != null) {
				count.increment();
			}

			final GenericTriggerLatch<?> latch = (GenericTriggerLatch<?>) ctxt.getAttribute("latch");

			return ctxt.readValue(p, GOCreatingTaskFuture.class).add(parent).latch(latch).push();
		}

	}

	public static class GameObjectTaskFutureDeserializer extends JsonDeserializer<GOCreatingTaskFuture<?>> {

		@Override
		public GOCreatingTaskFuture<? extends GameObject> deserialize(final JsonParser p, final DeserializationContext ctxt)
				throws IOException {
			final ObjectMapper mapper = (ObjectMapper) p.getCodec();
			final ObjectNode root = mapper.readTree(p);

			final String type = root.get("_type").asText();

			final Class<? extends GameObject> clazz = GameObjectRegistry.findInternalName(type);
			final JsonNode dataRoot = root.get("_data");

			return GameObjectFactory.create(clazz).set((i) -> {
				try {
					mapper.readerForUpdating(i).readValue(dataRoot);
				} catch (final IOException e) {
					throw new RuntimeException("Exception while updating: " + type + " = " + clazz.getName(), e);
				}
			}).postInit(i -> {
				if (i instanceof final PostDeserialize pd) {
					pd.postDeserialize();
				}
			});
		}

	}

	public static class GameObjectSerializer extends JsonSerializer<GameObject> {

		protected JsonSerializer<Object> delegate;

		public GameObjectSerializer(final JsonSerializer<Object> delegate) {
			this.delegate = delegate;
		}

		@Override
		public void serialize(final GameObject obj, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeStartObject();

			gen.writeStringField("_type", GameObjectRegistry.getInternalName((Class) obj.getClass()));

			gen.writeFieldName("_data");
			this.delegate.serialize(obj, gen, serializers);

			gen.writeEndObject();
		}

	}

}
