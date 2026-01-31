package lu.kbra.plant_game.engine.entity.ui.text;

import java.awt.geom.Rectangle2D;

import javax.swing.GroupLayout.Alignment;

import lu.pcy113.pclib.impl.ThrowingRunnable;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TransparentEntity;
import lu.kbra.standalone.gameengine.impl.future.YieldExecutionThrowable;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;

public class TextUIObject extends UIObject implements TextEmitterOwner, TransparentEntity {

	private TextEmitter textEmitter;

	public TextUIObject(final String str, final TextEmitter text) {
		super(str);
		this.setTextEmitter(text);
	}

	@Override
	public Rectangle2D.Float getBounds() {
		assert this.textEmitter.getLineCount() == 1;
		return GameEngineUtils.toRectangleBounds(this.textEmitter.getTextBounds(),
				this.textEmitter.getTextAlignment().asSwingAlignment(),
				Alignment.CENTER);
	}

	public String getText() {
		return this.getTextEmitter() != null ? this.getTextEmitter().getText() : null;
	}

	public TextUIObject setText(final String txt) {
		if (this.getTextEmitter() != null) {
			this.getTextEmitter().setText(txt);
		}
		return this;
	}

	public void flushText() {
		PGLogic.INSTANCE.RENDER_DISPATCHER.post((ThrowingRunnable<YieldExecutionThrowable>) this::updateText);
	}

	/** in gl thread only */
	public void updateText() {
		this.getTextEmitter().updateText();
	}

	@Override
	public TextEmitter getTextEmitter() {
		return this.textEmitter;
	}

	@Override
	public void setTextEmitter(final TextEmitter te) {
		this.textEmitter = te;
	}

}
