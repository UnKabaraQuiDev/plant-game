package lu.kbra.plant_game.engine.window.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.graph.window.Window;

public class DefaultInputHandler implements WindowInputHandler {

	public static final int GLFW_KEY_FIRST = GLFW.GLFW_KEY_SPACE;
	public static final int GLFW_MOUSE_FIRST = GLFW.GLFW_MOUSE_BUTTON_1;

	public static final String[] MOUSE_BUTTON_NAMES = {
			"Left",
			"Right",
			"Middle",
			"Button 3",
			"Button 4",
			"Button 5",
			"Button 6",
			"Button 7" };

	protected final GameEngine engine;
	protected final Window window;

	protected final boolean[] prevKeyPressed = new boolean[GLFW.GLFW_KEY_LAST - GLFW_KEY_FIRST + 1];
	protected final boolean[] currentKeyPressed = new boolean[GLFW.GLFW_KEY_LAST - GLFW_KEY_FIRST + 1];

	protected final boolean[] prevMousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST - GLFW_MOUSE_FIRST + 1];
	protected final boolean[] currentMousePressed = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST - GLFW_MOUSE_FIRST + 1];

	protected Thread owner;

	protected boolean windowResized;
	protected Vector2i oldSize = new Vector2i();
	protected Vector2f normalizedMousePosition = new Vector2f();
	protected Vector2d mouseScroll = new Vector2d();
	protected Vector2f mousePosition = new Vector2f();
	protected Character character;

	public DefaultInputHandler(final GameEngine engine) {
		this.engine = engine;
		this.window = engine.getWindow();
	}

	@Override
	public void onFrameBegin() {
		this.checkOwnerThread();

		System.arraycopy(this.currentKeyPressed, 0, this.prevKeyPressed, 0, this.prevKeyPressed.length);
		System.arraycopy(this.currentMousePressed, 0, this.prevMousePressed, 0, this.prevMousePressed.length);

		for (int i = 0; i < this.currentKeyPressed.length; i++) {
			final KeyState state = this.getKeyState(i);
			this.currentKeyPressed[i] = (state == KeyState.PRESS || state == KeyState.REPEAT);
		}
		for (int i = 0; i < this.currentMousePressed.length; i++) {
			final KeyState state = this.getButtonState(i);
			this.currentMousePressed[i] = (state == KeyState.PRESS || state == KeyState.REPEAT);
		}

		// set normalized mouse position
		final Vector2i windowSize = this.getWindowSize();
		final Vector2f mousePosition = this.getMousePosition();

		final float ndcX = (2.0f * mousePosition.x) / windowSize.x - 1.0f;
		final float ndcY = 1.0f - (2.0f * mousePosition.y) / windowSize.y; // flip y

		this.normalizedMousePosition.set(ndcX, ndcY);
		// --------------- -------------

		this.mouseScroll.set(this.window.getScroll());
		this.window.clearScroll();

		this.windowResized = this.oldSize.x != this.window.getSize().x || this.oldSize.y != this.window.getSize().y;
		this.oldSize.set(this.window.getSize());
		mousePosition.set(this.window.getMousePosition());

		this.character = this.window.getCharacter();
		this.window.clearCharacter();
	}

	@Override
	public boolean isKeyPressedOnce(final int code) {
		this.checkOwnerThread();

		final boolean pressed = this.isKeyHeld(code);

		final boolean pressedOnce = pressed && !this.prevKeyPressed[code];
		this.prevKeyPressed[code] = pressed;

		return pressedOnce;
	}

	@Override
	public boolean isKeyPressedOrRepeat(final int code) {
		this.checkOwnerThread();

		final KeyState state = this.getKeyState(code);

		final boolean pressedOrRepeat = (state == KeyState.PRESS && !this.prevKeyPressed[code]) || state == KeyState.REPEAT;
		this.prevKeyPressed[code] = (state == KeyState.PRESS || state == KeyState.REPEAT);

		return pressedOrRepeat;
	}

	@Override
	public boolean isMouseButtonPressedOnce(final int code) {
		this.checkOwnerThread();

		final boolean pressed = this.isMouseButtonPressed(code);

		final boolean pressedOnce = pressed && !this.prevMousePressed[code];
		this.prevMousePressed[code] = pressed;

		return pressedOnce;
	}

	@Override
	public KeyState getKeyState(final int code) {
		return this.window.getKeyState(code);
	}

	@Override
	public boolean isKeyPressed(final int code) {
		final KeyState state = this.getKeyState(code);
		return state == KeyState.PRESS;
	}

	@Override
	public boolean isKeyHeld(final int code) {
		final KeyState state = this.getKeyState(code);
		return state == KeyState.PRESS || state == KeyState.REPEAT;
	}

	@Override
	public boolean isKeyRepeat(final int code) {
		final KeyState state = this.getButtonState(code);
		return state == KeyState.REPEAT;
	}

	@Override
	public KeyState getButtonState(final int code) {
		return this.window.getMouseButtonState(code);
	}

	@Override
	public boolean isMouseButtonPressed(final int code) {
		final KeyState state = this.getButtonState(code);
		return state == KeyState.PRESS;
	}

	@Override
	public Vector2f getMousePosition() {
		this.checkOwnerThread();

		return this.mousePosition;
	}

	@Override
	public Vector2f getNormalizedMousePosition() {
		this.checkOwnerThread();

		return this.normalizedMousePosition;
	}

	@Override
	public Window getWindow() {
		return this.window;
	}

	@Override
	public Vector2i getWindowSize() {
		return this.window.getSize();
	}

	@Override
	public String getKeyName(final int code) {
		return GLFW.glfwGetKeyName(code, 0);
	}

	@Override
	public String getButtonName(final int code) {
		if (code >= 0 && code < MOUSE_BUTTON_NAMES.length) {
			return MOUSE_BUTTON_NAMES[code];
		}
		return null;
	}

	@Override
	public String getInputName(final int code) {
		if (code >= GLFW_MOUSE_FIRST && code <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
			return MOUSE_BUTTON_NAMES[code - GLFW_MOUSE_FIRST];
		}
		if (code >= GLFW_KEY_FIRST && code <= GLFW.GLFW_KEY_LAST) {
			return GLFW.glfwGetKeyName(code, 0);
		}

		return null;
	}

	@Override
	public Vector2d getMouseScroll() {
		return this.mouseScroll;
	}

	@Override
	public Thread getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(final Thread owner) {
		this.owner = owner;
	}

	@Override
	public boolean isOwnerThread() {
		if (this.owner == null) {
			return true;
		}
		return this.owner == Thread.currentThread();
	}

	@Override
	public void checkOwnerThread() {
		assert this.isOwnerThread() : "Thread: " + Thread.currentThread() + " not owner (" + this.owner + ")";
	}

	@Override
	public GameEngine getGameEngine() {
		return this.engine;
	}

	@Override
	public boolean wasResized() {
		return this.windowResized;
	}

	@Override
	public boolean hasPressedKey() {
		for (final boolean b : this.currentKeyPressed) {
			if (b) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasPressedMouse() {
		for (final boolean b : this.currentMousePressed) {
			if (b) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getPressedKey() {
		for (int i = 0; i < this.currentKeyPressed.length; i++) {
			if (this.currentKeyPressed[i]) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getPressedMouse() {
		for (int i = 0; i < this.currentMousePressed.length; i++) {
			if (this.currentMousePressed[i]) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public char getPressedKeyChar() {
		return this.character == null ? '\0' : this.character;
	}

	@Override
	public boolean hasPressedKeyChar() {
		return this.character != null;
	}

}
