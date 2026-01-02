package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;

public interface BoundsOwnerParentAware extends ParentAwareNode {

	default boolean hasBoundsOwnerParent() {
		return this.getBoundsOwnerParent() == null;
	}

	default BoundsOwner getBoundsOwnerParent() {
		Object current = this.getParent();

		while (current != null) {
			if (current instanceof final BoundsOwner bo) {
				return bo;
			}

			if (!(current instanceof final ParentAwareNode pa)) {
				return null;
			}
			current = pa.getParent();
		}

		return null;
	}

}
