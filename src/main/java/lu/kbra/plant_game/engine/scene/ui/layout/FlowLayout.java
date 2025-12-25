package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.UIObject;

public class FlowLayout implements Layout {

	protected boolean vertical = true;
	protected float gap = 0.0f;

	public FlowLayout(final boolean vertical, final float gap) {
		this.vertical = vertical;
		this.gap = gap;
	}

	@Override
	public void doLayout(final List<UIObject> children) {
		float offsetX = 0;
		float offsetY = 0;

		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}

			final Rectangle2D bounds = child.getLocalTransformedBounds().getBounds2D();

			child.getTransform().translationSet(offsetX - (float) bounds.getMinX(), 0, offsetY - (float) bounds.getMinY()).updateMatrix();

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

}
