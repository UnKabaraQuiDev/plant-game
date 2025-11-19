package lu.kbra.plant_game.engine.scene.ui;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticGrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.locale.LocalizationService;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

@DataPath("localization:string-placeholder")
public class OptionKeyUIObject extends ProgrammaticGrowOnHoverTextUIObject implements NeedsClick, NeedsInput {

	public static enum State {
		IDLE, WAITING_RELEASE, WAITING_INPUT;
	}

	private State awaitInput = State.IDLE;

	public OptionKeyUIObject(final String str, final TextEmitter text, final String key, final Scale2dDir dir,
			final Transform3D transform) {
		super(str, text, key, dir, transform);

		this.setKeyValue("x");
	}

	public OptionKeyUIObject(final String str, final TextEmitter text, final String key, final Scale2dDir dir) {
		this(str, text, key, dir, null);
	}

	@Override
	public void click(final WindowInputHandler input, final float dTime, final Scene scene) {
		if (awaitInput != State.IDLE) {
			return;
		}
		setKeyValue(" ");
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
			getTextEmitter().updateText();
			awaitInput = State.WAITING_RELEASE;
		});
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final Scene scene) {
		if (awaitInput == State.IDLE) {
			return;
		}
		if (awaitInput == State.WAITING_RELEASE && inputHandler.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			return;
		} else if (awaitInput == State.WAITING_RELEASE && !inputHandler.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			awaitInput = State.WAITING_INPUT;
		}

		final boolean dirty;
		if (inputHandler.hasPressedKey()) {
			setKeyValue(inputHandler.getMappedInputName(inputHandler.getPressedKey()));
			dirty = true;
		} else if (inputHandler.hasPressedMouse()) {
			setKeyValue(inputHandler.getMappedInputName(inputHandler.getPressedMouse()));
			dirty = true;
		} else {
			dirty = false;
		}

		if (dirty) {
			PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
				getTextEmitter().updateText();
				awaitInput = State.IDLE;
			});
		}
	}

	public void setKeyValue(String value) {
		final int length = this.getTextEmitter().getBufferLength();
		final String loc = LocalizationService.get("key." + super.key);
		value = "[" + value + "]";
		this.getTextEmitter().setText(loc + " ".repeat(Math.max(length - loc.length() - value.length(), 0)) + value);

		final Transform3D transform = this.getTransform();
		if (transform instanceof final Transform3DPivot t3dp) {
			final Vector2f textBounds = this.getTextEmitter().getTextBounds();
			t3dp.scalePivotSet(textBounds.x / 2, 0, textBounds.y / 2);
		}
	}

	public KeyOption getKeyOption() {
		return KeyOption.valueOf(super.key.toUpperCase());
	}

}
