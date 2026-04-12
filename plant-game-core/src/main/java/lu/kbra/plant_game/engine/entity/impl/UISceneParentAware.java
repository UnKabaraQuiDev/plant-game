package lu.kbra.plant_game.engine.entity.impl;

import java.util.Optional;

import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;

public interface UISceneParentAware extends BoundsSceneParentAware {

//	default boolean hasUISceneParent() {
//		return this.getUISceneParent() != null;
//	}

	default Optional<UIScene> getUISceneParent() {
		Object current = this.getParent();

		while (current != null) {
			if (current instanceof final UIScene scene) {
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
