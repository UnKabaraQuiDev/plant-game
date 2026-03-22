package lu.kbra.plant_game.engine.scene.world.modal;

import java.util.Map;

import lu.kbra.standalone.gameengine.objs.entity.ParentAwareRoot;

public interface ModalsOwner extends /* Iterable<Modal>, */ ParentAwareRoot {

	Map<Class<? extends Modal>, Modal> getModals();

	default <T extends Modal> T getModal(final Class<T> clazz) {
		return (T) this.getModals().get(clazz);
	}

//	@Override
//	default Iterator<Modal> iterator() {
//		return this.getModals().values().iterator();
//	}

}
