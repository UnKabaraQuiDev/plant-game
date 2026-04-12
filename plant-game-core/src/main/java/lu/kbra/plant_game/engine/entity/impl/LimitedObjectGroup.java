package lu.kbra.plant_game.engine.entity.impl;

import java.util.Collection;

import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;

public interface LimitedObjectGroup<T extends SceneEntity> extends ObjectGroup<T> {

	int getMaxItems();

	@Override
	default <V extends T> V add(final V e) {
		this.checkItemCount(1);
		return ObjectGroup.super.add(e);
	}

	default void checkItemCount(final int addCount) {
		final int MAX_ITEMS = this.getMaxItems();
		if (this.size() >= MAX_ITEMS || this.size() + addCount > MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " items: " + this.getROEntities());
		}
	}

	@Override
	default <V extends T> boolean addAll(final Collection<? extends V> c) {
		this.checkItemCount(c.size());
		return ObjectGroup.super.addAll(c);
	}

	@Override
	default <V extends T> V[] addAll(final V... e) {
		this.checkItemCount(e.length);
		return ObjectGroup.super.addAll(e);
	}

	@Override
	default <V extends T> boolean addChildren(final ObjectGroup<? extends V> c) {
		this.checkItemCount(c.size());
		return ObjectGroup.super.addAll(c.getROEntities());
	}

}
