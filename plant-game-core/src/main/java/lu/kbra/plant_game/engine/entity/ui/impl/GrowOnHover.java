package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;

public interface GrowOnHover extends Transform3DOwner, AnimatedOnHover, ParentAwareNode {

	Vector3fc HORIZONTAL_GROWTH_SCALE = new Vector3f(1.1f, 1, 1);
	Vector3fc VERTICAL_GROWTH_SCALE = new Vector3f(1, 1, 1.1f);
	Vector3fc BOTH_GROWTH_SCALE = new Vector3f(1.1f, 1, 1.1f);

	Vector3fc getTargetScale(boolean grow);

	default boolean forceRecomputeLayout() {
		return true;
	}

	default void recomputeLayout() {
		if (this.forceRecomputeLayout()) {
			this.getFirstParentMatching(LayoutOwner.class).ifPresent(LayoutOwner::doLayout);
		}
	}

	@Override
	default boolean isAnimated() {
		return this.hasTransform();
	}

	@Override
	default void animate(final float t, final boolean isHovered) {
		this.grow(t, isHovered);
	}

	default void grow(final float t, final boolean grow) {
		final Vector3f scale = this.getTransform().getScale();
		final Vector3fc start = this.getTargetScale(false);
		final Vector3fc end = this.getTargetScale(true);

//		final float interpT = this.getInterpolator(grow).evaluate(this.getGrowthProgress());

		start.lerp(end, t, scale);

		this.getTransform().update();

		this.recomputeLayout();
	}

}
