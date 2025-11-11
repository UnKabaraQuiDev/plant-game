package lu.kbra.plant_game.engine.window.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.graph.window.Window;

public class DefaultInputHandler implements WindowInputHandler {

	protected final GameEngine engine;
	protected final Window window;

	protected final boolean[] prevKeyPressed = new boolean[GLFW.GLFW_KEY_LAST + 1];
	protected final boolean[] currentKeyPressed = new boolean[GLFW.GLFW_KEY_LAST + 1];

	protected final boolean[] prevMousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
	protected final boolean[] currentMousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

	protected Thread owner;

	protected boolean windowResized;
	protected Vector2i oldSize = new Vector2i();
	protected Vector2f normalizedMousePosition = new Vector2f();
	protected Vector2d mouseScroll = new Vector2d();
	protected Vector2f mousePosition = new Vector2f();

	public DefaultInputHandler(GameEngine engine) {
		this.engine = engine;
		this.window = engine.getWindow();
	}

	@Override
	public void onFrameBegin() {
		checkOwnerThread();

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

		// set normalized mouse position
		final Vector2i windowSize = getWindowSize();
		final Vector2f mousePosition = getMousePosition();

		final float ndcX = (2.0f * mousePosition.x) / windowSize.x - 1.0f;
		final float ndcY = 1.0f - (2.0f * mousePosition.y) / windowSize.y; // flip y

		normalizedMousePosition.set(ndcX, ndcY);
		// --------------- -------------

		mouseScroll.set(window.getScroll());
		window.clearScroll();

		windowResized = oldSize.x != window.getSize().x || oldSize.y != window.getSize().y;
		oldSize.set(window.getSize());
		mousePosition.set(window.getMousePosition());
	}

	@Override
	public boolean isKeyPressedOnce(int code) {
		checkOwnerThread();

		final boolean pressed = isKeyHeld(code);

		final boolean pressedOnce = pressed && !prevKeyPressed[code];
		prevKeyPressed[code] = pressed;

		return pressedOnce;
	}

	@Override
	public boolean isKeyPressedOrRepeat(int code) {
		checkOwnerThread();

		final KeyState state = getKeyState(code);

		final boolean pressedOrRepeat = (state == KeyState.PRESS && !prevKeyPressed[code]) || state == KeyState.REPEAT;
		prevKeyPressed[code] = (state == KeyState.PRESS || state == KeyState.REPEAT);

		return pressedOrRepeat;
	}

	@Override
	public boolean isMouseButtonPressedOnce(int code) {
		checkOwnerThread();

		final boolean pressed = isMouseButtonPressed(code);

		final boolean pressedOnce = pressed && !prevMousePressed[code];
		prevMousePressed[code] = pressed;

		return pressedOnce;
	}

	@Override
	public Vector2f getMousePosition() {
		checkOwnerThread();

		return mousePosition;
	}

	@Override
	public Vector2f getNormalizedMousePosition() {
		checkOwnerThread();

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

	@Override
	public Vector2d getMouseScroll() {
		return mouseScroll;
	}

	@Override
	public Thread getOwner() {
		return owner;
	}

	@Override
	public void setOwner(Thread owner) {
		this.owner = owner;
	}

	@Override
	public boolean isOwnerThread() {
		if (owner == null)
			return true;
		return owner == Thread.currentThread();
	}

	@Override
	public void checkOwnerThread() {
		assert isOwnerThread() : "Thread: " + Thread.currentThread() + " not owner (" + owner + ")";
	}

	@Override
	public GameEngine getGameEngine() {
		return engine;
	}

	@Override
	public boolean wasResized() {
		return windowResized;
	}

}
