package lu.kbra.plant_game.engine.mesh;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.loader.AnimatedMeshLoader.AnimationData;
import lu.kbra.standalone.gameengine.geom.Mesh;

public interface AnimatedMesh extends Mesh {

	AnimationData getAnimation();

	void setAnimation(AnimationData a);

	default Matrix4f computeTransform(Matrix4f target, float t) {
		final AnimationData animation = getAnimation();
		if (animation == null) {
			return target;
		}

		final Vector3f pos = new Vector3f();
		animation.startPosition().lerp(animation.endPosition(), t, pos);

		final Quaternionf rot = new Quaternionf();
		animation.startRotation().slerp(animation.endRotation(), t, rot);

		final Vector3f scale = new Vector3f();
		animation.startScale().lerp(animation.endScale(), t, scale);

		if (this instanceof OffsetLoadedMesh thisOffset) {
			final Vector3f origin = thisOffset.getOffset();

			return target.identity().translate(pos).translate(origin).rotate(rot).scale(scale).translate(new Vector3f(origin).negate());
		} else {
			return target.identity().translate(pos).rotate(rot).scale(scale);
		}
	}

}
