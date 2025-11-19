package lu.kbra.plant_game.engine.window.input;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;

public class MappingInputHandler extends DefaultInputHandler {

	protected final int[] keyMapping = new int[GLFW.GLFW_KEY_LAST - GLFW_KEY_FIRST + 1];
	protected final int[] mouseMapping = new int[GLFW.GLFW_MOUSE_BUTTON_LAST - GLFW_MOUSE_FIRST + 1];

	public MappingInputHandler(final GameEngine engine) {
		super(engine);

		this.resetKeyMappings();
		this.resetMouseMappings();
	}

	@Override
	public boolean isKeyPressedOnce(final int code) {
		if (!this.isKeyMapped(code)) {
			return false;
		}
		return super.isKeyPressedOnce(this.getMappedKey(code));
	}

	@Override
	public boolean isKeyPressedOrRepeat(final int code) {
		if (!this.isKeyMapped(code)) {
			return false;
		}
		return super.isKeyPressedOrRepeat(this.getMappedKey(code));
	}

	@Override
	public boolean isMouseButtonPressedOnce(final int code) {
		if (!this.isButtonMapped(code)) {
			return false;
		}
		return super.isMouseButtonPressedOnce(this.getMappedButton(code));
	}

	@Override
	public KeyState getKeyState(final int code) {
		if (!this.isKeyMapped(code)) {
			return KeyState.RELEASE;
		}
		return super.getKeyState(this.getMappedKey(code));
	}

	@Override
	public KeyState getButtonState(final int code) {
		if (!this.isButtonMapped(code)) {
			return KeyState.RELEASE;
		}
		return super.getButtonState(this.getMappedButton(code));
	}

	public boolean isKeyMapped(final int code) {
		return this.keyMapping[code] != -1;
	}

	public boolean isButtonMapped(final int code) {
		return this.mouseMapping[code] != -1;
	}

	public Integer getMappedButton(final int code) {
		return this.isButtonMapped(code) ? this.mouseMapping[code] : null;
	}

	public Integer getMappedKey(final int code) {
		return this.isKeyMapped(code) ? this.keyMapping[code] : null;
	}

	@Override
	public String getKeyName(final int code) {
		if (!this.isKeyMapped(code)) {
			return null;
		}
		return GLFW.glfwGetKeyName(this.getMappedKey(code), 0);
	}

	@Override
	public String getButtonName(final int code) {
		if (!this.isButtonMapped(code)) {
			return null;
		}
		return super.getButtonName(this.getMappedButton(code));
	}

	@Override
	public String getInputName(final int code) {
		if (code >= GLFW_MOUSE_FIRST && code <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
			if (!this.isButtonMapped(code)) {
				return null;
			}
			final int physicalButton = this.getMappedButton(code) - GLFW_MOUSE_FIRST;
			if (physicalButton >= 0 && physicalButton < MOUSE_BUTTON_NAMES.length) {
				return MOUSE_BUTTON_NAMES[physicalButton];
			}
		} else if (code >= GLFW_KEY_FIRST && code <= GLFW.GLFW_KEY_LAST) {
			if (!this.isKeyMapped(code)) {
				return null;
			}
			final int physicalKey = this.getMappedKey(code);
			return GLFW.glfwGetKeyName(physicalKey, 0);
		}

		return null;
	}

	public void unsetKey(final int logicalKey) {
		this.keyMapping[logicalKey] = -1;
	}

	public void remapKey(final int logicalKey, final int physicalKey) {
		if (logicalKey >= 0 && logicalKey <= GLFW.GLFW_KEY_LAST) {
			this.keyMapping[logicalKey] = physicalKey;
		}
	}

	public void unsetMouseButton(final int logicalButton) {
		this.mouseMapping[logicalButton] = -1;
	}

	public void remapMouseButton(final int logicalButton, final int physicalButton) {
		if (logicalButton >= 0 && logicalButton <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
			this.mouseMapping[logicalButton] = physicalButton;
		}
	}

	public static class InputMappingConfig {
		public Map<Integer, Integer> keyMap;
		public Map<Integer, Integer> mouseMap;
	}

	public void resetKeyMappings() {
		for (int i = GLFW_KEY_FIRST; i <= GLFW.GLFW_KEY_LAST; i++) {
			this.keyMapping[i - GLFW_KEY_FIRST] = i;
		}
	}

	public void resetMouseMappings() {
		for (int i = GLFW_MOUSE_FIRST; i <= GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			this.mouseMapping[i - GLFW_MOUSE_FIRST] = i;
		}
	}

	public void loadMappings(final File file) throws IOException {
		if (!file.exists()) {
			return;
		}

		final InputMappingConfig config = Consts.OBJECT_MAPPER.readValue(file, InputMappingConfig.class);

		this.resetKeyMappings();
		this.resetMouseMappings();

		if (config.keyMap != null) {
			for (final Map.Entry<Integer, Integer> e : config.keyMap.entrySet()) {
				this.remapKey(e.getKey(), e.getValue());
			}
		}

		if (config.mouseMap != null) {
			for (final Map.Entry<Integer, Integer> e : config.mouseMap.entrySet()) {
				this.remapMouseButton(e.getKey(), e.getValue());
			}
		}
	}

	public void saveMappings(final File file) throws IOException {
		final InputMappingConfig config = new InputMappingConfig();

		final Map<Integer, Integer> keyMap = new java.util.HashMap<>();
		final Map<Integer, Integer> mouseMap = new java.util.HashMap<>();

		for (int i = 0; i < this.keyMapping.length; i++) {
			if (this.keyMapping[i] != i) {
				keyMap.put(i + GLFW_KEY_FIRST, this.keyMapping[i]);
			}
		}

		for (int i = 0; i < this.mouseMapping.length; i++) {
			if (this.mouseMapping[i] != i) {
				mouseMap.put(i + GLFW_MOUSE_FIRST, this.mouseMapping[i]);
			}
		}

		config.keyMap = keyMap;
		config.mouseMap = mouseMap;

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		Consts.OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, config);
	}

}
