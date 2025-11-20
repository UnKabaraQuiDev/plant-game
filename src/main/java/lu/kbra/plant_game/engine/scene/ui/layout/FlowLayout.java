package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

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

			final Rectangle2D bounds = child.getBounds().getBounds2D();

			final float scaleY;
			final float scaleX;
			if (child.hasTransform()) {
				final Transform3D transform = child.getTransform();
				scaleX = transform.getScale().x();
				scaleY = transform.getScale().y();
			} else {
				scaleX = 1f;
				scaleY = 1f;
			}

			child.getTransform().translationSet(offsetX, 0, offsetY - (float) bounds.getMinY() * scaleY).updateMatrix();

			if (this.vertical) {
				offsetY += bounds.getHeight() * scaleY + this.gap;
			} else {
				offsetX += bounds.getWidth() * scaleX + this.gap;
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
