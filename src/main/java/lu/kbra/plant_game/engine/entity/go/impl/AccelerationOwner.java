package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector3f;

public interface AccelerationOwner {

	Vector3f getAcceleration();

	boolean isApplyAcceleration();

	boolean isResetAcceleration();

}
