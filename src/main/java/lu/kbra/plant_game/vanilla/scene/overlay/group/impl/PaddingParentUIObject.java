package lu.kbra.plant_game.vanilla.scene.overlay.group.impl;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.PaddingOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class PaddingParentUIObject extends ParentUIObjectGroup implements PaddingOwner {

	protected float padding;

	public PaddingParentUIObject(final String str, final Layout layout, final Transform3D transform, final UIObject... values) {
		super(str, layout, transform, values);
	}

	public PaddingParentUIObject(final String str, final Layout layout, final UIObject... values) {
		super(str, layout, values);
	}

	public PaddingParentUIObject(final String str, final Layout layout, final UIObjectGroup parent, final UIObject... values) {
		super(str, layout, parent, values);
	}

	public PaddingParentUIObject(final String str, final Layout layout, final Transform3D transform, final float padding) {
		super(str, layout, transform);
		this.padding = padding;
	}

	public PaddingParentUIObject(final String str, final Layout layout, final UIObjectGroup parent, final float padding) {
		super(str, layout, parent);
		this.padding = padding;
	}

	@Override
	public float getPadding() {
		return this.padding;
	}

	@Override
	public void setPadding(final float p) {
		this.padding = p;
	}

	@Override
	public String toString() {
		return "PaddingParentUIObject@" + System.identityHashCode(this) + " [padding=" + this.padding + ", layout=" + this.layout
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
