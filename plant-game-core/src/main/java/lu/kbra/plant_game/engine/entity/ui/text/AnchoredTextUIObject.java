package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class AnchoredTextUIObject extends TextUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor objectAnchor) {
		this.objectAnchor = objectAnchor;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor targetAnchor) {
		this.targetAnchor = targetAnchor;
	}

	@Override
	public String toString() {
		return "AnchoredTextUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
