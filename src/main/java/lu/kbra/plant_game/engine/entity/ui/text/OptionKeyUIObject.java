package lu.kbra.plant_game.engine.entity.ui.text;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.Focusable;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexedMenuElement;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.locale.LocalizationService;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.StandardKeyOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

@DataPath("")
public class OptionKeyUIObject extends ProgrammaticGrowOnHoverTextUIObject
		implements NeedsClick, Focusable, NeedsInput, IndexedMenuElement, AbsoluteTransformOwner {

	public static enum State {
		IDLE, WAITING_RELEASE, WAITING_INPUT;
	}

	private State awaitInput = State.IDLE;
	private boolean focused = false;

	public OptionKeyUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final Scale2dDir dir,
			final Transform3D transform) {
		super(str, text, key, dir, transform);

		this.setKeyValue("x");

		if (transform instanceof final Transform3DPivot t3dp) {
			final Vector2f textBounds = this.getTextEmitter().getTextBounds();
			t3dp.scalePivotSet(textBounds.x / 2, 0, textBounds.y / 2);
		}
	}

	public OptionKeyUIObject(final String str, final TextEmitter text, final String key, final Scale2dDir dir) {
		this(str, text, key, dir, null);
	}

	@Override
	public void click(final WindowInputHandler input, final float dTime, final Scene scene) {
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
	public void input(final WindowInputHandler inputHandler, final float dTime, final Scene scene) {
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
		final String loc = LocalizationService.get("key." + super.getKey());
		value = "[" + (value == null ? " " : value) + "]";
		super.setText(loc + " ".repeat(Math.max(length - loc.length() - value.length(), 0)) + value);
	}

	public KeyOption getKeyOption() {
		return StandardKeyOption.valueOf(super.key.toUpperCase());
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

	@Override
	public int getIndex() {
		return StandardKeyOption.valueOf(super.key.toUpperCase()).ordinal();
	}

}
