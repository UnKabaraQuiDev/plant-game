package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;

public interface AnimatedOnHover extends NeedsHover {

	float getGrowthRate(boolean grow);

	float getGrowthProgress();

	void setGrowthProgress(float f);

	default Interpolator getInterpolator(final boolean grow) {
		return grow ? Interpolators.BACK_OUT : Interpolators.EXPO_OUT;
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
