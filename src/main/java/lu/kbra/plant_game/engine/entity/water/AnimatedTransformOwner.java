package lu.kbra.plant_game.engine.entity.water;

import org.joml.Matrix4f;

public interface AnimatedTransformOwner {

	Matrix4f computeAnimatedTransform(float t);

	void setAnimatedTransform(Matrix4f animatedTransform);

	Matrix4f getAnimatedTransform();

}
