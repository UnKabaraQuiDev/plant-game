package lu.kbra.plant_game.engine.window.input;

import java.util.Arrays;

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

	private final GameEngine engine;
	private final Window window;

	private final boolean[] prevKeys;
	private final boolean[] currKeys;

	private final boolean[] prevMouse;
	private final boolean[] currMouse;

	private Thread owner;

	private final Vector2f mousePosition = new Vector2f();
	private final Vector2f normalizedMousePosition = new Vector2f();
	private final Vector2d mouseScroll = new Vector2d();

	private final Vector2i oldSize = new Vector2i();
	private boolean resized;

	private Character pressedChar;

	private float dTime;

	public DefaultInputHandler(final GameEngine engine) {
		this.engine = engine;
		this.window = engine.getWindow();

		this.prevKeys = new boolean[GLFW.GLFW_KEY_LAST + 1];
		this.currKeys = new boolean[GLFW.GLFW_KEY_LAST + 1];

		this.prevMouse = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
		this.currMouse = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
	}

	@Override
	public void onFrameBegin(final float dTime) {
		this.checkOwnerThread();
		this.dTime = dTime;

		System.arraycopy(this.currKeys, 0, this.prevKeys, 0, this.currKeys.length);
		System.arraycopy(this.currMouse, 0, this.prevMouse, 0, this.currMouse.length);

		for (int i = 0; i < this.currKeys.length; i++) {
			final KeyState s = this.window.getKeyState(i);
			this.currKeys[i] = (s == KeyState.PRESS || s == KeyState.REPEAT);
		}

		for (int i = 0; i < this.currMouse.length; i++) {
			final KeyState s = this.window.getMouseButtonState(i);
			this.currMouse[i] = (s == KeyState.PRESS || s == KeyState.REPEAT);
		}

		final Vector2i size = this.window.getSize();
		final Vector2f pos = this.window.getMousePosition();

		this.mousePosition.set(pos);

		final float nx = (2f * pos.x) / size.x - 1f;
		final float ny = 1f - (2f * pos.y) / size.y;
		this.normalizedMousePosition.set(nx, ny);

		this.mouseScroll.set(this.window.getScroll());
		this.window.clearScroll();

		this.resized = size.x != this.oldSize.x || size.y != this.oldSize.y;
		this.oldSize.set(size);

		this.pressedChar = this.window.getCharacter();
		this.window.clearCharacter();
	}

	@Override
	public float dTime() {
		return this.dTime;
	}

	@Override
	public KeyState getKeyState(final int code) {
		return this.window.getKeyState(code);
	}

	@Override
	public KeyState getButtonState(final int code) {
		return this.window.getMouseButtonState(code);
	}

	@Override
	public boolean isKeyHeld(final int code) {
		final KeyState s = this.getKeyState(code);
		return s == KeyState.PRESS || s == KeyState.REPEAT;
	}

	@Override
	public boolean isKeyPressed(final int code) {
		return this.getKeyState(code) == KeyState.PRESS;
	}

	@Override
	public boolean isKeyRepeat(final int code) {
		return this.getKeyState(code) == KeyState.REPEAT;
	}

	@Override
	public boolean isMouseButtonPressed(final int code) {
		return this.getButtonState(code) == KeyState.PRESS;
	}

	@Override
	public boolean isKeyPressedOnce(final int code) {
		this.checkOwnerThread();
		final boolean held = this.currKeys[code];
		final boolean once = held && !this.prevKeys[code];
		this.prevKeys[code] = held;
		return once;
	}

	@Override
	public boolean isMouseButtonPressedOnce(final int code) {
		this.checkOwnerThread();
		final boolean held = this.currMouse[code];
		final boolean once = held && !this.prevMouse[code];
		this.prevMouse[code] = held;
		return once;
	}

	@Override
	public boolean isKeyPressedOrRepeat(final int code) {
		this.checkOwnerThread();
		final KeyState s = this.getKeyState(code);
		final boolean active = (s == KeyState.PRESS && !this.prevKeys[code]) || s == KeyState.REPEAT;
		this.prevKeys[code] = (s == KeyState.PRESS || s == KeyState.REPEAT);
		return active;
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
	public GameEngine getGameEngine() {
		return this.engine;
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
	public boolean wasResized() {
		return this.resized;
	}

	@Override
	public char getPressedKeyChar() {
		return this.pressedChar == null ? '\0' : this.pressedChar;
	}

	@Override
	public boolean hasPressedKeyChar() {
		return this.pressedChar != null;
	}

	@Override
	public String getButtonName(final int code) {
		if (code >= 0 && code < MOUSE_BUTTON_NAMES.length) {
			return MOUSE_BUTTON_NAMES[code];
		}
		return null;
	}

	@Override
	public String getKeyName(final int code) {
		return GLFW.glfwGetKeyName(code, 0);
	}

	@Override
	public String getInputName(final int code) {
		if (code >= GLFW.GLFW_MOUSE_BUTTON_1 && code <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
			return this.getButtonName(code);
		}
		return this.getKeyName(code);
	}

	@Override
	public int getPressedMouse() {
		for (int i = 0; i < this.currMouse.length; i++) {
			if (this.currMouse[i]) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getPressedKey() {
		for (int i = 0; i < this.currKeys.length; i++) {
			if (this.currKeys[i]) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean hasPressedMouse() {
		for (final boolean b : this.currMouse) {
			if (b) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasPressedKey() {
		for (final boolean b : this.currKeys) {
			if (b) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasPressedMouseOnce() {
		for (int i = 0; i < this.currMouse.length; i++) {
			if (!this.prevMouse[i] && this.currMouse[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasPressedKeyOnce() {
		for (int i = 0; i < this.currKeys.length; i++) {
			if (!this.prevKeys[i] && this.currKeys[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "DefaultInputHandler [engine=" + this.engine + ", window=" + this.window + ", prevKeys=" + Arrays.toString(this.prevKeys)
				+ ", currKeys=" + Arrays.toString(this.currKeys) + ", prevMouse=" + Arrays.toString(this.prevMouse) + ", currMouse="
				+ Arrays.toString(this.currMouse) + ", owner=" + this.owner + ", mousePosition=" + this.mousePosition
				+ ", normalizedMousePosition=" + this.normalizedMousePosition + ", mouseScroll=" + this.mouseScroll + ", oldSize="
				+ this.oldSize + ", resized=" + this.resized + ", pressedChar=" + this.pressedChar + ", dTime=" + this.dTime + "]";
	}

}
