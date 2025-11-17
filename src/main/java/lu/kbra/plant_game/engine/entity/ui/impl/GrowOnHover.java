package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Math;
import org.joml.Vector3f;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.scene.Scene;

public interface GrowOnHover extends NeedsUpdate, Transform3DOwner, NeedsHover {

	Vector3f HORIZONTAL_GROWTH_SCALE = new Vector3f(1.1f, 1, 1);
	Vector3f VERTICAL_GROWTH_SCALE = new Vector3f(1, 1, 1.1f);
	Vector3f BOTH_GROWTH_SCALE = new Vector3f(1.1f, 1, 1.1f);

	boolean isHovered();

	Vector3f getTargetScale(boolean grow);

	float getGrowthRate(boolean grow);

	@Override
	default void update(final float dTime, final Scene scene) {
		this.grow(dTime, this.isHovered());
		this.getTransform().updateMatrix();
	}

	default float grow(final float dTime, final boolean grow) {
		final Vector3f scale = this.getTransform().getScale();
		final Vector3f target = this.getTargetScale(grow);

		final float speed = this.getGrowthRate(grow);

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

		final Vector3f start = this.getTargetScale(false);
		final Vector3f end = this.getTargetScale(true);
		final float currentDist = scale.distance(start);
		final float maxDistance = scale.distance(end);

		return Math.clamp(0, 1, currentDist / maxDistance);
	}

}
