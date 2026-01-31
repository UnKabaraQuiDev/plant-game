package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.utils.transform.Transform;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface Transform3DOwner extends TransformOwner {

	@Override
	Transform3D getTransform();

	@Deprecated
	@Override
	default void setTransform(final Transform t) {
		this.setTransform((Transform3D) t);
	}

	void setTransform(Transform3D transform);

}
