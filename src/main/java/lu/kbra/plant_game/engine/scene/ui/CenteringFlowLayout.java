package lu.kbra.plant_game.engine.scene.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.scene.Scene;

public class CenteringFlowLayout extends FlowLayout implements SceneParentAware {

	protected Object parent;

	public CenteringFlowLayout(final boolean vertical, final float gap) {
		super(vertical, gap);
	}

	@Override
	public void doLayout(final List<UIObject> children, final float aspectRatio) {
		if (children == null || children.isEmpty()) {
			return;
		}

		// Get parent size in normalized coordinates
		final Scene parent = this.getSceneParent();
		final float width = parent.getCamera().getProjection().getAspectRatio();

		float totalWidth = 0;
		float totalHeight = 0;
		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}
			final Shape boundsShape = child.getBounds();
			final Rectangle2D bounds = boundsShape.getBounds2D();
			totalWidth += (float) bounds.getWidth() * child.getTransform().getScale().x;
			totalHeight += (float) bounds.getHeight() * child.getTransform().getScale().y;
		}

		float offsetX = -totalWidth / 2.0f;
		float offsetY = totalHeight / 2.0f;

		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}

			final Shape boundsShape = child.getBounds();
			final Rectangle2D bounds = boundsShape.getBounds2D();
			final float scaleX = child.getTransform().getScale().x;
			final float scaleY = child.getTransform().getScale().y;

			final float x = offsetX + scaleX * (float) bounds.getWidth() / 2.0f;
			final float y = offsetY - scaleY * (float) bounds.getHeight() / 2.0f;

			child.getTransform().translationSet(x, 0, y).updateMatrix();

			// Update offset for next child
			if (this.vertical) {
				offsetY -= scaleY * ((float) bounds.getHeight() + this.gap);
			} else {
				offsetX += scaleX * ((float) bounds.getWidth() + this.gap);
			}
		}
	}

	@Override
	public void setParent(final Object e) {
		this.parent = e;
	}

	@Override
	public Object getParent() {
		return this.parent;
	}

}
