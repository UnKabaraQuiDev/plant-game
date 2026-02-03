package lu.kbra.plant_game.engine.data.json;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.plugin.registry.ResourceRegistry;

public class ResourceTypeModule extends SimpleModule {

	public ResourceTypeModule() {
		this.addKeySerializer(ResourceType.class, new ResourceTypeSerializer());
		this.addKeyDeserializer(ResourceType.class, new ResourceTypeKeyDeserializer());
		this.addSerializer(ResourceType.class, new ResourceTypeSerializer());
		this.addDeserializer(ResourceType.class, new ResourceTypeDeserializer());
	}

	static final class ResourceTypeKeyDeserializer extends StdKeyDeserializer {

		public ResourceTypeKeyDeserializer() {
			super(TYPE_CLASS, ResourceType.class);
		}

		@Override
		public Object deserializeKey(final String key, final DeserializationContext ctxt) throws IOException {
			ResourceType resourceType = ResourceRegistry.RESOURCE_TYPE_DEFS.get(key);
			if (resourceType == null) {
				throw new IllegalStateException("Unsupported type: " + key);
			}

			return resourceType;
		}

	}

	static final class ResourceTypeDeserializer extends JsonDeserializer<ResourceType> {

		@Override
		public ResourceType deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
			String key = jp.getValueAsString();
			ResourceType resourceType = ResourceRegistry.RESOURCE_TYPE_DEFS.get(key);
			if (resourceType == null) {
				throw new IllegalArgumentException("Unsupported type: " + key);
			}
			return resourceType;
		}
	}

	static final class ResourceTypeSerializer extends JsonSerializer<ResourceType> {

		@Override
		public void serialize(final ResourceType value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
			for (Map.Entry<String, ResourceType> entry : ResourceRegistry.RESOURCE_TYPE_DEFS.entrySet()) {
				if (Objects.equals(value, entry.getValue())) {
					gen.writeString(entry.getKey());
					return;
				}
			}
			throw new IllegalArgumentException("Unsupported ResourceType instance. Make sure it's registered in RESOURCE_TYPE_DEFS");
		}
	}

}
