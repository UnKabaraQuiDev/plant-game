package lu.kbra.plant_game.engine.entity.ui.group;

import lu.kbra.plant_game.engine.entity.impl.AnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredLayoutUIObjectGroup extends LayoutOffsetUIObjectGroup implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredLayoutUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final UIObject... values) {
		super(str, layout, transform, values);
	}

	public AnchoredLayoutUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final UIObject... values) {
		super(str, layout, parent, values);
	}

	public AnchoredLayoutUIObjectGroup(
			final String str,
			final Layout layout,
			final UIObjectGroup parent,
			final Anchor obj,
			final Anchor tar,
			final UIObject... values) {
		super(str, layout, parent, values);
		this.objectAnchor = obj;
		this.targetAnchor = tar;
	}

	public AnchoredLayoutUIObjectGroup(
			final String str,
			final Layout layout,
			final Anchor obj,
			final Anchor tar,
			final UIObject... values) {
		super(str, layout, values);
		this.objectAnchor = obj;
		this.targetAnchor = tar;
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor a) {
		this.objectAnchor = a;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor a) {
		this.targetAnchor = a;
	}

	@Override
	public String toString() {
		return "AnchoredLayoutUIObjectGroup [objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", layout="
				+ this.layout + ", transform=" + this.transform + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", bounds=" + this.computedBounds + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
