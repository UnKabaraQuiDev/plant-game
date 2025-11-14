package lu.kbra.plant_game.engine.entity.ui.textinput;

import org.lwjgl.glfw.GLFW;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.ui.impl.Focusable;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:string-placeholder")
@BufferSize(25)
public class TextBoxUIObject extends TextFieldUIObject implements NeedsInput, NeedsClick, Focusable {

	public TextBoxUIObject(String str, TextEmitter textEmitter) {
		super(str, textEmitter);
	}

	public TextBoxUIObject(String str, TextEmitter textEmitter, Transform3D transform) {
		super(str, textEmitter, transform);
	}

	@Override
	protected boolean handleInput(WindowInputHandler inputHandler) {
		if ((inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_ENTER) || inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_KP_ENTER))
				&& super.getTextEmitter().getText().length() > 0) {
			input = PCUtils.insertChar(input, cursorPosition, '\n');
			cursorPosition++;

			return true;
		} else if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_TAB) && super.getTextEmitter().getText().length() > 0) {
			input = PCUtils.insertChar(input, cursorPosition, '\t');
			cursorPosition++;

			return true;
		} else if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_UP)) {
			moveCursorUp();
			return true;

		} else if (inputHandler.isKeyPressedOrRepeat(GLFW.GLFW_KEY_DOWN)) {
			moveCursorDown();
			return true;
		}

		return super.handleInput(inputHandler);
	}

	protected int getColumn(String text, int pos) {
		int lineStart = text.lastIndexOf('\n', pos - 1);
		if (lineStart == -1)
			lineStart = 0;
		else
			lineStart += 1;

		return pos - lineStart;
	}

	protected int getLineStart(String text, int pos) {
		int idx = text.lastIndexOf('\n', pos - 1);
		if (idx == -1)
			return 0;
		return idx + 1;
	}

	protected int getPrevLineStart(String text, int lineStart) {
		if (lineStart == 0)
			return -1;

		int endPrev = text.lastIndexOf('\n', lineStart - 2);
		if (endPrev == -1)
			return 0;
		return endPrev + 1;
	}

	protected int getNextLineStart(String text, int pos) {
		int next = text.indexOf('\n', pos);
		if (next == -1)
			return -1;
		return next + 1;
	}

	protected boolean moveCursorUp() {
		String text = getTextEmitter().getText();
		int pos = cursorPosition;

		int col = getColumn(text, pos);
		int lineStart = getLineStart(text, pos);
		int prevLineStart = getPrevLineStart(text, lineStart);

		if (prevLineStart == -1)
			return true; // no line above

		int prevLineEnd = text.indexOf('\n', prevLineStart);
		if (prevLineEnd == -1)
			prevLineEnd = text.length();

		int target = prevLineStart + col;
		target = Math.min(target, prevLineEnd);
		cursorPosition = target;

		return false;
	}

	protected boolean moveCursorDown() {
		String text = getTextEmitter().getText();
		int pos = cursorPosition;

		int col = getColumn(text, pos);
		int nextLineStart = getNextLineStart(text, pos);

		if (nextLineStart == -1)
			return false; // no line below

		int nextLineEnd = text.indexOf('\n', nextLineStart);
		if (nextLineEnd == -1)
			nextLineEnd = text.length();

		int target = nextLineStart + col;
		target = Math.min(target, nextLineEnd);
		cursorPosition = target;

		return true;
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

}
