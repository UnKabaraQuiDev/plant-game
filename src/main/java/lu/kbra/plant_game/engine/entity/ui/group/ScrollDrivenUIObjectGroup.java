package lu.kbra.plant_game.engine.entity.ui.group;

import java.awt.geom.Rectangle2D;
import java.util.function.Supplier;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ScrollDrivenUIObjectGroup extends OffsetUIObjectGroup implements NeedsUpdate, SceneParentAware {

	protected Supplier<Float> scrollRatioSupplier;
	protected float margin;
	protected Direction dir;

	public ScrollDrivenUIObjectGroup(
			final String str,
			final Transform3D transform,
			final Supplier<Float> scrollRatio,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, transform, values);
		this.scrollRatioSupplier = scrollRatio;
		this.dir = dir;
		this.margin = margin;
	}

	public ScrollDrivenUIObjectGroup(
			final String str,
			final Supplier<Float> scrollRatio,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, values);
		this.scrollRatioSupplier = scrollRatio;
		this.dir = dir;
		this.margin = margin;
	}

	public ScrollDrivenUIObjectGroup(
			final String str,
			final UIObjectGroup parent,
			final Supplier<Float> scrollRatio,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, parent, values);
		this.scrollRatioSupplier = scrollRatio;
		this.dir = dir;
		this.margin = margin;
	}

	@Override
	public void update(final float dTime, final Scene scene) {
		if (!this.isActive() || !this.hasTransform()) {
			return;
		}

//		this.recomputeBounds();
		final Rectangle2D bounds = this.getBounds().getBounds2D();
		final Rectangle2D sceneBounds = ((BoundsOwner) scene).getBounds().getBounds2D();

		if (this.dir.isHorizontal() && bounds.getWidth() < sceneBounds.getWidth()) {
			this.getTransform().getTranslation().x = (float) (sceneBounds.getCenterX() - bounds.getCenterX());
			this.getTransform().updateMatrix();
			return;
		}
		if (this.dir.isVertical() && bounds.getHeight() < sceneBounds.getHeight()) {
			this.getTransform().getTranslation().z = (float) (/* sceneBounds.getCenterY() - */ bounds.getCenterY());
			this.getTransform().updateMatrix();
			return;
		}

		final float ratio = this.scrollRatioSupplier.get();

		if (this.dir.isHorizontal()) {
			final float min = (float) (sceneBounds.getMinX() + this.margin);
			final float max = (float) (sceneBounds.getMaxX() - this.margin);
			final float objMin = (float) bounds.getMinX();
			final float objMax = (float) bounds.getMaxX();
			final float originMin = min - objMin - (float) bounds.getCenterX();
			final float originMax = max - objMax + (float) bounds.getCenterX();

			this.getTransform().getTranslation().x = PCUtils.map(ratio, 1, 0, originMin, originMax);
		} else if (this.dir.isVertical()) {
			final float min = (float) (sceneBounds.getMinY() + this.margin);
			final float max = (float) (sceneBounds.getMaxY() - this.margin);
			this.getTransform().getTranslation().z = PCUtils.map(ratio, 0, 1, min - (float) bounds.getY(), max - (float) bounds.getY());
		}
		this.getTransform().updateMatrix();
	}

	public boolean needsScrollBar() {
		if (!this.hasSceneParent()) {
			return false;
		}

		final Rectangle2D bounds = this.getBounds().getBounds2D();
		final Rectangle2D sceneBounds = ((UIScene) this.getSceneParent()).getBounds().getBounds2D();

		if (this.dir.isHorizontal() && bounds.getWidth() < sceneBounds.getWidth()) {
			return false;
		}
		if (this.dir.isVertical() && bounds.getHeight() < sceneBounds.getHeight()) {
			return false;
		}

		return true;
	}

	public float getMargin() {
		return this.margin;
	}

	public Supplier<Float> getScrollRatioSupplier() {
		return this.scrollRatioSupplier;
	}

	public void setMargin(final float margin) {
		this.margin = margin;
	}

	public void setScrollRatioSupplier(final Supplier<Float> scrollRatio) {
		this.scrollRatioSupplier = scrollRatio;
	}

	public Direction getDirection() {
		return this.dir;
	}

	public void setDirection(final Direction dir) {
		this.dir = dir;
	}

	@Override
	public String toString() {
		return "ScrollingUIObjectGroup [scrollRatio=" + this.scrollRatioSupplier + ", dir=" + this.dir + ", bounds=" + this.bounds
				+ ", active=" + this.active + ", name=" + this.name + ", getTransform()=" + this.getTransform() + ", size()=" + this.size()
				+ "]";
	}

}
