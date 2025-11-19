package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface Transform3DOwner {

	default boolean hasTransform() {
		return this.getTransform() != null;
	}

	Transform3D getTransform();

}
