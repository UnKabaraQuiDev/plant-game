package lu.kbra.plant_game;

import org.lwjgl.glfw.GLFW;

import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.graph.window.Window;

public class InputHandler {

	private final Window window;

	private final boolean[] prevKeyPressed = new boolean[GLFW.GLFW_KEY_LAST + 1];
	private final boolean[] prevMousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

	public InputHandler(Window window) {
		this.window = window;
	}

	public boolean isKeyPressedOnce(int code) {
		final KeyState state = window.getKeyState(code);
		final boolean pressed = (state == KeyState.PRESS || state == KeyState.REPEAT);

		final boolean pressedOnce = pressed && !prevKeyPressed[code];
		prevKeyPressed[code] = pressed;
		return pressedOnce;
	}

	public boolean isMouseButtonPressedOnce(int code) {
		final KeyState state = window.getMouseButtonState(code);
		final boolean pressed = (state == KeyState.PRESS || state == KeyState.REPEAT);

		final boolean pressedOnce = pressed && !prevMousePressed[code];
		prevMousePressed[code] = pressed;
		return pressedOnce;
	}

}
