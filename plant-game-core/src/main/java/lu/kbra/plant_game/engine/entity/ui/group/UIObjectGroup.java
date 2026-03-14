package lu.kbra.plant_game.engine.entity.ui.group;

import java.awt.Shape;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;

import lu.kbra.plant_game.base.scene.overlay.group.building.MinBoundsOwner;
import lu.kbra.plant_game.engine.entity.impl.NoMeshObject;
import lu.kbra.plant_game.engine.entity.impl.SynchronizedEntityContainer;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.IgnoreBounds;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.Margin2DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.ObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.Padding2DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.PaddingOwner;
import lu.kbra.plant_game.engine.scene.ui.UIScene;

public class UIObjectGroup extends UIObject implements ObjectGroup<UIObject>, NoMeshObject, SynchronizedEntityContainer<UIObject> {

	public static final Comparator<UIObject> INDEX_COMPARATOR = Comparator.comparingInt(b -> {
		if (b instanceof final IndexOwner ime) {
			return ime.getIndex();
		}
		return 0;
	});
	public static final Comparator<UIObject> UI_OBJECT_COMPARATOR = INDEX_COMPARATOR
			.thenComparingDouble(b -> b.hasTransform() ? b.getTransform().getTranslation().y() : 0);

	protected static final ToDoubleFunction<? super UIObject> marginSumX = c -> {
		float padding = 0;
		if (c instanceof MarginOwner po) {
			padding += po.getMargin();
		}
		if (c instanceof Margin2DOwner po) {
			padding += po.getMarginX();
		}
		return padding;
	};
	protected static final ToDoubleFunction<? super UIObject> marginSumZ = c -> {
		float padding = 0;
		if (c instanceof MarginOwner po) {
			padding += po.getMargin();
		}
		if (c instanceof Margin2DOwner po) {
			padding += po.getMarginZ();
		}
		return padding;
	};

	protected static final ToDoubleFunction<? super UIObject> paddingSumX = c -> {
		float padding = 0;
		if (c instanceof PaddingOwner po) {
			padding += po.getPadding();
		}
		if (c instanceof Padding2DOwner po) {
			padding += po.getPaddingX();
		}
		return padding;
	};
	protected static final ToDoubleFunction<? super UIObject> paddingSumZ = c -> {
		float padding = 0;
		if (c instanceof PaddingOwner po) {
			padding += po.getPadding();
		}
		if (c instanceof Padding2DOwner po) {
			padding += po.getPaddingZ();
		}
		return padding;
	};

	protected final Object subEntitiesLock = new Object();
	protected List<UIObject> subEntities = Collections.synchronizedList(new ArrayList<>());

	protected Shape computedBounds;

	public UIObjectGroup(final String str, final List<UIObject> entities) {
		super(str);
	}

	@SafeVarargs
	public UIObjectGroup(final String str, final UIObject... values) {
		super(str);
		this.addAll(values);
	}

	public UIObjectGroup(final String str, final UIObjectGroup parent, final UIObject... values) {
		this(str, values);
		parent.add(this);
	}

	public UIObjectGroup(final String str, final UIScene parent, final UIObject... values) {
		this(str, values);
		parent.add(this);
	}

	@Override
	public UIObject get(final int index) {
		synchronized (this.getEntitiesLock()) {
			return this.getWEntities().get(index);
		}
	}

	@Override
	public <V extends UIObject> boolean addChildren(final ObjectGroup<? extends V> c) {
		if (c == null || c.size() == 0) {
			return false;
		}
		final boolean a = this.addAll(c.getROEntities());
		this.recomputeBounds();
		return a;
	}

	@Override
	public <T extends UIObject> T add(final T entity) {
		final T a = ObjectGroup.super.add(entity);
		this.recomputeBounds();
		return a;
	}

	@Override
	public <T extends UIObject> boolean addAll(final Collection<? extends T> entities) {
		if (entities == null || entities.size() == 0) {
			return false;
		}
		final boolean a = ObjectGroup.super.addAll(entities);
		this.recomputeBounds();
		return a;
	}

	@Override
	public <T extends UIObject> T[] addAll(final T... entity) {
		if (entity == null || entity.length == 0) {
			return null;
		}
		final T[] a = ObjectGroup.super.addAll(entity);
		this.recomputeBounds();
		return a;
	}

	@Override
	public void doSort() {
		ObjectGroup.super.sort(UI_OBJECT_COMPARATOR);
	}

	@Override
	public Object getEntitiesLock() {
		return this.subEntitiesLock;
	}

	@Override
	public List<UIObject> getWEntities() {
		return this.subEntities;
	}

	public boolean recomputeBounds() {
		final Area combined = new Area();
		synchronized (this.getEntitiesLock()) {
			this.getWEntities().forEach(se -> {
				if (se instanceof IgnoreBounds || !se.isActive()) {
					return;
				}
				if (se instanceof final UIObjectGroup objGroup) {
					objGroup.recomputeBounds();
				}
				if (se instanceof MinBoundsOwner mbo) {
					final Shape otherBounds = mbo.getTransformedMinBounds();
					combined.add(new Area(otherBounds));
					return;
				}
				final Shape otherBounds = se.getTransformedBounds();
				if (otherBounds == null) {
					return;
				}
				combined.add(new Area(otherBounds));
			});
		}
		final boolean changed = this.computedBounds == null || !this.computedBounds.equals(combined);
		this.computedBounds = combined;
		return changed;
	}

	@Override
	public Shape getBounds() {
		if (this.computedBounds == null) {
			this.recomputeBounds();
		}
		return this.computedBounds;
	}

	@Override
	public String toString() {
		return "UIObjectGroup@" + System.identityHashCode(this) + " [subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
