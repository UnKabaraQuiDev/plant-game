package lu.kbra.plant_game.engine.window.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.graph.window.Window;

public interface WindowInputHandler {

	Vector2f getMousePosition();

	Vector2f getNormalizedMousePosition();

	GameEngine getGameEngine();
	
	Window getWindow();

	Vector2i getWindowSize();

	void onFrameBegin();

	KeyState getKeyState(int code);

	KeyState getButtonState(int code);

	boolean isKeyHeld(int code);

	boolean isKeyPressed(int code);

	boolean isKeyRepeat(int code);

	boolean isMouseButtonPressed(int code);

	boolean isKeyPressedOnce(int code);

	boolean isMouseButtonPressedOnce(int code);

	boolean isKeyPressedOrRepeat(int code);

	Vector2d getMouseScroll();

	Thread getOwner();

	void setOwner(Thread owner);

	boolean isOwnerThread();

	void checkOwnerThread();

}
