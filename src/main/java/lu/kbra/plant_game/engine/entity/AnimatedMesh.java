package lu.kbra.plant_game.engine.entity;

import org.joml.Matrix4f;

import lu.kbra.plant_game.engine.entity.AnimatedObjectLoader.AnimationData;
import lu.kbra.standalone.gameengine.geom.Mesh;

public interface AnimatedMesh extends Mesh {

	AnimationData getAnimation();

	void setAnimation(AnimationData a);

	Matrix4f computeTransform(Matrix4f mat, float t);

}
