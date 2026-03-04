package lu.kbra.plant_game.engine.entity.factory;

import java.util.Objects;

/**
 * Cache key for resolved objects.
 */
public record DIKey(Class<?> type, String qualifier, String dataPath) {

	public DIKey {
		Objects.requireNonNull(type);
		qualifier = qualifier == null ? "" : qualifier;
		dataPath = dataPath == null ? "" : dataPath;
	}

	public static DIKey of(final Class<?> type) {
		return new DIKey(type, "", "");
	}

	public DIKey withQualifier(final String q) {
		return new DIKey(this.type, q, this.dataPath);
	}

	public DIKey withDataPath(final String p) {
		return new DIKey(this.type, this.qualifier, p);
	}
}
