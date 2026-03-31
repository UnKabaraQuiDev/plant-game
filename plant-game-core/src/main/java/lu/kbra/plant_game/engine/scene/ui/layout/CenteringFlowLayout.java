package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;

public class CenteringFlowLayout extends FlowLayout implements SceneParentAware {

	protected ParentAwareComponent parent;

	public CenteringFlowLayout(final boolean vertical, final float gap) {
		super(vertical, gap);
		assert vertical : "horizontal not supported";
	}

	@Override
	public void doLayout(final List<UIObject> children) {
		if (children == null || children.isEmpty()) {
			return;
		}

		final float totalHeight = (float) (children.parallelStream()
				.map(e -> e.getBounds().getBounds2D().getHeight())
				.collect(Collectors.summingDouble(Double::valueOf)) + this.gap * (children.size() - 1));

		float offsetX = 0;
		float offsetY = -totalHeight / 2;

		for (final UIObject child : children) {
			if (child.getTransform() == null) {
				continue;
			}

			final Rectangle2D bounds = child.getBounds().getBounds2D();
			final float scaleX = child.getTransform().getScale().x;
			final float scaleY = child.getTransform().getScale().y;

			final float x = offsetX - scaleX * ((float) bounds.getWidth() / 2 + (float) bounds.getMinX());
			final float y = offsetY + scaleY * (float) bounds.getHeight() / 2;

			child.getTransform().translationSet(x, 0, y).updateMatrix();

			if (this.vertical) {
				offsetY += scaleY * ((float) bounds.getHeight() + this.gap);
			} else {
				offsetX += scaleX * ((float) bounds.getWidth() + this.gap);
			}
		}
	}

	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		this.parent = e;
	}

	@Override
	public ParentAwareComponent getParent() {
		return this.parent;
	}

}
