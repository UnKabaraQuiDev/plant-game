package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;

public interface GrowOnHover extends /* NeedsUpdate, */Transform3DOwner, NeedsHover {

	Vector3fc HORIZONTAL_GROWTH_SCALE = new Vector3f(1.1f, 1, 1);
	Vector3fc VERTICAL_GROWTH_SCALE = new Vector3f(1, 1, 1.1f);
	Vector3fc BOTH_GROWTH_SCALE = new Vector3f(1.1f, 1, 1.1f);

//	boolean isHovered();

	Vector3fc getTargetScale(boolean grow);

	float getGrowthRate(boolean grow);

	float getGrowthProgress();

	void setGrowthProgress(float f);

	default Interpolator getInterpolator(final boolean grow) {
		return Interpolators.BACK_OUT;
	}
//
//	@Override
//	default void update(final WindowInputHandler input) {
//		this.grow(input.dTime(), this.isHovered());
//		this.getTransform().updateMatrix();
//	}

	@Override
	default boolean hover(final WindowInputHandler input, final HoverState hoverState) {
		if (!this.hasTransform()) {
			return false;
		}
		final boolean hovered = (hoverState == HoverState.ENTER || hoverState == HoverState.STAY);
		final float progress = this.grow(input.dTime(), hovered);
		this.getTransform().updateMatrix();
		return progress == 0;
	}

	default float grow(final float dTime, final boolean grow) {
		final Vector3f scale = this.getTransform().getScale();
		final Vector3fc start = this.getTargetScale(false);
		final Vector3fc end = this.getTargetScale(true);

		final float t = PCUtils.clamp(0, 1, this.getGrowthProgress() + (grow ? dTime : -dTime) * this.getGrowthRate(grow));

		this.setGrowthProgress(t);

		if (t == (grow ? 1 : 0)) {
			return grow ? 1 : 0;
		}

		final float interpT = this.getInterpolator(grow).evaluate(t);

		start.lerp(end, interpT, scale);

		return t;
	}

}
