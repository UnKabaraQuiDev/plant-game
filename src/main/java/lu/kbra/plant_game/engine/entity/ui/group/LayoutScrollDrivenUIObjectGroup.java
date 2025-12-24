package lu.kbra.plant_game.engine.entity.ui.group;

import java.util.function.Supplier;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutParent;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class LayoutScrollDrivenUIObjectGroup extends ScrollDrivenUIObjectGroup implements LayoutParent {

	protected Layout layout;

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
				if (e instanceof final UIObjectGroup objGroup) {
					objGroup.recomputeBounds();
				}
			});
		}
		this.layout.doLayout(this.getSubEntities(), this.getSceneParent().getCamera().getProjection().getAspectRatio());
		this.recomputeBounds();
	}

	@Override
	public String toString() {
		return "LayoutScrollDrivenUIObjectGroup [layout=" + this.layout + ", scrollRatio=" + this.scrollRatioSupplier + ", dir=" + this.dir
				+ ", active=" + this.active + ", name=" + this.name + ", size()=" + this.size() + "]";
	}

}
