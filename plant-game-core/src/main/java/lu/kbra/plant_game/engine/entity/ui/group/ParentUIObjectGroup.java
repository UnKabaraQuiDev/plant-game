package lu.kbra.plant_game.engine.entity.ui.group;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.impl.PaddingOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ParentUIObjectGroup extends LayoutOffsetUIObjectGroup implements BoundsOwnerParentAware, PaddingOwner {

	protected float padding;

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
	public Shape getBounds() {
		return this.getBoundsOwnerParent().map(BoundsOwner::getBounds).orElse(new Rectangle2D.Float(0, 0, 0, 0));
	}

	@Override
	public float getPadding() {
		return this.padding;
	}

	@Override
	public void setPadding(final float padding) {
		this.padding = padding;
	}

	@Override
	public String toString() {
		return "ParentUIObjectGroup@" + System.identityHashCode(this) + " [padding=" + this.padding + ", layout=" + this.layout
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
