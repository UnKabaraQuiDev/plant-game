package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface NeedsBoundsInput {

	/**
	 * @return true if the event shouldn't be passed down
	 */
	boolean boundsInput(final WindowInputHandler inputHandler);

}
