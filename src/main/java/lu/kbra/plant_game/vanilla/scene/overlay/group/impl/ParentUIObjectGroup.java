package lu.kbra.plant_game.vanilla.scene.overlay.group.impl;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ParentUIObjectGroup extends LayoutOffsetUIObjectGroup implements BoundsOwnerParentAware {

	public ParentUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final UIObject... values) {
		super(str, layout, transform, values);
	}

	public ParentUIObjectGroup(final String str, final Layout layout, final UIObject... values) {
		super(str, layout, values);
	}

	public ParentUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final UIObject... values) {
		super(str, layout, parent, values);
	}

	@Override
	public void doLayout() {
		System.err.println("id: " + this.getId());
		super.doLayout();
	}

	@Override
	public Shape getBounds() {
		return this.getBoundsOwnerParent().map(BoundsOwner::getBounds).orElse(new Rectangle2D.Float(0, 0, 0, 0));
	}

}
