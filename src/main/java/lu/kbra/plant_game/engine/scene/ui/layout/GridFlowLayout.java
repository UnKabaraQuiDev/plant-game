package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;

public class GridFlowLayout implements Layout, BoundsOwnerParentAware {

	protected float outerMarginX;
	protected float outerMarginY;
	protected float gapX;
	protected float gapY;

	protected ParentAwareComponent parent;

	public GridFlowLayout(final float outerMarginX, final float outerMarginY, final float gapX, final float gapY) {
		this.outerMarginX = outerMarginX;
		this.outerMarginY = outerMarginY;
		this.gapX = gapX;
		this.gapY = gapY;
	}

	public GridFlowLayout(final float outerMarginX, final float gapX) {
		this.outerMarginX = this.outerMarginY = outerMarginX;
		this.gapX = this.gapY = gapX;
	}

	public GridFlowLayout(final float outerMarginX) {
		this.gapX = this.gapY = this.outerMarginX = this.outerMarginY = outerMarginX;
	}

	@Override
	public void doLayout(final List<UIObject> children) {
		if (children.isEmpty()) {
			return;
		}
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty()) {
			return;
		}

		final Rectangle2D parentBounds = obo.get().getBounds().getBounds2D();

		final float minX = (float) (parentBounds.getX() + this.outerMarginX);
		final float maxX = (float) (parentBounds.getX() + parentBounds.getWidth() - this.outerMarginX);

		final float minY = (float) (parentBounds.getY() + this.outerMarginY);
		final float maxY = (float) (parentBounds.getY() + parentBounds.getHeight() - this.outerMarginY);

		final float availableWidth = maxX - minX;

		/* ---------- build rows ---------- */

		final List<List<UIObject>> rows = new ArrayList<>();
		List<UIObject> currentRow = new ArrayList<>();

		float rowWidth = 0f;

		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}

			final Rectangle2D b = child.getLocalTransformedBounds().getBounds2D();
			final float childWidth = (float) b.getWidth();

			final float nextWidth = currentRow.isEmpty() ? childWidth : rowWidth + this.gapX + childWidth;

			if (nextWidth > availableWidth && !currentRow.isEmpty()) {
				rows.add(currentRow);
				currentRow = new ArrayList<>();
				rowWidth = 0f;
			}

			if (!currentRow.isEmpty()) {
				rowWidth += this.gapX;
			}

			rowWidth += childWidth;
			currentRow.add(child);
		}

		if (!currentRow.isEmpty()) {
			rows.add(currentRow);
		}

		/* ---------- layout rows ---------- */

		float cursorY = minY;

		for (final List<UIObject> row : rows) {

			float totalWidth = 0f;
			float maxHeight = 0f;

			for (final UIObject child : row) {
				final Rectangle2D b = child.getLocalTransformedBounds().getBounds2D();
				totalWidth += (float) b.getWidth();
				maxHeight = Math.max(maxHeight, (float) b.getHeight());
			}

			final int count = row.size();
			final float computedGapX = count > 1 ? Math.max(this.gapX, (availableWidth - totalWidth) / (count - 1)) : 0f;

			float cursorX = minX;

			for (final UIObject child : row) {
				final Rectangle2D b = child.getLocalTransformedBounds().getBounds2D();

				child.getTransform().translationSet(cursorX - (float) b.getMinX(), 0f, cursorY - (float) b.getMinY()).updateMatrix();

				cursorX += (float) b.getWidth() + computedGapX;
			}

			cursorY += maxHeight + this.gapY;
			if (cursorY > maxY) {
				break;
			}
		}
	}

	public float getOuterMarginX() {
		return this.outerMarginX;
	}

	public void setOuterMarginX(final float outerMarginX) {
		this.outerMarginX = outerMarginX;
	}

	public float getOuterMarginY() {
		return this.outerMarginY;
	}

	public void setOuterMarginY(final float outerMarginY) {
		this.outerMarginY = outerMarginY;
	}

	public float getGapX() {
		return this.gapX;
	}

	public void setGapX(final float gapX) {
		this.gapX = gapX;
	}

	public float getGapY() {
		return this.gapY;
	}

	public void setGapY(final float gapY) {
		this.gapY = gapY;
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
		return "GridFlowLayout [outerMarginX=" + this.outerMarginX + ", outerMarginY=" + this.outerMarginY + ", gapX=" + this.gapX
				+ ", gapY=" + this.gapY + "]";
	}

}
