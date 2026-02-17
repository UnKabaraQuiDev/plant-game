package lu.kbra.plant_game.base.entity.go.obj.water;

import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface NeedsRandomTick {

	void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene);

}
