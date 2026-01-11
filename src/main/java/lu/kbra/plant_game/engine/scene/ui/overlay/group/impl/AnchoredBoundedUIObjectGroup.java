package lu.kbra.plant_game.engine.scene.ui.overlay.group.impl;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredBoundedUIObjectGroup extends AnchoredLayoutUIObjectGroup implements BoundsOwnerParentAware {

	protected boolean vertical;
	protected Rectangle2D.Float bounds = new Rectangle2D.Float();

	public AnchoredBoundedUIObjectGroup(final String str, final Layout layout, final boolean vertical, final Anchor obj, final Anchor tar,
			final UIObject... values) {
		super(str, layout, obj, tar, values);
		this.vertical = vertical;
	}

	public AnchoredBoundedUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final boolean vertical,
			final UIObject... values) {
		super(str, layout, transform, values);
		this.vertical = vertical;
	}

	public AnchoredBoundedUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final boolean vertical,
			final UIObject... values) {
		super(str, layout, parent, values);
		this.vertical = vertical;
	}

	@Override
	public boolean recomputeBounds() {
		final Rectangle2D parentBounds = this.getBoundsOwnerParent().getBounds().getBounds2D();
		final Rectangle2D compBounds = super.computedBounds.getBounds2D();
		super.recomputeBounds();

		if (this.vertical) {
			this.bounds = new Rectangle2D.Float((float) parentBounds.getX(),
					(float) compBounds.getY(),
					(float) parentBounds.getWidth(),
					(float) compBounds.getHeight());
		} else {
			this.bounds = new Rectangle2D.Float((float) compBounds.getX(),
					(float) parentBounds.getY(),
					(float) compBounds.getWidth(),
					(float) parentBounds.getHeight());
		}

		return true;
	}

	@Override
	public Shape getBounds() {
		return this.bounds;
	}

}
