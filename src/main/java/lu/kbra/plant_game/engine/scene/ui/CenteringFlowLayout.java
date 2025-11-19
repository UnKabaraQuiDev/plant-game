package lu.kbra.plant_game.engine.scene.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.scene.Scene;

public class CenteringFlowLayout extends FlowLayout implements SceneParentAware {

	protected Object parent;

	public CenteringFlowLayout(final boolean vertical, final float gap) {
		super(vertical, gap);
		assert vertical : "horizontal not supported";
	}

	@Override
	public void doLayout(final List<UIObject> children, final float aspectRatio) {
		if (children == null || children.isEmpty()) {
			return;
		}

		final Scene parent = this.getSceneParent();
		final float width = parent.getCamera().getProjection().getAspectRatio();

		final float totalHeight = (float) (children
				.parallelStream()
				.map(e -> e.getBounds().getBounds2D().getHeight())
				.collect(Collectors.summingDouble(Double::valueOf)) + this.gap * (children.size() - 1));

		float offsetX = 0;
		float offsetY = -totalHeight / 2;

		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}

			final Shape boundsShape = child.getBounds();
			final Rectangle2D bounds = boundsShape.getBounds2D();
			final float scaleX = child.getTransform().getScale().x;
			final float scaleY = child.getTransform().getScale().y;

			final float x = offsetX - scaleX * (float) bounds.getWidth() / 2.0f;
			final float y = offsetY + scaleY * (float) bounds.getHeight() / 2.0f;

			child.getTransform().translationSet(x, 0, y).updateMatrix();

			// Update offset for next child
			if (this.vertical) {
				offsetY += scaleY * ((float) bounds.getHeight() + this.gap);
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
