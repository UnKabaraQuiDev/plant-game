package lu.kbra.plant_game.engine.data.locale;

public interface Localizable {

	String getLocalizationKey();

	default String getLocalizationValue() {
		return LocalizationService.get(this.getLocalizationKey());
	}

}
