package lu.kbra.plant_game.engine.scene.world.modal;

import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class InspectBuildingUIObjectGroup extends LayoutOffsetUIObjectGroup implements MarginOwner {

	public static final String NAME = "inspect-building-popup";

	protected float margin = 0f;

	public InspectBuildingUIObjectGroup() {
		super(NAME, new FlowLayout(true, 0.02f, Anchor2D.LEADING), new Transform3D());
	}

	public Object init() {
		return null;
	}

	@Override
	public void setMargin(final float margin) {
		this.margin = margin;
	}

	@Override
	public float getMargin() {
		return this.margin;
	}

	@Override
	public String toString() {
		return "InspectBuildingUIObjectGroup@" + System.identityHashCode(this) + " [margin=" + this.margin + ", layout=" + this.layout
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
