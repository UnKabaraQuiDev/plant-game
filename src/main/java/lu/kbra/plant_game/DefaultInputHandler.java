package lu.kbra.plant_game;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.engine.entity.impl.WindowInputHandler;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.graph.window.Window;

public class DefaultInputHandler implements WindowInputHandler {

	protected final Window window;

	protected final boolean[] prevKeyPressed = new boolean[GLFW.GLFW_KEY_LAST + 1];
	protected final boolean[] currentKeyPressed = new boolean[GLFW.GLFW_KEY_LAST + 1];

	protected final boolean[] prevMousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
	protected final boolean[] currentMousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

	protected Vector2f normalizedMousePosition = new Vector2f();

	public DefaultInputHandler(Window window) {
		this.window = window;
	}

	@Override
	public void onFrameBegin() {
		System.arraycopy(currentKeyPressed, 0, prevKeyPressed, 0, prevKeyPressed.length);
		System.arraycopy(currentMousePressed, 0, prevMousePressed, 0, prevMousePressed.length);

		for (int i = 0; i <= GLFW.GLFW_KEY_LAST; i++) {
			KeyState state = getKeyState(i);
			currentKeyPressed[i] = (state == KeyState.PRESS || state == KeyState.REPEAT);
		}
		for (int i = 0; i <= GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			KeyState state = getButtonState(i);
			currentMousePressed[i] = (state == KeyState.PRESS || state == KeyState.REPEAT);
		}
	}

	@Override
	public boolean isKeyPressedOnce(int code) {
		final boolean pressed = isKeyHeld(code);

		final boolean pressedOnce = pressed && !prevKeyPressed[code];
		prevKeyPressed[code] = pressed;

		return pressedOnce;
	}

	@Override
	public boolean isKeyPressedOrRepeat(int code) {
		final KeyState state = getKeyState(code);

		final boolean pressedOrRepeat = (state == KeyState.PRESS && !prevKeyPressed[code]) || state == KeyState.REPEAT;
		prevKeyPressed[code] = (state == KeyState.PRESS || state == KeyState.REPEAT);

		return pressedOrRepeat;
	}

	@Override
	public boolean isMouseButtonPressedOnce(int code) {
		final boolean pressed = isMouseButtonPressed(code);

		final boolean pressedOnce = pressed && !prevMousePressed[code];
		prevMousePressed[code] = pressed;

		return pressedOnce;
	}

	@Override
	public Vector2f getMousePosition() {
		return window.getMousePosition();
	}

	@Override
	public Vector2f getNormalizedMousePosition() {
		final Vector2i windowSize = getWindowSize();
		final Vector2f mousePosition = getMousePosition();

		final float ndcX = (2.0f * mousePosition.x) / windowSize.x - 1.0f;
		final float ndcY = 1.0f - (2.0f * mousePosition.y) / windowSize.y; // flip y

		normalizedMousePosition.set(ndcX, ndcY);

		return normalizedMousePosition;
	}

	@Override
	public Window getWindow() {
		return window;
	}

	@Override
	public Vector2i getWindowSize() {
		return window.getSize();
	}

	@Override
	public KeyState getKeyState(int code) {
		return window.getKeyState(code);
	}

	@Override
	public boolean isKeyPressed(int code) {
		final KeyState state = getKeyState(code);
		return state == KeyState.PRESS;
	}

	@Override
	public boolean isKeyHeld(int code) {
		final KeyState state = getKeyState(code);
		return state == KeyState.PRESS || state == KeyState.REPEAT;
	}

	@Override
	public boolean isKeyRepeat(int code) {
		final KeyState state = getButtonState(code);
		return state == KeyState.REPEAT;
	}

	@Override
	public KeyState getButtonState(int code) {
		return window.getMouseButtonState(code);
	}

	@Override
	public boolean isMouseButtonPressed(int code) {
		final KeyState state = getButtonState(code);
		return state == KeyState.PRESS;
	}

}
