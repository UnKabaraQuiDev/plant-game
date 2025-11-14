package lu.kbra.plant_game.engine.scene.ui;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
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
	public void setLayout(final Layout layout) {
		this.layout = layout;
	}

	@Override
	public Layout getLayout() {
		return this.layout;
	}

	@Override
	public void doLayout() {
		this.layout.doLayout(this.getSubEntities(), this.getSceneParent().getCamera().getProjection().getAspectRatio());
		this.recomputeBounds();
	}

}
