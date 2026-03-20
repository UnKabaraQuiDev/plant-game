package lu.kbra.plant_game.engine.entity.impl;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lu.kbra.pclib.PCUtils;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.scene.EntityContainer;

public interface SynchronizedEntityContainer<B extends SceneEntity> extends EntityContainer<B>, ParentAwareNode {

	Object getEntitiesLock();

	List<B> getWEntities();

	default List<B> getROEntities() {
		synchronized (this.getEntitiesLock()) {
			return List.copyOf(this.getWEntities());
		}
	}

	@Override
	default void forEach(final Consumer<? super B> action) {
		this.getROEntities().forEach(action);
	}

	default void forEachSynchronized(final Consumer<? super B> action) {
		synchronized (this.getEntitiesLock()) {
			this.forEach(action);
		}
	}

	default void sort(final Comparator<B> comp) {
		synchronized (this.getEntitiesLock()) {
			this.getWEntities().sort(comp);
		}
	}

	@Override
	default <T extends B> T add(final T entity) {
		assert !this.getParents().contains(entity) : "Child cannot be parent.";
		if (entity == null) {
			return null;
		}
		synchronized (this.getEntitiesLock()) {
			this.getWEntities().add(entity);
		}
		if (entity instanceof final ParentAwareNode pa) {
			ParentAwareComponent.checkHierarchy(this, pa);
			pa.setParent(this);
		}
		return entity;
	}

	@Override
	default <T extends B> T[] addAll(final T... entity) {
		if (entity == null || entity.length == 0) {
			return null;
		}
		synchronized (this.getEntitiesLock()) {
			for (final T e : entity) {
				this.add(e);
			}
		}
		return entity;
	}

	@Override
	default Iterator<B> iterator() {
		return this.getWEntities().iterator();
	}

	@Override
	default <T extends B> boolean contains(final T e) {
		return this.getWEntities().stream().anyMatch(f -> f == e && f.getId().equals(e.getId()));
	}

	@Override
	default <T extends B> boolean contains(final String e) {
		return this.getWEntities().stream().anyMatch(f -> f.getId().equals(e));
	}

	@Override
	default <T extends B> boolean addAll(final Collection<? extends T> entities) {
		final HashSet<String> strs = entities.parallelStream().map(T::getId).collect(Collectors.toCollection(HashSet::new));
		synchronized (this.getEntitiesLock()) {
			final boolean addedAny = this.getWEntities().parallelStream().anyMatch(c -> strs.contains(c.getId()));
			entities.forEach(this::add);
			return addedAny;
		}
	}

	@Override
	default int size() {
		return this.getWEntities().size();
	}

	@Override
	default <T extends SceneEntity> T getEntity(final String str) {
		return (T) this.getWEntities().parallelStream().filter(c -> c.getId().equals(str)).findFirst().orElse(null);
	}

	@Override
	default <T extends B> Optional<T> remove(final T e) {
		synchronized (this.getEntitiesLock()) {
			if (e == null) {
				return Optional.empty();
			}
			final T old = (T) this.getWEntities().parallelStream().filter(c -> c.getId().equals(e.getId())).findFirst().orElse(null);
			if (old != null) {
				if (old instanceof final ParentAwareNode pa) {
					ParentAwareComponent.checkHierarchy(this, pa);
					pa.setParent(null);
				}
				return Optional.of(old);
			}
		}
		return Optional.empty();
	}

	@Override
	default <T extends B, O extends B> Optional<O> replace(final O old, final T new_) {
		synchronized (this.getEntitiesLock()) {
			if (old == null) {
				this.add(new_);
				return Optional.empty();
			}
			final O found = (O) this.getWEntities().parallelStream().filter(c -> c.getId().equals(old.getId())).findFirst().orElse(null);
			if (found == null) {
				this.add(new_);
				return Optional.empty();
			}
			if (found != old) {
				throw new IllegalStateException("Found value and given old values do not match (" + PCUtils.toSimpleIdentityString(found)
						+ " <> " + PCUtils.toSimpleIdentityString(old) + ").");
			}
			this.add(new_);
			return Optional.of(found);
		}
	}

	@Override
	default Stream<B> stream() {
		return this.getROEntities().stream();
	}

	@Override
	default Stream<B> parallelStream() {
		return this.getROEntities().stream();
	}

}
