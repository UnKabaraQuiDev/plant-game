package lu.kbra.plant_game.engine.window.input;

import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;

public interface KeyOption extends IndexOwner {

	String LOCALIZATION_KEY = "key.";

	int getPhysicalKey();

	String name();

}
