package lu.kbra.plant_game.engine.scene.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;

public class FlowLayout implements Layout {

	protected boolean vertical = true;
	protected float gap = 0.0f;

	public FlowLayout(final boolean vertical, final float gap) {
		this.vertical = vertical;
		this.gap = gap;
	}

	@Override
	public void doLayout(final List<UIObject> children, final float aspectRatio) {
		float offsetX = 0;
		float offsetY = 0;

		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}

			final Shape boundsShape = child.getBounds();
			final Rectangle2D bounds = boundsShape.getBounds2D();

			child.getTransform().translationSet(offsetX, 0, offsetY).updateMatrix();

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
