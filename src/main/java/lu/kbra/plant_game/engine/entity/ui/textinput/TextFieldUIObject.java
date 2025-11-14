package lu.kbra.plant_game.engine.entity.ui.textinput;

import org.lwjgl.glfw.GLFW;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.Focusable;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.future.ScheduledTask;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:string-placeholder")
@BufferSize(25)
public class TextFieldUIObject extends TextUIObject implements NeedsInput, NeedsClick, Focusable {

	private static final char CURSOR_CHAR = '_';

	protected String input = "";
	protected boolean focused = false;
	protected int cursorPosition = -1;
	protected ScheduledTask updateTask;

	public TextFieldUIObject(String str, TextEmitter textEmitter) {
		super(str, textEmitter);
		this.input = textEmitter.getText();
	}

	public TextFieldUIObject(String str, TextEmitter textEmitter, Transform3D transform) {
		super(str, textEmitter, transform);
		this.input = textEmitter.getText();
	}

	@Override
	public void input(WindowInputHandler inputHandler, float dTime, Scene scene) {
		if (updateTask != null && updateTask.wasRan()) {
			updateTask = null;
		}

		if (!focused) {
			return;
		}

		final boolean dirty = handleInput(inputHandler);

		if (dirty && (updateTask == null || updateTask.wasRan())) {
			super.getTextEmitter().setText(cursorPosition == -1 ? input : PCUtils.insertChar(input, cursorPosition, CURSOR_CHAR));
			updateTask = PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> super.getTextEmitter().updateText());
		}
	}

	protected boolean handleInput(WindowInputHandler inputHandler) {
		if (inputHandler.hasPressedKeyChar()) {
			input = PCUtils.insertChar(input, cursorPosition, inputHandler.getPressedKeyChar());
			cursorPosition++;

			return true;
		} else if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_BACKSPACE) && super.getTextEmitter().getText().length() > 0) {
			input = PCUtils.backspace(input, cursorPosition);
			cursorPosition = Math.max(0, cursorPosition - 1);

			return true;
		} else if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_LEFT)) {
			moveCursorLeft();

			return true;
		} else if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_RIGHT)) {
			moveCursorRight();

			return true;
		}

		return false;
	}

	protected void moveCursorLeft() {
		cursorPosition = Math.max(0, cursorPosition - 1);
	}

	protected void moveCursorRight() {
		cursorPosition = Math.min(getTextEmitter().getText().length(), cursorPosition + 1);
	}

	@Override
	public void click(WindowInputHandler input, float dTime, Scene scene) {

	}

	@Override
	public boolean hasFocus() {
		return focused;
	}

	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
		if (focused) {
			cursorPosition = super.getTextEmitter().getText().length();
		} else {
			cursorPosition = -1;
		}
	}

	public String getText() {
		return getTextEmitter() != null ? super.getTextEmitter().getText() : null;
	}

	public String getInput() {
		return input;
	}

}
