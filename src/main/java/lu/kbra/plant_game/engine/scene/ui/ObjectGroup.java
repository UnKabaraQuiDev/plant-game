package lu.kbra.plant_game.engine.scene.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lu.kbra.standalone.gameengine.objs.entity.Component;
import lu.kbra.standalone.gameengine.objs.entity.Entity;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;
import java.util.Collections;

public class ObjectGroup<T extends Entity> extends Entity implements Iterable<T> {

	private SubEntitiesComponent<T> subEntitiesComponent;

	public ObjectGroup(String str, Component... cs) {
		super(str, cs);
		super.addComponent(this.subEntitiesComponent = new SubEntitiesComponent<>());
	}

	public ObjectGroup(String str, List<T> entities, Component... cs) {
		super(str, cs);
		super.addComponent(this.subEntitiesComponent = new SubEntitiesComponent<>(entities));
	}

	@SafeVarargs
	public ObjectGroup(String str, T... values) {
		super(str);
		super.addComponent(this.subEntitiesComponent = new SubEntitiesComponent<>(
				Arrays.stream(values).collect(Collectors.toCollection(ArrayList::new))));
	}

	public SubEntitiesComponent<T> getSubEntitiesComponent() {
		return subEntitiesComponent;
	}

	public List<T> getSubEntities() {
		return subEntitiesComponent == null ? Collections.emptyList() : subEntitiesComponent.getEntities();
	}

	public boolean contains(T o) {
		return getSubEntities().contains(o);
	}

	public boolean add(T e) {
		return getSubEntities().add(e);
	}

	public boolean addAll(Collection<? extends T> c) {
		return getSubEntities().addAll(c);
	}

	public boolean addAll(ObjectGroup<? extends T> c) {
		return getSubEntities().addAll(c.getSubEntities());
	}

	public T get(int index) {
		return getSubEntities().get(index);
	}

	public Stream<T> stream() {
		return getSubEntities().stream();
	}

	@Override
	public Iterator<T> iterator() {
		return getSubEntities().iterator();
	}

	@Override
	public String toString() {
		return "ObjectGroup [subEntitiesComponent=" + subEntitiesComponent + ", getSubEntities()=" + getSubEntities() + "]";
	}

}
