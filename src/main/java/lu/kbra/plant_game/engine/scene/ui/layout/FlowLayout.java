package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;

public class FlowLayout implements Layout, BoundsOwnerParentAware {

	protected boolean vertical = true;
	protected boolean center = true;
	protected float gap = 0.0f;
	protected boolean fixed = false;

	protected ParentAwareComponent parent;

	public FlowLayout(final boolean vertical, final float gap) {
		this.vertical = vertical;
		this.gap = gap;
	}

	public FlowLayout(final boolean vertical, final float gap, final boolean fixed) {
		this.vertical = vertical;
		this.gap = gap;
		this.fixed = fixed;
		this.center = false;
	}

	public FlowLayout(final boolean vertical, final boolean center, final float gap) {
		this.vertical = vertical;
		this.center = center;
		this.gap = gap;
	}

	@Override
	public void doLayout(final List<UIObject> children) {
		float offsetX = 0;
		float offsetY = 0;

		if (this.fixed) {
			if (!this.hasBoundsOwnerParent()) {
				GlobalLogger.warning("Hierarchy incomplete: FlowLayout doesn't have a BoundsOwner parent.");
				return;
			}
			final Rectangle2D superBounds = this.getBoundsOwnerParent().getBounds().getBounds2D();
			offsetX = (float) superBounds.getMinX();
			offsetY = (float) superBounds.getMinY();
		}

		for (final UIObject child : children) {
			if (!child.hasTransform()) {
				continue;
			}

			final Rectangle2D bounds = child.getLocalTransformedBounds().getBounds2D();

			if (this.center) {
				if (this.vertical) {
					child.getTransform().translationSet(offsetX, 0, offsetY - (float) bounds.getMinY()).updateMatrix();
				} else {
					child.getTransform().translationSet(offsetX - (float) bounds.getMinX(), 0, offsetY).updateMatrix();
				}
			} else {
				child.getTransform()
						.translationSet(offsetX - (float) bounds.getMinX(), 0, offsetY - (float) bounds.getMinY())
						.updateMatrix();
			}

			if (this.vertical) {
				offsetY += bounds.getHeight() + this.gap;
			} else {
				offsetX += bounds.getWidth() + this.gap;
			}
		}
	}

	public void setGap(final float gap) {
		this.gap = gap;
	}

	public void setVertical(final boolean vertical) {
		this.vertical = vertical;
	}

	public float getGap() {
		return this.gap;
	}

	public boolean isVertical() {
		return this.vertical;
	}

	public boolean isCenter() {
		return this.center;
	}

	public void setCenter(final boolean center) {
		this.center = center;
	}

	public boolean isFixed() {
		return this.fixed;
	}

	public void setFixed(final boolean fixed) {
		this.fixed = fixed;
	}

	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		this.parent = e;
	}

	@Override
	public ParentAwareComponent getParent() {
		return this.parent;
	}

	@Override
	public String toString() {
		return "FlowLayout [vertical=" + this.vertical + ", center=" + this.center + ", gap=" + this.gap + ", fixed=" + this.fixed + "]";
	}

}
