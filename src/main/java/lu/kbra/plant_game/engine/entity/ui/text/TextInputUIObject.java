package lu.kbra.plant_game.engine.entity.ui.text;

import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:btn.options")
public class TextInputUIObject extends TextUIObject implements NeedsInput, NeedsClick, Focusable {

	protected boolean focused = false;

	public TextInputUIObject(String str, TextEmitter text) {
		super(str, text);
	}

	public TextInputUIObject(String str, TextEmitter text, Transform3D transform) {
		super(str, text, transform);
	}

	@Override
	public void input(WindowInputHandler inputHandler, float dTime, Scene scene) {
		if (focused && inputHandler.hasPressedKeyChar()) {
			super.getTextEmitter().setText(super.getTextEmitter().getText() + inputHandler.getPressedKeyChar());
			PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> super.getTextEmitter().updateText());
		} else if (focused && inputHandler.isKeyPressedOnce(GLFW.GLFW_KEY_BACKSPACE) && super.getTextEmitter().getText().length() > 0) {
			super.getTextEmitter().setText(super.getTextEmitter().getText().substring(0, super.getTextEmitter().getText().length() - 1));
			PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> super.getTextEmitter().updateText());
		}
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
	}

	public String getText() {
		return getTextEmitter() != null ? super.getTextEmitter().getText() : null;
	}

}
