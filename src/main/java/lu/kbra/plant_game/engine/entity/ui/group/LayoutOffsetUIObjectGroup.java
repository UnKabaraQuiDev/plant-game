package lu.kbra.plant_game.engine.entity.ui.group;

import java.util.Collection;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutParent;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.objs.entity.SceneParentAware;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class LayoutOffsetUIObjectGroup extends OffsetUIObjectGroup implements LayoutParent, SceneParentAware {

	protected Layout layout;

	public LayoutOffsetUIObjectGroup(final String str, final Layout layout, final UIObject... values) {
		super(str, values);
		this.setLayout(layout);
	}

	public LayoutOffsetUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final UIObject... values) {
		super(str, transform, values);
		this.setLayout(layout);
	}

	public LayoutOffsetUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final UIObject... values) {
		super(str, parent, values);
		this.setLayout(layout);
	}

	@Override
	public <V extends UIObject> V add(final V e) {
		final V v = super.add(e);
		this.doLayout();
		return v;
	}

	@Override
	public <V extends UIObject> boolean addAll(final Collection<? extends V> c) {
		final boolean v = super.addAll(c);
		this.doLayout();
		return v;
	}

	@Override
	public <V extends UIObject> V[] addAll(final V... e) {
		final V[] v = super.addAll(e);
		this.doLayout();
		return v;
	}

	@Override
	public <V extends UIObject> boolean addChildren(final ObjectGroup<? extends V> c) {
		final boolean v = super.addChildren(c);
		this.doLayout();
		return v;
	}

	@Override
	public void setLayout(final Layout layout) {
		this.layout = layout;
		if (layout instanceof final ParentAware pa) {
			pa.setParent(this);
		}
	}

	@Override
	public Layout getLayout() {
		return this.layout;
	}

	@Override
	public void doLayout() {
		synchronized (this.getSubEntitiesLock()) {
			this.getSubEntities().stream().forEach(e -> {
				if (e instanceof final LayoutParent lp) {
					lp.doLayout();
				}
			});
		}
		if (this.layout != null) {
			this.layout.doLayout(this.getSubEntities());
		}
		this.recomputeBounds();
	}

}
