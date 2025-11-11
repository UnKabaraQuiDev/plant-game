package lu.kbra.plant_game.engine.entity.ui;

import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.scene.Scene;

public interface NeedsInput {

	void input(final WindowInputHandler inputHandler, final float dTime, final Scene scene);

}
