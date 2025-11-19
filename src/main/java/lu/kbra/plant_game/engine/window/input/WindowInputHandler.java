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

	default boolean isKeyHeld(final KeyOption code) {
		return this.isKeyHeld(code.getPhysicalKey());
	}

	boolean isKeyHeld(int code);

	default boolean isKeyPressed(final KeyOption code) {
		return this.isKeyPressed(code.getPhysicalKey());
	}

	boolean isKeyPressed(int code);

	default boolean isKeyRepeat(final KeyOption code) {
		return this.isKeyRepeat(code.getPhysicalKey());
	}

	boolean isKeyRepeat(int code);

	default boolean isMouseButtonPressed(final KeyOption code) {
		return this.isMouseButtonPressed(code.getPhysicalKey());
	}

	boolean isMouseButtonPressed(int code);

	default boolean isKeyPressedOnce(final KeyOption code) {
		return this.isKeyPressedOnce(code.getPhysicalKey());
	}

	boolean isKeyPressedOnce(int code);

	default boolean isMouseButtonPressedOnce(final KeyOption code) {
		return this.isMouseButtonPressedOnce(code.getPhysicalKey());
	}

	boolean isMouseButtonPressedOnce(int code);

	default boolean isKeyPressedOrRepeat(final KeyOption code) {
		return this.isKeyPressedOrRepeat(code.getPhysicalKey());
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

	default String getButtonName(final KeyOption code) {
		return this.getButtonName(code.getPhysicalKey());
	}

	String getButtonName(int code);

	default String getKeyName(final KeyOption code) {
		return this.getKeyName(code.getPhysicalKey());
	}

	String getKeyName(int code);

	default String getInputName(final KeyOption code) {
		return this.getInputName(code.getPhysicalKey());
	}

	String getInputName(int code);

	int getPressedMouse();

	int getPressedKey();

	boolean hasPressedMouse();

	boolean hasPressedKey();

}
