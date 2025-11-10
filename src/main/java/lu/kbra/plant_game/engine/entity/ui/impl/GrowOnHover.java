package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Math;
import org.joml.Vector3f;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.NeedsUpdate;

public interface GrowOnHover extends NeedsUpdate, Transform3DOwner, NeedsHover {

	boolean isHovered();

	Vector3f getTargetScale(boolean grow);

	float getGrowthRate(boolean grow);

	@Override
	default void update(float dTime) {
		grow(dTime, isHovered());
		getTransform().updateMatrix();
	}

	default float grow(float dTime, boolean grow) {
		final Vector3f scale = getTransform().getScale();
		final Vector3f target = getTargetScale(grow);

		final float speed = getGrowthRate(grow);

		for (int i = 0; i < 3; i++) {
			final float s = scale.get(i);
			final float t = target.get(i);
			final float factor = (t - s) * speed;

			if (!PCUtils.compare(s, t, 0.001f)) {
				scale.setComponent(i, s + factor * dTime);
			} else {
				scale.setComponent(i, t);
			}
		}

		final Vector3f start = getTargetScale(false), end = getTargetScale(true);
		final float currentDist = scale.distance(start), maxDistance = scale.distance(end);

		return Math.clamp(0, 1, currentDist / maxDistance);
	}

}
