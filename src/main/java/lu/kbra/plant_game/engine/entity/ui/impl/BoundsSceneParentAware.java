package lu.kbra.plant_game.engine.entity.ui.impl;

import java.util.Optional;

import lu.kbra.plant_game.engine.entity.impl.SceneBoundsOwner;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;

public interface BoundsSceneParentAware extends SceneParentAware, BoundsOwnerParentAware {

	default Optional<SceneBoundsOwner> getBoundsSceneParent() {
		Object current = this.getParent();

		while (current != null) {
			if (current instanceof final SceneBoundsOwner scene) {
				return Optional.of(scene);
			}

			if (!(current instanceof final ParentAwareNode pa)) {
				return null;
			}
			current = pa.getParent();
		}

		return Optional.empty();
	}

}
