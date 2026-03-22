package lu.kbra.plant_game.engine.data.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lu.kbra.plant_game.plugin.PluginDescriptor.InternalDependencies.VersionnedPluginDescriptor;
import lu.kbra.plant_game.plugin.VersionMatcher;
import lu.kbra.plant_game.plugin.VersionMatcher.BoundsVersionMatcher;
import lu.kbra.plant_game.plugin.VersionMatcher.BoundsVersionMatcher.BoundsDirection;
import lu.kbra.plant_game.plugin.VersionMatcher.RangeVersionMatcher;
import lu.kbra.plant_game.plugin.VersionMatcher.StrictVersionMatcher;

public final class VersionMatcherModule extends SimpleModule {

	public VersionMatcherModule() {
		this.addSerializer(VersionnedPluginDescriptor.class, new VersionnedPluginDescriptorSerializer());
		this.addDeserializer(VersionnedPluginDescriptor.class, new VersionnedPluginDescriptorDeserializer());
	}

	static final class VersionnedPluginDescriptorSerializer extends JsonSerializer<VersionnedPluginDescriptor> {

		@Override
		public void serialize(final VersionnedPluginDescriptor value, final JsonGenerator gen, final SerializerProvider serializers)
				throws IOException {

			String out = value.getInternalName() + ":" + this.serializeMatcher(value.getVersion());
			gen.writeString(out);
		}

		private String serializeMatcher(final VersionMatcher matcher) {
			return switch (matcher) {
			case StrictVersionMatcher s -> s.getTarget();
			case BoundsVersionMatcher b -> (b.getDir() == BoundsVersionMatcher.BoundsDirection.LOWER ? ">" : "<") + b.getTarget();
			case RangeVersionMatcher r -> "[" + r.getTargetLower() + ", " + r.getTargetUpper() + "]";
			case null, default -> throw new IllegalStateException("Unknown matcher: " + matcher.getClass());
			};
		}
	}

	static final class VersionnedPluginDescriptorDeserializer extends JsonDeserializer<VersionnedPluginDescriptor> {

		@Override
		public VersionnedPluginDescriptor deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {

			String raw = p.getValueAsString();
			int sep = raw.indexOf(':');

			if (sep < 0) {
				throw new JsonParseException(p, "Missing ':' in version string");
			}

			String name = raw.substring(0, sep).trim();
			String expr = raw.substring(sep + 1).trim();

			VersionMatcher matcher = this.parseMatcher(expr);

			VersionnedPluginDescriptor d = new VersionnedPluginDescriptor(name, matcher);

			return d;
		}

		private VersionMatcher parseMatcher(final String expr) {
			if (expr.startsWith(">")) {
				final BoundsVersionMatcher m = new BoundsVersionMatcher(expr.substring(1), BoundsDirection.LOWER);
				return m;
			}

			if (expr.startsWith("<")) {
				final BoundsVersionMatcher m = new BoundsVersionMatcher(expr.substring(1), BoundsDirection.UPPER);
				return m;
			}

			if (expr.startsWith(">=")) {
				final BoundsVersionMatcher m = new BoundsVersionMatcher(expr.substring(2), BoundsDirection.LOWER_INCL);
				return m;
			}

			if (expr.startsWith("<=")) {
				final BoundsVersionMatcher m = new BoundsVersionMatcher(expr.substring(2), BoundsDirection.UPPER_INCL);
				return m;
			}

			if (expr.startsWith("[") && expr.endsWith("]")) {
				final String[] parts = expr.substring(1, expr.length() - 1).split(",");
				if (parts.length != 2) {
					throw new IllegalArgumentException("Invalid range: " + expr);
				}

				final RangeVersionMatcher m = new RangeVersionMatcher(parts[0].trim(), parts[1].trim());
				return m;
			}

			final StrictVersionMatcher m = new StrictVersionMatcher(expr);
			return m;
		}
	}
}
