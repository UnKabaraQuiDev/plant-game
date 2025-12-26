package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.utils.transform.Transform;

public interface TransformOwner {

	default boolean hasTransform() {
		return this.getTransform() != null;
	}

	Transform getTransform();

}
