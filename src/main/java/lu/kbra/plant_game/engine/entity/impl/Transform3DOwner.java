package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface Transform3DOwner extends TransformOwner {

	@Override
	Transform3D getTransform();

}
