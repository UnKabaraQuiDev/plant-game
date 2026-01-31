package lu.kbra.plant_game.engine.entity.ui.slider;

import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class AnchoredSliderUIObject extends SliderUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredSliderUIObject(final String str, final TextEmitter text) {
		super(str, text);
		AnchorOwner.super.setAnchors(Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT);
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
		return "AnchoredSliderUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", min=" + this.min + ", max=" + this.max + ", value=" + this.value + ", divisors=" + this.divisors
				+ ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
