package lu.kbra.plant_game.engine.entity.ui.text;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.entity.ui.impl.Focusable;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.plugin.registry.KeyRegistry;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

public class OptionKeyUIObject extends ProgrammaticGrowOnHoverTextUIObject
		implements NeedsClick, Focusable, NeedsInput, NeedsPostConstruct {

	public static enum State {
		IDLE,
		WAITING_RELEASE,
		WAITING_INPUT;
	}

	protected KeyOption keyOption;

	private State awaitInput = State.IDLE;
	private boolean focused = false;

	public OptionKeyUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public void postConstruct() {
		if (this.transform instanceof final Transform3DPivot t3dp) {
			final Vector2f textBounds = this.getTextEmitter().getTextBounds();
			t3dp.scalePivotSet(textBounds.x / 2, 0, textBounds.y / 2);
		}
		this.setKeyValue("x");
	}

	@Override
	public void click(final WindowInputHandler input) {
		if (this.awaitInput != State.IDLE) {
			return;
		}
		this.focused = true;
		this.setKeyValue(" ");
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
			super.updateText();
			this.awaitInput = State.WAITING_RELEASE;
		});
	}

	@Override
	public void input(final WindowInputHandler inputHandler) {
		if (this.awaitInput == State.IDLE) {
			return;
		}
		if (this.awaitInput == State.WAITING_RELEASE && inputHandler.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			return;
		}
		if (this.awaitInput == State.WAITING_RELEASE && !inputHandler.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			this.awaitInput = State.WAITING_INPUT;
		}

		final boolean dirty;
		if (inputHandler.hasPressedKey()) {
			final int key = inputHandler.getPressedKey();
			if (key == GLFW.GLFW_KEY_ESCAPE) {
				((MappingInputHandler) inputHandler).unsetKey(this.getKeyOption().getPhysicalKey());
			} else {
				this.setKeyValue(inputHandler.getInputName(key));
				((MappingInputHandler) inputHandler).remapKey(this.getKeyOption().getPhysicalKey(), key);
			}
			dirty = true;
		} else if (inputHandler.hasPressedMouse()) {
			final int btn = inputHandler.getPressedMouse();
			this.setKeyValue(inputHandler.getInputName(btn));
			((MappingInputHandler) inputHandler).remapMouseButton(this.getKeyOption().getPhysicalKey(), btn);
			dirty = true;
		} else {
			dirty = false;
		}

		if (dirty) {
			PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
				super.updateText();
				this.awaitInput = State.IDLE;
				this.focused = false;
			});
		}
	}

	public void setKeyValue(String value) {
		final int length = this.getTextEmitter().getBufferLength();
		final String loc = LocalizationService.get(KeyRegistry.getLocalizationKey(getKeyOption()));
		value = "[" + (value == null ? " " : value) + "]";
		super.setText(loc + " ".repeat(Math.max(length - loc.length() - value.length(), 0)) + value);
	}

	@Override
	public float getGrowthRate(final boolean grow) {
		return grow ? 0.5f : 0.4f;
	}

	@Override
	public void setFocused(final boolean focused) {
		this.focused = focused;
	}

	@Override
	public boolean hasFocus() {
		return this.focused;
	}

	public KeyOption getKeyOption() {
		return keyOption;
	}

	public void setKeyOption(KeyOption keyOption) {
		this.keyOption = keyOption;
	}

	@Override
	public String toString() {
		return "OptionKeyUIObject [keyOption=" + keyOption + ", awaitInput=" + awaitInput + ", focused=" + focused + ", dir=" + dir
				+ ", progress=" + progress + ", transform=" + transform + ", active=" + active + ", name=" + name + "]";
	}

}
