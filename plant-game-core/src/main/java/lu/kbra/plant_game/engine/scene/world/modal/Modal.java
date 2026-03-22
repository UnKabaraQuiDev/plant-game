package lu.kbra.plant_game.engine.scene.world.modal;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;

public interface Modal extends ParentAwareNode {

	void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState);

	void update(
			final WindowInputHandler inputHandler,
			final DeferredCompositor compositor,
			final Dispatcher workers,
			final Dispatcher renderDispatcher);

	void render(final float dTime);

	void cancel();

	void start();

	void stop();

}
