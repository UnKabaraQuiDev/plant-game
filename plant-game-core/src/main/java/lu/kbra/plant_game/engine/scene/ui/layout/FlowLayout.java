package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Optional;

import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;

public class FlowLayout implements Layout, BoundsOwnerParentAware {

	protected boolean vertical = true;
	protected boolean center = true;
	protected float gap = 0.0f;
	protected boolean fixed = false;
	protected Anchor2D fixedAnchor = Anchor2D.LEADING;

	protected ParentAwareComponent parent;

	public FlowLayout(final boolean vertical, final float gap) {
		this.vertical = vertical;
		this.gap = gap;
	}

	/**
	 * Default {@code Anchor2D}: {@link Anchor2D.LEADING}
	 */
	@Deprecated
	public FlowLayout(final boolean vertical, final float gap, final boolean fixed) {
		this.vertical = vertical;
		this.gap = gap;
		this.fixed = fixed;
		this.center = false;
	}

	public FlowLayout(final boolean vertical, final float gap, final Anchor2D fixedAnchor) {
		this.vertical = vertical;
		this.gap = gap;
		this.fixed = true;
		this.fixedAnchor = fixedAnchor;
		this.center = false;
	}

	public FlowLayout(final boolean vertical, final boolean center, final float gap) {
		this.vertical = vertical;
		this.center = center;
		this.gap = gap;
	}

	public FlowLayout(final boolean vertical, final boolean center, final float gap, final boolean fixed) {
		this.vertical = vertical;
		this.center = center;
		this.gap = gap;
		this.fixed = fixed;
	}

	@Override
	public void doLayout(final List<UIObject> children) {
		float offsetX = 0;
		float offsetY = 0;

		if (this.fixed) {
			final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
			if (obo.isEmpty()) {
				GlobalLogger.warning("Hierarchy incomplete: FlowLayout doesn't have a BoundsOwner parent.");
				return;
			}
			final Rectangle2D superBounds = obo.get().getBounds().getBounds2D();
			switch (this.fixedAnchor) {
			case LEADING -> {
				offsetX = (float) superBounds.getMinX();
				offsetY = (float) superBounds.getMinY();
			}
			case CENTER -> {
				offsetX = (float) superBounds.getCenterX();
				offsetY = (float) superBounds.getCenterY();
			}
			case TRAILING -> {
				offsetX = (float) superBounds.getMaxX();
				offsetY = (float) superBounds.getMaxY();
			}
			}
		}

		for (final UIObject child : children) {
			if (!child.hasTransform() || child instanceof NoLayout || !child.isActive()) {
				continue;
			}

			final Rectangle2D bounds = child.getLocalTransformedBounds().getBounds2D();

			if (this.center) {
				if (this.vertical) {
					child.getTransform().translationSet(offsetX, 0, offsetY - (float) bounds.getMinY()).updateMatrix();
				} else {
					child.getTransform().translationSet(offsetX - (float) bounds.getMinX(), 0, offsetY).updateMatrix();
				}
			} else if (this.fixed) {
				switch (this.fixedAnchor) {
				case LEADING -> {
					child.getTransform()
							.translationSet(offsetX - (float) bounds.getMinX(), 0, offsetY - (float) bounds.getMinY())
							.updateMatrix();

				}
				case CENTER -> {
					child.getTransform()
							.translationSet(offsetX - (float) bounds.getCenterX(), 0, offsetY - (float) bounds.getCenterY())
							.updateMatrix();
				}
				case TRAILING -> {
					child.getTransform()
							.translationSet(offsetX - (float) bounds.getMaxX(), 0, offsetY - (float) bounds.getMaxY())
							.updateMatrix();
				}
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
