package lu.kbra.plant_game.engine.scene.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;

public class MarginFlowLayout extends FlowLayout {

	protected float marginLeft, marginRight, marginTop, marginBottom;
	protected final byte flags; // bitmask: L/T/V/R

	public static final byte LEFT = 1 << 0;
	public static final byte TOP = 1 << 1;

	public MarginFlowLayout(
			final boolean vertical,
			final float gap,
			final float marginLeft,
			final float marginRight,
			final float marginTop,
			final float marginBottom,
			final byte flags) {
		super(vertical, gap);
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
		this.flags = flags;
	}

	public MarginFlowLayout(final boolean vertical, final float gap, final float margin, final byte flags) {
		this(vertical, gap, margin, margin, margin, margin, flags);
	}

	@Override
	public void doLayout(final List<UIObject> children, final float aspectRatio) {
		if (children == null || children.isEmpty()) {
			return;
		}

		final boolean left = (this.flags & LEFT) != 0;
		final boolean top = (this.flags & TOP) != 0;

		float offsetX = left ? -aspectRatio + this.marginLeft : aspectRatio - this.marginRight;
		float offsetY = top ? -1.0f + this.marginTop : 1.0f - this.marginBottom;

		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}

			final Shape boundsShape = child.getBounds();
			final Rectangle2D bounds = boundsShape.getBounds2D();

			final float scaleX = child.getTransform().getScale().x;
			final float scaleY = child.getTransform().getScale().y;

			final float x = offsetX + scaleX * (float) (left ? bounds.getMinX() : -bounds.getWidth());
			final float y = offsetY + scaleY * (float) (top ? -bounds.getMinY() : bounds.getHeight());

			child.getTransform().translationSet(x, 0, y).updateMatrix();

			if (this.vertical) {
				offsetY = y + (top ? 1 : -1) * scaleY * ((float) bounds.getHeight() + this.gap);
			} else {
				offsetX = x + (left ? 1 : -1) * scaleX * ((float) bounds.getWidth() + this.gap);
			}
		}
	}

	public float getMarginLeft() {
		return this.marginLeft;
	}

	public void setMarginLeft(final float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public float getMarginRight() {
		return this.marginRight;
	}

	public void setMarginRight(final float marginRight) {
		this.marginRight = marginRight;
	}

	public float getMarginTop() {
		return this.marginTop;
	}

	public void setMarginTop(final float marginTop) {
		this.marginTop = marginTop;
	}

	public float getMarginBottom() {
		return this.marginBottom;
	}

	public void setMarginBottom(final float marginBottom) {
		this.marginBottom = marginBottom;
	}

	public byte getFlags() {
		return this.flags;
	}

}
