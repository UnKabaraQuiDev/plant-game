package lu.kbra.plant_game.engine.entity.ui.group;

import java.util.Collection;
import java.util.function.Supplier;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.ObjectGroup;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@Deprecated
public class LayoutScrollDrivenUIObjectGroup extends ScrollDrivenUIObjectGroup implements LayoutOwner {

	@Deprecated
	protected Layout layout;

	@Deprecated
	public LayoutScrollDrivenUIObjectGroup(
			final String str,
			final Transform3D transform,
			final Supplier<Float> scrollRatio,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, transform, scrollRatio, dir, margin, values);
		this.setLayout(layout);
	}

	@Deprecated
	public LayoutScrollDrivenUIObjectGroup(
			final String str,
			final UIObjectGroup parent,
			final Supplier<Float> scrollRatio,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, parent, scrollRatio, dir, margin, values);
		this.setLayout(layout);
	}

	@Deprecated
	public LayoutScrollDrivenUIObjectGroup(
			final String str,
			final Supplier<Float> scrollRatio,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, scrollRatio, dir, margin, values);
		this.setLayout(layout);
	}

	@Deprecated
	@Override
	public <V extends UIObject> V add(final V e) {
		final V v = super.add(e);
		this.getFirstParentMatching(LayoutOwner.class).ifPresent(LayoutOwner::doLayout);
		return v;
	}

	@Deprecated
	@Override
	public <V extends UIObject> boolean addAll(final Collection<? extends V> c) {
		final boolean v = super.addAll(c);
		this.getFirstParentMatching(LayoutOwner.class).ifPresent(LayoutOwner::doLayout);
		return v;
	}

	@Deprecated
	@Override
	public <V extends UIObject> V[] addAll(final V... e) {
		final V[] v = super.addAll(e);
		this.getFirstParentMatching(LayoutOwner.class).ifPresent(LayoutOwner::doLayout);
		return v;
	}

	@Deprecated
	@Override
	public <V extends UIObject> boolean addChildren(final ObjectGroup<? extends V> c) {
		final boolean v = super.addChildren(c);
		this.getFirstParentMatching(LayoutOwner.class).ifPresent(LayoutOwner::doLayout);
		return v;
	}

	@Deprecated
	@Override
	public void setLayout(final Layout layout) {
		this.layout = layout;
		if (layout instanceof final ParentAwareNode pa) {
			ParentAwareComponent.checkHierarchy(this, pa);
			pa.setParent(this);
		}
	}

	@Deprecated
	@Override
	public Layout getLayout() {
		return this.layout;
	}

	@Deprecated
	@Override
	public void doLayout() {
		synchronized (this.getEntitiesLock()) {
			this.getWEntities().stream().forEach(e -> {
				if (e instanceof final LayoutOwner lp) {
					lp.doLayout();
				}
			});
			if (this.layout != null) {
				this.layout.doLayout(this.getWEntities());
			}
		}

		this.recomputeBounds();
	}

	@Deprecated
	@Override
	public String toString() {
		return "LayoutScrollDrivenUIObjectGroup [layout=" + this.layout + ", scrollRatio=" + this.scrollRatioSupplier + ", dir=" + this.dir
				+ ", active=" + this.active + ", name=" + this.name + ", size()=" + this.size() + "]";
	}

}
