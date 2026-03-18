package lu.kbra.plant_game.engine.data.locale;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@FunctionalInterface
public interface Localizable {

	Map<String, Localizable> CACHED = new HashMap<>();

	String getLocalizationKey();

	default String getLocalizationValue() {
		return LocalizationService.get(this.getLocalizationKey());
	}

	static Localizable raw(final Object raw) {
		return new Localizable() {
			final String r = Objects.toString(raw);

			@Override
			public String getLocalizationKey() {
				return "raw";
			}

			@Override
			public String getLocalizationValue() {
				return r;
			}
		};
	}

	static Localizable of(final String key) {
		return CACHED.computeIfAbsent(key, (k) -> () -> k);
	}

	static ComposedLocalizable composed(final String key, final Localizable... values) {
		return composed(Localizable.of(key), values);
	}

	static ComposedLocalizable composed(final Localizable key, final Localizable... values) {
		final LocalizationString ls = new LocalizationString(key);
		for (Localizable l : values) {
			ls.addParam(l);
		}
		return ls;
	}

	static ComposedLocalizable composed(final String key, final String... values) {
		return composed(Localizable.of(key), values);
	}

	static ComposedLocalizable composed(final Localizable key, final String... values) {
		final LocalizationString ls = new LocalizationString(key);
		for (String l : values) {
			ls.addParam(Localizable.of(l));
		}
		return ls;
	}

}
