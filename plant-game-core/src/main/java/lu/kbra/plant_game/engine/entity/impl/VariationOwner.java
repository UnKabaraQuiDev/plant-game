package lu.kbra.plant_game.engine.entity.impl;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.standalone.gameengine.GameEngine;

public interface VariationOwner {

	float DEV = 0.15f;
	Vector3fc MIN_DEV = new Vector3f(1 - DEV, 1 - DEV, 1 - DEV);
	Vector3fc MAX_DEV = new Vector3f(1 + DEV, 1 + DEV, 1 + DEV);

	default Vector3fc getMinVariation() {
		return MIN_DEV;
	}

	default Vector3fc getMaxVariation() {
		return MAX_DEV;
	}

	default boolean hasVariation() {
		return true;
	}

	default Vector3fc getVariationCellSize() {
		return GameEngine.IDENTITY_VECTOR3F;
	}

	default boolean useObjectTransform() {
		return false;
	}

}
