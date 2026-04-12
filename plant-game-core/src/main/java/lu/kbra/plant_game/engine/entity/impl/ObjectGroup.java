package lu.kbra.plant_game.engine.entity.impl;

import java.util.Collection;

import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.scene.SynchronizedEntityContainer;

public interface ObjectGroup<T extends SceneEntity> extends Iterable<T>, ParentAwareNode, SynchronizedEntityContainer<T> {

	T get(int index);

	<V extends T> boolean addChildren(ObjectGroup<? extends V> c);

	void doSort();

	@Override
	default <U extends T> U add(final U entity) {
		final U ret = SynchronizedEntityContainer.super.add(entity);
		this.doSort();
		return ret;
	};

	@Override
	default <U extends T> boolean addAll(final Collection<? extends U> entities) {
		final boolean ret = SynchronizedEntityContainer.super.addAll(entities);
		this.doSort();
		return ret;
	}

	@Override
	default <U extends T> U[] addAll(final U[] entities) {
		final U[] ret = SynchronizedEntityContainer.super.addAll(entities);
		this.doSort();
		return ret;
	};

}
