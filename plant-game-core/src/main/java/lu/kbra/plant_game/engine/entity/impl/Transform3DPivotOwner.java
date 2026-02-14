package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

public interface Transform3DPivotOwner extends Transform3DOwner {

	@Override
	Transform3DPivot getTransform();

	void setTransform(Transform3DPivot transform);

	@Deprecated
	@Override
	default void setTransform(final Transform3D t) {
		this.setTransform(t instanceof Transform3DPivot t3d ? t3d : new Transform3DPivot(t.getMatrix()));
	}

}
