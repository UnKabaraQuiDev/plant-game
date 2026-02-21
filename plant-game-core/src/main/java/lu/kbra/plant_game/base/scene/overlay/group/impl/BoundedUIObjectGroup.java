package lu.kbra.plant_game.base.scene.overlay.group.impl;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BoundedUIObjectGroup extends LayoutOffsetUIObjectGroup implements BoundsOwnerParentAware {

	/**
	 * component size
	 */
	protected Direction2d dir;
	protected Rectangle2D.Float bounds = new Rectangle2D.Float();

	public BoundedUIObjectGroup(final String str, final Layout layout, final Direction2d dir, final UIObject... values) {
		super(str, layout, values);
		this.dir = dir;
	}

	public BoundedUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final Direction2d dir,
			final UIObject... values) {
		super(str, layout, transform, values);
		this.dir = dir;
	}

	public BoundedUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Direction2d dir,
			final UIObject... values) {
		super(str, layout, parent, values);
		this.dir = dir;
	}

	public BoundedUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Transform3D transform,
			final Direction2d dir, final UIObject... values) {
		super(str, layout, parent, transform, values);
		this.dir = dir;
	}

	@Override
	public boolean recomputeBounds() {
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty() || this.dir == null) {
			return false;
		}

		if (!obo.get().areBoundsValid()) {
			super.recomputeBounds();
			final Rectangle2D b2d = super.computedBounds.getBounds2D();
			this.bounds = new Rectangle2D.Float((float) b2d.getX(), (float) b2d.getY(), (float) b2d.getWidth(), (float) b2d.getHeight());
			return true;
		}

		final Rectangle2D parentBounds = obo.get().getBounds().getBounds2D();
		super.recomputeBounds();
		final Rectangle2D compBounds = super.computedBounds.getBounds2D();
		final float marginX = (float) marginSumX.applyAsDouble(this);
		final float marginZ = (float) marginSumZ.applyAsDouble(this);

		this.bounds.setFrame(switch (this.dir) {
		case VERTICAL -> new Rectangle2D.Float((float) parentBounds.getX(),
				(float) compBounds.getY() + marginZ,
				(float) parentBounds.getWidth(),
				(float) compBounds.getHeight() - 2 * marginZ);
		case HORIZONTAL -> new Rectangle2D.Float((float) compBounds.getX() + marginX,
				(float) parentBounds.getY(),
				(float) compBounds.getWidth() - 2 * marginX,
				(float) parentBounds.getHeight());
		});

		return true;
	}

	@Override
	public Shape getBounds() {
		return this.bounds;
	}

	@Override
	public String toString() {
		return "BoundedUIObjectGroup@" + System.identityHashCode(this) + " [dir=" + this.dir + ", bounds=" + this.bounds + ", layout="
				+ this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
