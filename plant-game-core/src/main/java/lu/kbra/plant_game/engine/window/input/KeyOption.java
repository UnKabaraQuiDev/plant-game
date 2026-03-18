package lu.kbra.plant_game.engine.window.input;

import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;

public interface KeyOption extends Localizable, IndexOwner {

	String LOCALIZATION_KEY = "key.";

	int getPhysicalKey();

	String name();

	@Override
	default String getLocalizationKey() {
		return LOCALIZATION_KEY + name().toLowerCase();
	}

}
