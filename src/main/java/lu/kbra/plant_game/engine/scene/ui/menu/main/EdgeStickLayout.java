package lu.kbra.plant_game.engine.scene.ui.menu.main;

import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class EdgeStickLayout extends FlowLayout implements BoundsOwnerParentAware, SceneParentAware {

	protected Object parent;
	protected float size;

	public EdgeStickLayout(final boolean vertical, final float gap, final float size) {
		super(vertical, gap);
		assert vertical : "horizontal not supported";
		this.size = size;
	}

	@Override
	public void doLayout(final List<UIObject> children, final float aspectRatio) {
		if (children.isEmpty()) {
			return;
		}

		float y = 0;

		final float width = this.size;

		for (int i = 0; i < children.size(); i += 2) {
			final UIObject left = children.get(i);
			final UIObject right = i + 1 < children.size() ? children.get(i + 1) : null;

			float maxHeight = 0f;

			if (left.hasTransform()) {
				final Rectangle2D bounds = left.getBounds().getBounds2D();
				final Transform3D t = left.getTransform();
				t.translationSet(-width / 2 - (float) bounds.getMinX(), 0, y).updateMatrix();
				maxHeight = Math.max(maxHeight, (float) bounds.getHeight() * t.getScale().y());
				System.err.println("x: " + -bounds.getMinX() + " for: " + left + " " + bounds);
			}

			if (right != null && right.hasTransform()) {
				final Rectangle2D bounds = right.getBounds().getBounds2D();
				final Transform3D t = right.getTransform();
				t.translationSet(width / 2 - (float) bounds.getMaxX(), 0, y).updateMatrix();
				maxHeight = Math.max(maxHeight, (float) bounds.getHeight() * t.getScale().y());
			}

			if (this.vertical) {
				y += maxHeight + this.gap;
			} else {
				PCUtils.throwUnsupported();
			}
		}

	}

	public float getSize() {
		return this.size;
	}

	public void setSize(final float size) {
		this.size = size;
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
