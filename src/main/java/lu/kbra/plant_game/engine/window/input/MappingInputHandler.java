package lu.kbra.plant_game.engine.window.input;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;

public class MappingInputHandler extends DefaultInputHandler {

	protected final int[] keyMapping = new int[GLFW.GLFW_KEY_LAST + 1];
	protected final int[] mouseMapping = new int[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

	public MappingInputHandler(GameEngine engine) {
		super(engine);

		for (int i = 0; i <= GLFW.GLFW_KEY_LAST; i++) {
			keyMapping[i] = i;
		}
		for (int i = 0; i <= GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			mouseMapping[i] = i;
		}
	}

	@Override
	public KeyState getKeyState(int code) {
		int mappedCode = keyMapping[code];
		return super.getKeyState(mappedCode);
	}

	@Override
	public KeyState getButtonState(int code) {
		int mappedCode = mouseMapping[code];
		return super.getButtonState(mappedCode);
	}

	public int getMappedButton(int code) {
		return mouseMapping[code];
	}

	public int getMappedKey(int code) {
		return keyMapping[code];
	}

	public String getMappedKeyName(int code) {
		return GLFW.glfwGetKeyName(getMappedKey(code), 0);
	}

	public String getMappedButtonName(int code) {
		int physical = getMappedButton(code);
		if (physical >= 0 && physical < MOUSE_BUTTON_NAMES.length) {
			return MOUSE_BUTTON_NAMES[physical];
		}
		return null;
	}

	@Override
	public String getMappedInputName(int code) {
		if (code >= GLFW.GLFW_MOUSE_BUTTON_1 && code <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
			int physicalButton = getMappedButton(code) - GLFW.GLFW_MOUSE_BUTTON_1;
			if (physicalButton >= 0 && physicalButton < MOUSE_BUTTON_NAMES.length) {
				return MOUSE_BUTTON_NAMES[physicalButton];
			}
			return null;
		}

		if (code <= GLFW.GLFW_KEY_LAST) {
			int physicalKey = getMappedKey(code);
			return GLFW.glfwGetKeyName(physicalKey, 0);
		}

		return null;
	}

	public void remapKey(int logicalKey, int physicalKey) {
		if (logicalKey >= 0 && logicalKey <= GLFW.GLFW_KEY_LAST)
			keyMapping[logicalKey] = physicalKey;
	}

	public void remapMouseButton(int logicalButton, int physicalButton) {
		if (logicalButton >= 0 && logicalButton <= GLFW.GLFW_MOUSE_BUTTON_LAST)
			mouseMapping[logicalButton] = physicalButton;
	}

	public static class InputMappingConfig {
		public Map<Integer, Integer> keyMap;
		public Map<Integer, Integer> mouseMap;
	}

	public void resetKeyMappings() {
		for (int i = 0; i <= GLFW.GLFW_KEY_LAST; i++)
			keyMapping[i] = i;
	}

	public void resetMouseMappings() {
		for (int i = 0; i <= GLFW.GLFW_MOUSE_BUTTON_LAST; i++)
			mouseMapping[i] = i;
	}

	public void loadMappings(File file) throws IOException {
		if (!file.exists())
			return;

		InputMappingConfig config = Consts.OBJECT_MAPPER.readValue(file, InputMappingConfig.class);

		resetKeyMappings();
		resetMouseMappings();

		if (config.keyMap != null) {
			for (Map.Entry<Integer, Integer> e : config.keyMap.entrySet()) {
				remapKey(e.getKey(), e.getValue());
			}
		}

		if (config.mouseMap != null) {
			for (Map.Entry<Integer, Integer> e : config.mouseMap.entrySet()) {
				remapMouseButton(e.getKey(), e.getValue());
			}
		}
	}

	public void saveMappings(File file) throws IOException {
		InputMappingConfig config = new InputMappingConfig();

		Map<Integer, Integer> keyMap = new java.util.HashMap<>();
		Map<Integer, Integer> mouseMap = new java.util.HashMap<>();

		for (int i = 0; i <= GLFW.GLFW_KEY_LAST; i++) {
			if (keyMapping[i] != i)
				keyMap.put(i, keyMapping[i]);
		}

		for (int i = 0; i <= GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			if (mouseMapping[i] != i)
				mouseMap.put(i, mouseMapping[i]);
		}

		config.keyMap = keyMap;
		config.mouseMap = mouseMap;

		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if (!file.exists())
			file.createNewFile();
		Consts.OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, config);
	}

}
