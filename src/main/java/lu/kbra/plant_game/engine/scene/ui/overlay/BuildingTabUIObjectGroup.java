package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.util.function.Supplier;

import lu.kbra.plant_game.engine.entity.ui.group.LayoutScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BuildingTabUIObjectGroup extends LayoutScrollDrivenUIObjectGroup implements IndexOwner, AnchorOwner {

	protected Anchor objectAnchor = Anchor.BOTTOM_CENTER;
	protected Anchor targetAnchor = Anchor.BOTTOM_CENTER;

	protected int index;

	public BuildingTabUIObjectGroup(final String str, final int index, final Supplier<Float> scroll) {
		super("building-tab-" + str, new Transform3D(), scroll, Direction.EAST, 0.02f, new FlowLayout(false, 0.05f));
		this.index = index;
	}

	@Override
	public void doLayout() {
		this.recomputeBounds();
		super.doLayout();
	}

	@Override
	public int getIndex() {
		return this.index;
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
		return "BuildingTabUIObjectGroup [index=" + this.index + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", bounds=" + this.bounds + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
