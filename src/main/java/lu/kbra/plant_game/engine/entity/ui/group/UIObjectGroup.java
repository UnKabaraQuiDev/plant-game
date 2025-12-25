package lu.kbra.plant_game.engine.entity.ui.group;

import java.awt.Shape;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.standalone.gameengine.objs.entity.Component;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.objs.entity.components.SubEntitiesComponent;

public class UIObjectGroup extends UIObject implements ObjectGroup<UIObject> {

	public static final Comparator<UIObject> INDEX_COMPARATOR = Comparator.comparingInt(b -> {
		if (b instanceof final IndexOwner ime) {
			return ime.getIndex();
		}
		return 0;
	});

	protected SubEntitiesComponent<UIObject> subEntitiesComponent;

	protected Shape bounds;

	protected Object parent;

	public UIObjectGroup(final String str, final List<UIObject> entities, final Component... cs) {
		super(str, null);
		super.addComponent(this.subEntitiesComponent = new SubEntitiesComponent<>(entities));
	}

	@SafeVarargs
	public UIObjectGroup(final String str, final UIObject... values) {
		super(str, null);
		super.addComponent(this.subEntitiesComponent = new SubEntitiesComponent<>(
				Arrays.stream(values).collect(Collectors.toCollection(ArrayList::new))));
	}

	public UIObjectGroup(final String str, final UIObjectGroup parent, final UIObject... values) {
		this(str, values);
		parent.add(this);
	}

	public UIObjectGroup(final String str, final UIScene parent, final UIObject... values) {
		this(str, values);
		parent.addEntity(this);
	}

	@Override
	public SubEntitiesComponent<UIObject> getSubEntitiesComponent() {
		return this.subEntitiesComponent;
	}

	@Override
	public List<UIObject> getSubEntities() {
		return this.subEntitiesComponent == null ? Collections.emptyList() : List.copyOf(this.subEntitiesComponent.getEntities());
	}

	@Override
	public Object getSubEntitiesLock() {
		return this.subEntitiesComponent == null ? null : this.subEntitiesComponent.getEntitiesLock();
	}

	@Override
	public boolean contains(final UIObject o) {
		synchronized (this.getSubEntitiesLock()) {
			return this.getSubEntities().contains(o);
		}
	}

	@Override
	public <V extends UIObject> V add(final V e) {
		synchronized (this.getSubEntitiesLock()) {
			this.getSubEntitiesComponent().getEntities().add(e);

			this.doSort();
		}
		if (e instanceof final ParentAware pa) {
			pa.setParent(this);
		}
		this.recomputeBounds();
		return e;
	}

	@Override
	public <V extends UIObject> V[] addAll(final V... e) {
		synchronized (this.getSubEntitiesLock()) {
			for (final V v : e) {
				this.getSubEntitiesComponent().getEntities().add(v);
				if (v instanceof final ParentAware pa) {
					pa.setParent(this);
				}
			}

			this.doSort();
		}

		this.recomputeBounds();
		return e;
	}

	@Override
	public boolean addAll(final Collection<? extends UIObject> c) {
		final boolean result;
		synchronized (this.getSubEntitiesLock()) {
			result = this.getSubEntitiesComponent().getEntities().addAll(c);

			this.doSort();
		}
		c.stream().filter(ParentAware.class::isInstance).forEach(b -> b.setParent(this));
		this.recomputeBounds();
		return result;
	}

	@Override
	public boolean addChildren(final ObjectGroup<? extends UIObject> c) {
		final boolean result;
		synchronized (this.getSubEntitiesLock()) {
			final List<UIObject> list = this.getSubEntitiesComponent().getEntities();
			result = c.stream().map(list::add).collect(Collectors.reducing(false, (a, b) -> (a || b)));

			this.doSort();
		}
		c.getSubEntities().stream().filter(ParentAware.class::isInstance).forEach(b -> b.setParent(this));
		this.recomputeBounds();
		return result;
	}

	@Override
	public void doSort() {
		this.getSubEntitiesComponent().sort(INDEX_COMPARATOR);
	}

	@Override
	public int size() {
		synchronized (this.getSubEntitiesLock()) {
			return this.getSubEntities().size();
		}
	}

	@Override
	public UIObject get(final int index) {
		synchronized (this.getSubEntitiesLock()) {
			return this.getSubEntities().get(index);
		}
	}

	@Override
	public Stream<UIObject> stream() {
		synchronized (this.getSubEntitiesLock()) {
			return this.getSubEntities().stream();
		}
	}

	@Override
	public Stream<UIObject> parallelStream() {
		synchronized (this.getSubEntitiesLock()) {
			return this.getSubEntities().parallelStream();
		}
	}

	@Override
	public Iterator<UIObject> iterator() {
		synchronized (this.getSubEntitiesLock()) {
			return this.getSubEntities().iterator();
		}
	}

	@Override
	public void setParent(final Object e) {
		this.parent = e;
	}

	@Override
	public Object getParent() {
		return this.parent;
	}

	@Override
	public Shape getBounds() {
		if (this.bounds == null) {
			this.recomputeBounds();
		}
		return this.bounds;
	}

	public void recomputeBounds() {
		final Area combined = new Area();
		synchronized (this.getSubEntitiesLock()) {
			this.getSubEntities().forEach(se -> {
				if (se instanceof final UIObjectGroup objGroup) {
					objGroup.recomputeBounds();
				}
				combined.add(new Area(se.getTransformedBounds()));
			});
		}
		this.bounds = combined;
	}

	@Override
	public String toString() {
		return "UIObjectGroup [subEntitiesComponent=" + this.subEntitiesComponent + ", bounds=" + this.bounds + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
