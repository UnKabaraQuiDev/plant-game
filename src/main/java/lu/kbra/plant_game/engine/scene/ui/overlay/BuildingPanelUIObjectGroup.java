package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;;

public class BuildingPanelUIObjectGroup extends AnchoredLayoutUIObjectGroup implements BoundsOwnerParentAware, NeedsInput {

	protected Rectangle2D.Float fixedBounds;
	protected float boundsMarginX = 0.05f;
	protected float boundsMarginY = 0f;
	protected float heightRatio = 1f / 10 * 2;
	protected float scroll = 0;

	public BuildingPanelUIObjectGroup() {
		super("building-tab", new AnchorLayout(), Anchor.BOTTOM_CENTER, Anchor.BOTTOM_CENTER);
		this.fixedBounds = new Rectangle2D.Float(0, 0, 1, 1);
	}

	@Override
	public void input(final WindowInputHandler inputHandler) {
		this.scroll += inputHandler.getMouseScroll().y() /* + inputHandler.getMouseScroll().x() */;
	}

	@Override
	public void recomputeBounds() {
		if (!this.hasBoundsOwnerParent()) {
			return;
		}
		final Rectangle2D superBounds = this.getBoundsOwnerParent().getBounds().getBounds2D();
		this.fixedBounds.setRect(superBounds.getMinX() + this.boundsMarginX,
				superBounds.getMaxY() - this.boundsMarginY,
				superBounds.getWidth() - 2 * this.boundsMarginX,
				superBounds.getHeight() * this.heightRatio);
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

	public float getHeightRatio() {
		return this.heightRatio;
	}

	public void setHeightRatio(final float heightRatio) {
		this.heightRatio = heightRatio;
	}

	@Override
	public String toString() {
		return "BuildingTabUIObjectGroup [objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", bounds=" + this.bounds + ", transform=" + this.transform
				+ ", active=" + this.active + ", name=" + this.name + "]";
	}

	public float getScroll() {
		return 0;
	}

}
