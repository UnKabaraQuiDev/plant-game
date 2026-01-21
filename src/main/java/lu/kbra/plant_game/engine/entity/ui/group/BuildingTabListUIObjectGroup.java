package lu.kbra.plant_game.engine.entity.ui.group;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.BuildingTabButtonUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.AnchoredLayoutUIObjectGroup;

public class BuildingTabListUIObjectGroup extends AnchoredLayoutUIObjectGroup implements BoundsOwnerParentAware {

	protected Rectangle2D.Float fixedBounds;
	protected float boundsMarginX = 0f;
	protected float boundsMarginY = 0f;
	protected float fixedHeight = 0.1f;

//	protected Map<String, BuildingTabButtonUIObjectGroup> tabButtons = new HashMap<>();

	public BuildingTabListUIObjectGroup() {
		super("building-tab-list", new FlowLayout(false, 0f, true), Anchor.TOP_CENTER, Anchor.TOP_CENTER);
		this.fixedBounds = new Rectangle2D.Float(0, 0, 1, 1);
	}

	@Override
	public boolean recomputeBounds() {
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty()) {
			return false;
		}
		final Rectangle2D superBounds = obo.get().getBounds().getBounds2D();
		final Rectangle2D.Float newBounds = new Rectangle2D.Float((float) superBounds.getMinX() + this.boundsMarginX,
				-this.fixedHeight / 2 + this.boundsMarginY,
				(float) superBounds.getWidth() - 2 * this.boundsMarginX,
				this.fixedHeight - 2 * this.boundsMarginY);
		final boolean changed = newBounds.equals(this.fixedBounds);
		this.fixedBounds.setFrame(newBounds);
		return changed;
	}

	public Optional<BuildingTabButtonUIObjectGroup> getButton(final String titleKey) {
		return this.stream()
				.filter(t -> t instanceof BuildingTabButtonUIObjectGroup btn && btn.getTitleKey().equals(titleKey))
				.map(BuildingTabButtonUIObjectGroup.class::cast)
				.findFirst();
	}

	@Override
	public void doLayout() {
		this.recomputeBounds();
		super.doLayout();
	}

	@Override
	public Shape getBounds() {
		return this.fixedBounds;
	}

	public float getBoundsMarginX() {
		return this.boundsMarginX;
	}

	public void setBoundsMarginX(final float boundsMarginX) {
		this.boundsMarginX = boundsMarginX;
	}

	public float getBoundsMarginY() {
		return this.boundsMarginY;
	}

	public void setBoundsMarginY(final float boundsMarginY) {
		this.boundsMarginY = boundsMarginY;
	}

	public float getFixedHeight() {
		return this.fixedHeight;
	}

	public void setFixedHeight(final float fixedHeight) {
		this.fixedHeight = fixedHeight;
	}

	@Override
	public String toString() {
		return "BuildingTabListUIObjectGroup [fixedBounds=" + this.fixedBounds + ", boundsMarginX=" + this.boundsMarginX
				+ ", boundsMarginY=" + this.boundsMarginY + ", fixedHeight=" + this.fixedHeight + ", objectAnchor=" + this.objectAnchor
				+ ", targetAnchor=" + this.targetAnchor + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock
				+ ", subEntities=" + this.subEntities + ", bounds=" + this.computedBounds + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
