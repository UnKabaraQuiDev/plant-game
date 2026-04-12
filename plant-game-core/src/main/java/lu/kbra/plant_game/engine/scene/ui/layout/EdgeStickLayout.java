package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.engine.entity.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@Deprecated
public class EdgeStickLayout extends FlowLayout implements BoundsOwnerParentAware, SceneParentAware {

	@Deprecated
	protected ParentAwareComponent parent;
	@Deprecated
	protected float size;

	@Deprecated
	public EdgeStickLayout(final boolean vertical, final float gap, final float size) {
		super(vertical, gap);
		assert vertical : "horizontal not supported";
		this.size = size;
	}

	@Deprecated
	@Override
	public void doLayout(final List<UIObject> children) {
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

	@Deprecated
	public float getSize() {
		return this.size;
	}

	@Deprecated
	public void setSize(final float size) {
		this.size = size;
	}

	@Deprecated
	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		this.parent = e;
	}

	@Deprecated
	@Override
	public ParentAwareComponent getParent() {
		return this.parent;
	}

}
