package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.data.HoverState;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
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
	default boolean hover(final WindowInputHandler input, final HoverState hoverState) {
		if (!this.hasTransform()) {
			return false;
		}
		final boolean hovered = (hoverState == HoverState.ENTER || hoverState == HoverState.STAY);
		this.grow(input.dTime(), hovered);
		return this.getGrowthProgress() == 0;
	}

	default void grow(final float dTime, final boolean grow) {
		final Vector3f scale = this.getTransform().getScale();
		final Vector3fc start = this.getTargetScale(false);
		final Vector3fc end = this.getTargetScale(true);

		this.compute(dTime, grow);

		final float interpT = this.getInterpolator(grow).evaluate(this.getGrowthProgress());

		start.lerp(end, interpT, scale);

		this.getTransform().update();

		this.recomputeLayout();
	}

}
