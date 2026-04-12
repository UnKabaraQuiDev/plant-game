package lu.kbra.plant_game.engine.entity.impl;

import java.util.Optional;

import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;

public interface BoundsOwnerParentAware extends ParentAwareNode {

	default Optional<BoundsOwner> getBoundsOwnerParent() {
		Object current = this.getParent();

		while (current != null) {
			if (current instanceof final BoundsOwner bo) {
				return Optional.of(bo);
			}

			if (!(current instanceof final ParentAwareNode pa)) {
				return Optional.empty();
			}
			current = pa.getParent();
		}

		return Optional.empty();
	}

}
