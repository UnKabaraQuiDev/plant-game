package lu.kbra.plant_game.vanilla.scene.overlay.group.impl;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class MarginAnchoredUIObjectGroup extends AnchoredLayoutUIObjectGroup implements MarginOwner {

	protected float margin;

	public MarginAnchoredUIObjectGroup(final String str, final Layout layout, final Anchor obj, final Anchor tar,
			final UIObject... values) {
		super(str, layout, obj, tar, values);
	}

	public MarginAnchoredUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final UIObject... values) {
		super(str, layout, transform, values);
	}

	public MarginAnchoredUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Anchor obj,
			final Anchor tar, final UIObject... values) {
		super(str, layout, parent, obj, tar, values);
	}

	public MarginAnchoredUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final UIObject... values) {
		super(str, layout, parent, values);
	}

	public MarginAnchoredUIObjectGroup(final String str, final Layout layout, final Anchor obj, final Anchor tar, final float margin) {
		super(str, layout, obj, tar);
		this.margin = margin;
	}

	public MarginAnchoredUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Anchor obj,
			final Anchor tar, final float margin) {
		super(str, layout, parent, obj, tar);
		this.margin = margin;
	}

	@Override
	public float getMargin() {
		return this.margin;
	}

	@Override
	public void setMargin(final float margin) {
		this.margin = margin;
	}

	@Override
	public String toString() {
		return "MarginAnchoredUIObjectGroup@" + System.identityHashCode(this) + " [margin=" + this.margin + ", objectAnchor="
				+ this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", layout=" + this.layout + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds=" + this.computedBounds + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
