package lu.kbra.plant_game.engine.entity.ui.textinput;

import org.lwjgl.glfw.GLFW;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
@BufferSize(25)
public class TextBoxUIObject extends TextFieldUIObject {

	public TextBoxUIObject(final String str, final TextEmitter textEmitter) {
		super(str, textEmitter);
	}

	@Override
	protected boolean handleInput(final WindowInputHandler inputHandler) {
		if ((inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_ENTER) || inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_KP_ENTER))
				&& super.getTextEmitter().getText().length() > 0) {
			this.input = PCUtils.insertChar(this.input, this.cursorPosition, '\n');
			this.cursorPosition++;

			return true;
		}
		if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_TAB) && super.getTextEmitter().getText().length() > 0) {
			this.input = PCUtils.insertChar(this.input, this.cursorPosition, '\t');
			this.cursorPosition++;

			return true;
		}
		if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_UP)) {
			this.moveCursorUp();
			return true;

		}
		if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_DOWN)) {
			this.moveCursorDown();
			return true;
		}

		return super.handleInput(inputHandler);
	}

	protected int getColumn(final String text, final int pos) {
		int lineStart = text.lastIndexOf('\n', pos - 1);
		if (lineStart == -1) {
			lineStart = 0;
		} else {
			lineStart += 1;
		}

		return pos - lineStart;
	}

	protected int getLineStart(final String text, final int pos) {
		final int idx = text.lastIndexOf('\n', pos - 1);
		if (idx == -1) {
			return 0;
		}
		return idx + 1;
	}

	protected int getPrevLineStart(final String text, final int lineStart) {
		if (lineStart == 0) {
			return -1;
		}

		final int endPrev = text.lastIndexOf('\n', lineStart - 2);
		if (endPrev == -1) {
			return 0;
		}
		return endPrev + 1;
	}

	protected int getNextLineStart(final String text, final int pos) {
		final int next = text.indexOf('\n', pos);
		if (next == -1) {
			return -1;
		}
		return next + 1;
	}

	protected boolean moveCursorUp() {
		final String text = this.getTextEmitter().getText();
		final int pos = this.cursorPosition;

		final int col = this.getColumn(text, pos);
		final int lineStart = this.getLineStart(text, pos);
		final int prevLineStart = this.getPrevLineStart(text, lineStart);

		if (prevLineStart == -1) {
			return true; // no line above
		}

		int prevLineEnd = text.indexOf('\n', prevLineStart);
		if (prevLineEnd == -1) {
			prevLineEnd = text.length();
		}

		int target = prevLineStart + col;
		target = Math.min(target, prevLineEnd);
		this.cursorPosition = target;

		return false;
	}

	protected boolean moveCursorDown() {
		final String text = this.getTextEmitter().getText();
		final int pos = this.cursorPosition;

		final int col = this.getColumn(text, pos);
		final int nextLineStart = this.getNextLineStart(text, pos);

		if (nextLineStart == -1) {
			return false; // no line below
		}

		int nextLineEnd = text.indexOf('\n', nextLineStart);
		if (nextLineEnd == -1) {
			nextLineEnd = text.length();
		}

		int target = nextLineStart + col;
		target = Math.min(target, nextLineEnd);
		this.cursorPosition = target;

		return true;
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

}
