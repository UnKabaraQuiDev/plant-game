package lu.kbra.plant_game.engine.entity.ui;

import java.awt.Shape;

import javax.swing.GroupLayout.Alignment;

import lu.kbra.plant_game.engine.entity.impl.UIObject;
import lu.kbra.plant_game.engine.entity.impl.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.entity.components.TextEmitterComponent;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TextButtonUIObject extends UIObject {

	private TextEmitterComponent textEmitterComponent;

	public TextButtonUIObject(String str, TextEmitter text) {
		super(str, null);
		super.addComponent(textEmitterComponent = new TextEmitterComponent(text));
	}

	public TextButtonUIObject(String str, TextEmitter text, Transform3D transform) {
		super(str, null, transform);
		super.addComponent(textEmitterComponent = new TextEmitterComponent(text));
	}

	@Override
	public void hover(WindowInputHandler input, float dTime) {
	}

	@Override
	public void click(WindowInputHandler input, float dTime) {
	}

	@Override
	public Shape getBounds() {
		assert textEmitterComponent.getTextEmitter().getLineCount() == 1;
		return GameEngineUtils.toRectangleBounds(textEmitterComponent.getTextEmitter().getTextBounds(),
				textEmitterComponent.getTextEmitter().getTextAlignment().asSwingAlignment(), Alignment.CENTER);
	}

	public TextEmitterComponent getTextEmitterComponent() {
		return textEmitterComponent;
	}

	public TextEmitter getTextEmitter() {
		return textEmitterComponent == null ? null : textEmitterComponent.getTextEmitter();
	}

}
