package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.utils.transform.Transform;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface Transform3DOwner extends TransformOwner {

	@Override
	Transform3D getTransform();

	@Deprecated
	@Override
	default void setTransform(final Transform t) {
		this.setTransform(t instanceof Transform3D t3d ? t3d : new Transform3D(t.getMatrix()));
	}

	void setTransform(Transform3D transform);

}
