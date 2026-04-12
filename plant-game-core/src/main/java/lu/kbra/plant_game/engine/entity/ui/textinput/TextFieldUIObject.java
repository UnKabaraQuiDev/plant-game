package lu.kbra.plant_game.engine.entity.ui.textinput;

import org.lwjgl.glfw.GLFW;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.impl.Focusable;
import lu.kbra.plant_game.engine.entity.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.future.ScheduledTask;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
@BufferSize(25)
public class TextFieldUIObject extends TextUIObject implements NeedsInput, Focusable/* , NeedsPostConstruct */ {

	private static final char CURSOR_CHAR = '_';

	protected String input = "";
	protected boolean focused = false;
	protected int cursorPosition = -1;
	protected ScheduledTask updateTask;

	public TextFieldUIObject(final String str, final TextEmitter textEmitter) {
		super(str, textEmitter);
		this.input = textEmitter.getText();
	}

	@Override
	public void input(final WindowInputHandler inputHandler) {
		if (this.updateTask != null && this.updateTask.wasRan()) {
			this.updateTask = null;
		}

		if (!this.focused) {
			return;
		}

		final boolean dirty = this.handleInput(inputHandler);

		if (dirty && (this.updateTask == null || this.updateTask.wasRan())) {
			super.getTextEmitter()
					.setText(this.cursorPosition == -1 ? this.input : PCUtils.insertChar(this.input, this.cursorPosition, CURSOR_CHAR));
			this.updateTask = PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> super.getTextEmitter().updateText());
		}
	}

	protected boolean handleInput(final WindowInputHandler inputHandler) {
		if (inputHandler.hasPressedKeyChar()) {
			this.input = PCUtils.insertChar(this.input, this.cursorPosition, inputHandler.getPressedKeyChar());
			this.cursorPosition++;

			return true;
		}
		if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_BACKSPACE) && super.getTextEmitter().getText().length() > 0) {
			this.input = PCUtils.backspace(this.input, this.cursorPosition);
			this.cursorPosition = Math.max(0, this.cursorPosition - 1);

			return true;
		}
		if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_LEFT)) {
			this.moveCursorLeft();

			return true;
		}
		if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_RIGHT)) {
			this.moveCursorRight();

			return true;
		}

		return false;
	}

	protected void moveCursorLeft() {
		this.cursorPosition = Math.max(0, this.cursorPosition - 1);
	}

	protected void moveCursorRight() {
		this.cursorPosition = Math.min(this.getTextEmitter().getText().length(), this.cursorPosition + 1);
	}

	@Override
	public boolean hasFocus() {
		return this.focused;
	}

	@Override
	public void setFocused(final boolean focused) {
		this.focused = focused;
		if (focused) {
			this.cursorPosition = super.getTextEmitter().getText().length();
		} else {
			this.cursorPosition = -1;
		}
	}

	public int getCursorPosition() {
		return this.cursorPosition;
	}

	public void setCursorPosition(final int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public boolean isFocused() {
		return this.focused;
	}

	public void setInput(final String input) {
		this.input = input;
	}

	public String getInput() {
		return this.input;
	}

	@Override
	public String toString() {
		return "TextFieldUIObject [input=" + this.input + ", focused=" + this.focused + ", cursorPosition=" + this.cursorPosition
				+ ", updateTask=" + this.updateTask + "]";
	}

}
