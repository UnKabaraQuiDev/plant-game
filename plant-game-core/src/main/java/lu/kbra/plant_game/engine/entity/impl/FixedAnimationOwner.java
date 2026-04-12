package lu.kbra.plant_game.engine.entity.impl;

import org.joml.Matrix4f;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;

public interface FixedAnimationOwner extends AnimatedMeshOwner {

	float getAnimationTime();

	void setAnimationTime(float t);

	float getAnimationDuration();

	default Interpolator getAnimationInterpolator() {
		return Interpolators.LINEAR;
	}

	default boolean isAnimationPingPong() {
		return true;
	}

	@Override
	default Matrix4f computeAnimatedTransform(final float dtime) {
		final AnimatedMesh animatedMesh = this.getAnimatedMesh();
		final Matrix4f animatedTransform = this.getAnimatedTransform();
		float t = this.getAnimationTime() + dtime;
		this.setAnimationTime(t);
		t /= this.getAnimationDuration();
		if (this.isAnimationPingPong()) {
			t = PCUtils.zigZag(t, 1);
		} else {
			t %= 1;
		}
		animatedMesh.computeTransform(animatedTransform, this.getAnimationInterpolator().evaluate(t));
		return animatedTransform;
	}

}
