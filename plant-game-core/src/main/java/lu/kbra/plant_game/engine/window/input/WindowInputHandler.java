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

	void onFrameBegin(float dTime);

	float dTime();

	/**
	 * @param code GLFW code
	 */
	KeyState getKeyState(int code);

	/**
	 * @param code GLFW code
	 */
	KeyState getButtonState(int code);

	default boolean isKeyHeld(final KeyOption code) {
		return this.isKeyHeld(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	boolean isKeyHeld(int code);

	default boolean isKeyPressed(final KeyOption code) {
		return this.isKeyPressed(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	boolean isKeyPressed(int code);

	default boolean isKeyRepeat(final KeyOption code) {
		return this.isKeyRepeat(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	boolean isKeyRepeat(int code);

	default boolean isMouseButtonPressed(final KeyOption code) {
		return this.isMouseButtonPressed(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	boolean isMouseButtonPressed(int code);

	default boolean isKeyPressedOnce(final KeyOption code) {
		return this.isKeyPressedOnce(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	boolean isKeyPressedOnce(int code);

	default boolean isMouseButtonPressedOnce(final KeyOption code) {
		return this.isMouseButtonPressedOnce(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	boolean isMouseButtonPressedOnce(int code);

	default boolean isKeyPressedOrRepeat(final KeyOption code) {
		return this.isKeyPressedOrRepeat(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	boolean isKeyPressedOrRepeat(int code);

	Vector2d getMouseScroll();

	Thread getOwner();

	void setOwner(Thread owner);

	default boolean isOwnerThread() {
		if (this.getOwner() == null) {
			return true;
		}
		return this.getOwner() == Thread.currentThread();
	}

	default void checkOwnerThread() {
		assert this.isOwnerThread() : Thread.currentThread().getName() + " isn't owner !";
	}

	boolean wasResized();

	char getPressedKeyChar();

	boolean hasPressedKeyChar();

	default String getButtonName(final KeyOption code) {
		return this.getButtonName(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	String getButtonName(int code);

	default String getKeyName(final KeyOption code) {
		return this.getKeyName(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	String getKeyName(int code);

	default String getInputName(final KeyOption code) {
		return this.getInputName(code.getPhysicalKey());
	}

	/**
	 * @param code GLFW code
	 */
	String getInputName(int code);

	int getPressedMouse();

	int getPressedKey();

	boolean hasPressedMouse();

	boolean hasPressedKey();

	boolean hasPressedMouseOnce();

	boolean hasPressedKeyOnce();

}
