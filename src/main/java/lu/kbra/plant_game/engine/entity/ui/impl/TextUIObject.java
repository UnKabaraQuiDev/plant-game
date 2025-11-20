package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.Shape;

import javax.swing.GroupLayout.Alignment;

import lu.kbra.standalone.gameengine.objs.entity.components.TextEmitterComponent;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TextUIObject extends UIObject {

	private TextEmitterComponent textEmitterComponent;

	public TextUIObject(final String str, final TextEmitter text) {
		super(str, null);
		super.addComponent(this.textEmitterComponent = new TextEmitterComponent(text));
	}

	public TextUIObject(final String str, final TextEmitter text, final Transform3D transform) {
		super(str, null, transform);
		super.addComponent(this.textEmitterComponent = new TextEmitterComponent(text));
	}

	@Override
	public Shape getBounds() {
		assert this.textEmitterComponent.getTextEmitter().getLineCount() == 1;
		return GameEngineUtils
				.toRectangleBounds(this.textEmitterComponent.getTextEmitter().getTextBounds(),
						this.textEmitterComponent.getTextEmitter().getTextAlignment().asSwingAlignment(),
						Alignment.CENTER);
	}

	public TextEmitterComponent getTextEmitterComponent() {
		return this.textEmitterComponent;
	}

	public TextEmitter getTextEmitter() {
		return this.textEmitterComponent == null ? null : this.textEmitterComponent.getTextEmitter();
	}

}
