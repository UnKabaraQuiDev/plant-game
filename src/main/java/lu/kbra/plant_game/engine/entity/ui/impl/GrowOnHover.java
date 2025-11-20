package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;

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

	// not sure if this code works
	default float grow(final float dTime, final boolean grow) {
		final Vector3f scale = this.getTransform().getScale();
		final Vector3fc start = this.getTargetScale(false);
		final Vector3fc end = this.getTargetScale(true);

		if (scale.equals(grow ? end : start, 0.001f)) {
			return grow ? 1 : 0;
		}

		final float currentDist = scale.distance(start);
		final float maxDistance = start.distance(end);
		float t = maxDistance > 0 ? currentDist / maxDistance : 1f;

		final float speed = this.getGrowthRate(grow);
		t += (grow ? 1 : -1) * speed * dTime / maxDistance;
		t = Math.clamp(0f, 1f, t);

		final float interpT = Interpolators.SINE_IN.evaluate(t);

		start.lerp(end, interpT, scale);

		return t;
	}

}
