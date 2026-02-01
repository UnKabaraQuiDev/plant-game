package lu.kbra.plant_game.base.scene.menu.main;

import lu.kbra.plant_game.engine.entity.ui.impl.Focusable;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface NeedsFocusInput extends Focusable {

	/**
	 * @return false to stop give up on the focus
	 */
	boolean focusInput(WindowInputHandler inputHandler);

}
