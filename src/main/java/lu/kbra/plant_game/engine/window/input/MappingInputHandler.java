package lu.kbra.plant_game.engine.window.input;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.graph.window.KeyState;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;

public class MappingInputHandler extends DefaultInputHandler {

	private final int[] keyMap;
	private final int[] mouseMap;

	public MappingInputHandler(final GameEngine engine) {
		super(engine);

		this.keyMap = new int[GLFW.GLFW_KEY_LAST + 1];
		this.mouseMap = new int[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];

		this.resetKeyMappings();
		this.resetMouseMappings();
	}

	/* --------------------------------------------------------- */
	/* Mapping utilities */
	/* --------------------------------------------------------- */

	public void resetKeyMappings() {
		for (int i = 0; i < this.keyMap.length; i++) {
			this.keyMap[i] = i;
		}
	}

	public void resetMouseMappings() {
		for (int i = 0; i < this.mouseMap.length; i++) {
			this.mouseMap[i] = i;
		}
	}

	public void remapKey(final int logical, final int physical) {
		if (logical >= 0 && logical < this.keyMap.length) {
			this.keyMap[logical] = physical;
		}
	}

	public void remapMouseButton(final int logical, final int physical) {
		if (logical >= 0 && logical < this.mouseMap.length) {
			this.mouseMap[logical] = physical;
		}
	}

	public void unsetKey(final int logical) {
		if (logical >= 0 && logical < this.keyMap.length) {
			this.keyMap[logical] = -1;
		}
	}

	public void unsetMouseButton(final int logical) {
		if (logical >= 0 && logical < this.mouseMap.length) {
			this.mouseMap[logical] = -1;
		}
	}

	private boolean isKeyMapped(final int logical) {
		return logical >= 0 && logical < this.keyMap.length && this.keyMap[logical] != -1;
	}

	private boolean isMouseMapped(final int logical) {
		return logical >= 0 && logical < this.mouseMap.length && this.mouseMap[logical] != -1;
	}

	private int getMappedKey(final int logical) {
		return this.keyMap[logical];
	}

	private int getMappedMouse(final int logical) {
		return this.mouseMap[logical];
	}

	/* --------------------------------------------------------- */
	/* Overrides */
	/* --------------------------------------------------------- */

	@Override
	public KeyState getKeyState(final int logical) {
		if (!this.isKeyMapped(logical)) {
			return KeyState.RELEASE;
		}
		return super.getKeyState(this.getMappedKey(logical));
	}

	@Override
	public KeyState getButtonState(final int logical) {
		if (!this.isMouseMapped(logical)) {
			return KeyState.RELEASE;
		}
		return super.getButtonState(this.getMappedMouse(logical));
	}

	@Override
	public String getKeyName(final int logical) {
		if (!this.isKeyMapped(logical)) {
			return null;
		}
		return super.getKeyName(this.getMappedKey(logical));
	}

	@Override
	public String getButtonName(final int logical) {
		if (!this.isMouseMapped(logical)) {
			return null;
		}
		return super.getButtonName(this.getMappedMouse(logical));
	}

	@Override
	public String getInputName(final int logical) {
		if (logical >= 0 && logical < this.mouseMap.length) {
			if (!this.isMouseMapped(logical)) {
				return null;
			}
			return super.getInputName(this.getMappedMouse(logical));
		}

		if (logical >= 0 && logical < this.keyMap.length) {
			if (!this.isKeyMapped(logical)) {
				return null;
			}
			return super.getInputName(this.getMappedKey(logical));
		}

		return null;
	}

	/* --------------------------------------------------------- */
	/* Saving / loading */
	/* --------------------------------------------------------- */

	public static class InputMappingConfig {
		public Map<Integer, Integer> keyMap;
		public Map<Integer, Integer> mouseMap;
	}

	public void loadMappings(final File file) throws IOException {
		if (!file.exists()) {
			return;
		}

		final InputMappingConfig config = Consts.OBJECT_MAPPER.readValue(file, InputMappingConfig.class);

		this.resetKeyMappings();
		this.resetMouseMappings();

		if (config.keyMap != null) {
			for (final var e : config.keyMap.entrySet()) {
				this.remapKey(e.getKey(), e.getValue());
			}
		}

		if (config.mouseMap != null) {
			for (final var e : config.mouseMap.entrySet()) {
				this.remapMouseButton(e.getKey(), e.getValue());
			}
		}
	}

	public void saveMappings(final File file) throws IOException {
		final InputMappingConfig cfg = new InputMappingConfig();
		cfg.keyMap = new HashMap<>();
		cfg.mouseMap = new HashMap<>();

		for (int i = 0; i < this.keyMap.length; i++) {
			if (this.keyMap[i] != i) {
				cfg.keyMap.put(i, this.keyMap[i]);
			}
		}

		for (int i = 0; i < this.mouseMap.length; i++) {
			if (this.mouseMap[i] != i) {
				cfg.mouseMap.put(i, this.mouseMap[i]);
			}
		}

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}

		Consts.OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, cfg);
	}

}
