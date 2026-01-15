package lu.kbra.plant_game.vanilla.scene.overlay.group.impl;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredFixedPBUIObjectGroup extends FixedPBUIObjectGroup implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredFixedPBUIObjectGroup(final String str, final Layout layout, final Direction2d dir, final float size, final Anchor obj,
			final Anchor tar, final UIObject... values) {
		super(str, layout, dir, size, values);
		this.objectAnchor = obj;
		this.targetAnchor = tar;
	}

	public AnchoredFixedPBUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final Direction2d dir,
			final float size, final Anchor obj, final Anchor tar, final UIObject... values) {
		super(str, layout, transform, dir, size, values);
		this.objectAnchor = obj;
		this.targetAnchor = tar;
	}

	public AnchoredFixedPBUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Direction2d dir,
			final float size, final Anchor obj, final Anchor tar, final UIObject... values) {
		super(str, layout, parent, dir, size, values);
		this.objectAnchor = obj;
		this.targetAnchor = tar;
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
	public String toString() {
		return "AnchoredFixedPBUIObjectGroup@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", dir=" + this.dir + ", bounds=" + this.bounds + ", size=" + this.size + ", layout=" + this.layout
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
