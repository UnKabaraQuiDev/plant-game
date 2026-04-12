package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.engine.entity.ui.data.HoverState;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;

public interface AnimatedOnHover extends NeedsHover {

	float getGrowthRate(boolean grow);

	float getGrowthProgress();

	void setGrowthProgress(float f);

	default Interpolator getInterpolator(final boolean grow) {
		return grow ? Interpolators.BACK_OUT : Interpolators.EXPO_OUT;
	}

	boolean isAnimated();

	void animate(float t, final boolean isHovered);

	@Override
	default boolean hover(final WindowInputHandler input, final HoverState hoverState) {
		if (!this.isAnimated()) {
			return false;
		}
		final boolean hovered = (hoverState == HoverState.ENTER || hoverState == HoverState.STAY);
		this.compute(input.dTime(), hovered);
		this.animate(this.getInterpolator(hovered).evaluate(this.getGrowthProgress()), hovered);
		return this.getGrowthProgress() == 0;
	}

	default float compute(final float dTime, final boolean grow) {
		if ((grow && this.getGrowthProgress() >= 1) || (!grow && this.getGrowthProgress() <= 0)) {
			this.setGrowthProgress(grow ? 1 : 0);
			return this.getGrowthProgress();
		}

		final float t = PCUtils.clamp(0, 1, this.getGrowthProgress() + (grow ? dTime : -dTime) * this.getGrowthRate(grow));

		this.setGrowthProgress(t);

		return this.getGrowthProgress();
	}

}
