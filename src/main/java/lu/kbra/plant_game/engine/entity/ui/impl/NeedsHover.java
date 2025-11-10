package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.kbra.plant_game.engine.entity.ui.HoverState;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface NeedsHover {

	void hover(final WindowInputHandler input, final float dTime, final HoverState hoverState);

}
