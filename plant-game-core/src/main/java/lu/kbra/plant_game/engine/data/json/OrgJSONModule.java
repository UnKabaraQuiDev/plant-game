package lu.kbra.plant_game.engine.data.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public final class OrgJSONModule extends SimpleModule {

	public OrgJSONModule() {
		super("OrgJSONModule");

		this.addSerializer(JSONObject.class, new JSONObjectSerializer());
		this.addDeserializer(JSONObject.class, new JSONObjectDeserializer());

		this.addSerializer(JSONArray.class, new JSONArraySerializer());
		this.addDeserializer(JSONArray.class, new JSONArrayDeserializer());
	}

	public class JSONObjectSerializer extends JsonSerializer<JSONObject> {
		@Override
		public void serialize(final JSONObject value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeObject(value.toMap());
		}
	}

	public class JSONArraySerializer extends JsonSerializer<JSONArray> {
		@Override
		public void serialize(final JSONArray value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
			gen.writeObject(value.toList());
		}
	}

	public class JSONObjectDeserializer extends JsonDeserializer<JSONObject> {
		@Override
		public JSONObject deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final Map<?, ?> map = p.readValueAs(Map.class);
			return new JSONObject(map);
		}
	}

	public class JSONArrayDeserializer extends JsonDeserializer<JSONArray> {
		@Override
		public JSONArray deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
			final List<?> list = p.readValueAs(List.class);
			return new JSONArray(list);
		}
	}

}
