package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.Shape;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public interface ExtAnchorOwner extends AnchorOwner, Transform3DOwner, BoundsOwner, NeedsUpdate {

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

	@Override
	default boolean isAnchored() {
		return AnchorOwner.super.isAnchored() && this.getTarget() != null;
	}

	default void applyAnchor() {
		if (!(this.isAnchored() && this.hasTransform())) {
			return;
		}

		final Shape targetBounds = AbsoluteTransformedBoundsOwner.getAbsoluteTransformedBounds(this.getTarget());
		final float margin = this instanceof MarginOwner mo ? mo.getMargin() : 0;
		AnchorLayout.alignAnchors(this.getTransform(),
				this.getBounds().getBounds2D(),
				targetBounds.getBounds2D(),
				this.getObjectAnchor(),
				this.getTargetAnchor(),
				margin,
				margin);
	}

}
