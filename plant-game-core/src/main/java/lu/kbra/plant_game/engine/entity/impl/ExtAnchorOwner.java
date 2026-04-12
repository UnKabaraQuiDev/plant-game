package lu.kbra.plant_game.engine.entity.impl;

import java.awt.Shape;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface ExtAnchorOwner extends AnchorOwner, Transform3DOwner, BoundsOwner, NeedsUpdate, TransformedBoundsOwner {

	UIObject getTarget();

	void setTarget(UIObject target);

	@Override
	default void update(final WindowInputHandler input) {
		this.applyAnchor();
	}

	default void setTarget(final UIObject target, final Anchor obj, final Anchor tar) {
		this.setTarget(target);
		this.setObjectAnchor(obj);
		this.setTargetAnchor(tar);
	}

	default boolean isExtAnchored() {
		return AnchorOwner.super.isAnchored() && this.getTarget() != null;
	}

	default void applyAnchor() {
		if (!this.isExtAnchored() || !this.hasTransform()) {
			return;
		}

		final Shape targetBounds = AbsoluteTransformedBoundsOwner.getAbsoluteTransformedBounds(this.getTarget());

		final float marginX = this instanceof MarginOwner mo ? mo.getMargin()
				: 0 + (this instanceof Margin2DOwner m2o ? m2o.getMarginX() : 0);
		final float marginZ = this instanceof MarginOwner mo ? mo.getMargin()
				: 0 + (this instanceof Margin2DOwner m2o ? m2o.getMarginZ() : 0);

		AnchorLayout.alignAnchors(this.getTransform(),
				this.getLocalTransformedBounds().getBounds2D(),
				targetBounds.getBounds2D(),
				this.getObjectAnchor(),
				this.getTargetAnchor(),
				-marginX,
				-marginZ);
	}

}
