package lu.kbra.plant_game.engine.entity.ui.bar;

import java.util.Collection;

import lu.kbra.plant_game.engine.entity.ui.group.ObjectGroup;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;

public interface LimitedObjectGroup<T extends SceneEntity> extends ObjectGroup<T> {

	int getMaxItems();

	@Override
	default <V extends T> V add(final V e) {
		final int MAX_ITEMS = this.getMaxItems();
		if (this.size() >= MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return this.add(e);
	}

	@Override
	default <V extends T> boolean addAll(final Collection<? extends V> c) {
		final int MAX_ITEMS = this.getMaxItems();
		if (this.size() >= MAX_ITEMS || this.size() + c.size() > MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return this.addAll(c);
	}

	@Override
	default <V extends T> V[] addAll(final V... e) {
		final int MAX_ITEMS = this.getMaxItems();
		if (this.size() >= MAX_ITEMS || this.size() + e.length > MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return this.addAll(e);
	}

	@Override
	default <V extends T> boolean addChildren(final ObjectGroup<? extends V> c) {
		final int MAX_ITEMS = this.getMaxItems();
		if (this.size() >= MAX_ITEMS || this.size() + c.size() > MAX_ITEMS) {
			throw new UnsupportedOperationException("Max. " + MAX_ITEMS + " columns: " + this.getSubEntities());
		}
		return this.addChildren(c);
	}

}
