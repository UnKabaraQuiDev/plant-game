package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.scene.Scene;

public interface NeedsClick {

	void click(final WindowInputHandler input, final float dTime, final Scene scene);

}
