package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.scene.Scene;

public interface GrowOnHover extends NeedsUpdate, Transform3DOwner, NeedsHover {

	Vector3fc HORIZONTAL_GROWTH_SCALE = new Vector3f(1.1f, 1, 1);
	Vector3fc VERTICAL_GROWTH_SCALE = new Vector3f(1, 1, 1.1f);
	Vector3fc BOTH_GROWTH_SCALE = new Vector3f(1.1f, 1, 1.1f);

	boolean isHovered();

	Vector3fc getTargetScale(boolean grow);

	float getGrowthRate(boolean grow);

	@Override
	default void update(final float dTime, final Scene scene) {
		this.grow(dTime, this.isHovered());
		this.getTransform().updateMatrix();
	}

	default float grow(final float dTime, final boolean grow) {
		final Vector3f scale = this.getTransform().getScale();
		final Vector3fc target = this.getTargetScale(grow);

		if (scale.equals(target, 0.001f)) {
			return grow ? 1 : 0;
		}

		final float speed = this.getGrowthRate(grow);

		for (int i = 0; i < 3; i++) {
			final float s = scale.get(i);
			final float t = target.get(i);
			final float step = speed * dTime;
			final float diff = t - s;

			if (!PCUtils.compare(s, t, 0.001f)) {
				if (Math.abs(diff) > step) {
					scale.setComponent(i, s + Math.signum(diff) * step);
				} else {
					scale.setComponent(i, t);
				}
			} else {
				scale.setComponent(i, t);
			}
		}

		final Vector3fc start = this.getTargetScale(false);
		final Vector3fc end = this.getTargetScale(true);
		final float currentDist = scale.distance(start);
		final float maxDistance = scale.distance(end);

		return Math.clamp(0, 1, currentDist / maxDistance);
	}

}
