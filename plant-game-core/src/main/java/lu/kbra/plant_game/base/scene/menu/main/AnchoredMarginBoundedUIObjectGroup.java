package lu.kbra.plant_game.base.scene.menu.main;

import lu.kbra.plant_game.base.scene.overlay.group.impl.BoundedUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredMarginBoundedUIObjectGroup extends BoundedUIObjectGroup implements MarginOwner, AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;
	protected float margin;

	public AnchoredMarginBoundedUIObjectGroup(
			final String str,
			final Layout layout,
			final Direction2d dir,
			final Anchor objectAnchor,
			final Anchor targetAnchor,
			final float margin,
			final UIObject... values) {
		super(str, layout, dir, values);
		this.objectAnchor = objectAnchor;
		this.targetAnchor = targetAnchor;
		this.margin = margin;
	}

	public AnchoredMarginBoundedUIObjectGroup(
			final String str,
			final Layout layout,
			final Transform3D transform,
			final Direction2d dir,
			final Anchor objectAnchor,
			final Anchor targetAnchor,
			final float margin,
			final UIObject... values) {
		super(str, layout, transform, dir, values);
		this.objectAnchor = objectAnchor;
		this.targetAnchor = targetAnchor;
		this.margin = margin;
	}

	public AnchoredMarginBoundedUIObjectGroup(
			final String str,
			final Layout layout,
			final UIObjectGroup parent,
			final Direction2d dir,
			final Anchor objectAnchor,
			final Anchor targetAnchor,
			final float margin,
			final UIObject... values) {
		super(str, layout, parent, dir, values);
		this.objectAnchor = objectAnchor;
		this.targetAnchor = targetAnchor;
		this.margin = margin;
	}

	public AnchoredMarginBoundedUIObjectGroup(
			final String str,
			final Layout layout,
			final UIObjectGroup parent,
			final Transform3D transform,
			final Direction2d dir,
			final Anchor objectAnchor,
			final Anchor targetAnchor,
			final float margin,
			final UIObject... values) {
		super(str, layout, parent, transform, dir, values);
		this.objectAnchor = objectAnchor;
		this.targetAnchor = targetAnchor;
		this.margin = margin;
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor objectAnchor) {
		this.objectAnchor = objectAnchor;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor targetAnchor) {
		this.targetAnchor = targetAnchor;
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
		return "AnchoredMarginBoundedUIObjectGroup@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor
				+ ", targetAnchor=" + this.targetAnchor + ", margin=" + this.margin + ", dir=" + this.dir + ", bounds=" + this.bounds
				+ ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities
				+ ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
