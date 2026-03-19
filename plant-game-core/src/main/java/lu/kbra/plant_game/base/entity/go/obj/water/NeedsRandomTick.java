package lu.kbra.plant_game.base.entity.go.obj.water;

import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface NeedsRandomTick {

	int DEFAULT_RANDOM_TICK_DURATION = 1000;

	void randomTick(final WindowInputHandler inputHandler, final WorldLevelScene worldLevelScene);

	default int getRandomTickDuration() {
		return DEFAULT_RANDOM_TICK_DURATION;
	}

}
