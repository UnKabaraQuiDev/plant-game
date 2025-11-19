package lu.kbra.plant_game.engine.window.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

import lu.kbra.plant_game.engine.scene.ui.KeyOption;
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

	default boolean isKeyHeld(KeyOption code) {
		return isKeyHeld(code.getPhysicalKey());
	}

	boolean isKeyHeld(int code);

	default boolean isKeyPressed(KeyOption code) {
		return isKeyPressed(code.getPhysicalKey());
	}

	boolean isKeyPressed(int code);

	default boolean isKeyRepeat(KeyOption code) {
		return isKeyRepeat(code.getPhysicalKey());
	}

	boolean isKeyRepeat(int code);

	default boolean isMouseButtonPressed(KeyOption code) {
		return isMouseButtonPressed(code.getPhysicalKey());
	}

	boolean isMouseButtonPressed(int code);

	default boolean isKeyPressedOnce(KeyOption code) {
		return isKeyPressedOnce(code.getPhysicalKey());
	}

	boolean isKeyPressedOnce(int code);

	default boolean isMouseButtonPressedOnce(KeyOption code) {
		return isMouseButtonPressedOnce(code.getPhysicalKey());
	}

	boolean isMouseButtonPressedOnce(int code);

	default boolean isKeyPressedOrRepeat(KeyOption code) {
		return isKeyPressedOrRepeat(code.getPhysicalKey());
	}

	boolean isKeyPressedOrRepeat(int code);

	Vector2d getMouseScroll();

	Thread getOwner();

	void setOwner(Thread owner);

	boolean isOwnerThread();

	void checkOwnerThread();

	boolean wasResized();

	char getPressedKeyChar();

	boolean hasPressedKeyChar();

	default String getButtonName(KeyOption code) {
		return getButtonName(code.getPhysicalKey());
	}

	String getButtonName(int code);

	default String getKeyName(KeyOption code) {
		return getKeyName(code.getPhysicalKey());
	}

	String getKeyName(int code);

	default String getMappedInputName(KeyOption code) {
		return getMappedInputName(code.getPhysicalKey());
	}

	String getMappedInputName(int code);

	int getPressedMouse();

	int getPressedKey();

	boolean hasPressedMouse();

	boolean hasPressedKey();

}
