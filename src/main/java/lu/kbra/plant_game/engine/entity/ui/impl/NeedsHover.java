package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface NeedsHover {

	/**
	 * @return false if HoverState is LEAVING and this method shouldn't be called anymore
	 */
	boolean hover(final WindowInputHandler input, final HoverState hoverState);

}
