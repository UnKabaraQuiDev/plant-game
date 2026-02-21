package lu.kbra.plant_game.engine.data.locale;

public class LocalizationString extends AbstractLocalizationString {

	private final Localizable key;

	public LocalizationString(final String key) {
		this.key = Localizable.of(key);
	}

	public LocalizationString(final Localizable key) {
		this.key = key;
	}

	@Override
	public LocalizationString addParam(final Localizable param) {
		super.addParam(param);
		return this;
	}

	@Override
	public String getLocalizationKey() {
		return this.key.getLocalizationKey();
	}

	@Override
	public String toString() {
		return "LocalizationString@" + System.identityHashCode(this) + " [key=" + this.key + ", params=" + this.params + ", computed="
				+ this.computed + "]";
	}

}
