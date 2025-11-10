package lu.kbra.plant_game.engine.scene.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import lu.kbra.standalone.gameengine.objs.entity.Entity;

public class ObjectGroup<T extends Entity> implements Iterable<T> {

	public final List<T> entities;

	public ObjectGroup(List<T> entities) {
		this.entities = entities;
	}

	@SafeVarargs
	public ObjectGroup(T... values) {
		this.entities = new ArrayList<>(values.length);
		for (T t : values) {
			this.entities.add(t);
		}
	}

	public void forEach(Consumer<? super T> action) {
		entities.forEach(action);
	}

	public boolean contains(T o) {
		return entities.contains(o);
	}

	public boolean add(T e) {
		return entities.add(e);
	}

	public boolean addAll(Collection<? extends T> c) {
		return entities.addAll(c);
	}

	public boolean addAll(ObjectGroup<? extends T> c) {
		return entities.addAll(c.entities);
	}

	public T get(int index) {
		return entities.get(index);
	}

	public Stream<T> stream() {
		return entities.stream();
	}

	@Override
	public Iterator<T> iterator() {
		return entities.iterator();
	}

}
