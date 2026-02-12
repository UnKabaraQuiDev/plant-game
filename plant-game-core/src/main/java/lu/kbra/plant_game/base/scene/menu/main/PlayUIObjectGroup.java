package lu.kbra.plant_game.base.scene.menu.main;

import java.awt.geom.Rectangle2D;
import java.util.Optional;

import org.joml.Vector2f;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.base.scene.overlay.group.impl.AnchoredFixedPBUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.impl.BoundedUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.LimitedObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.DebugBoundsColor;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsBoundsInput;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;

public class PlayUIObjectGroup extends AnchoredFixedPBUIObjectGroup
		implements NeedsBoundsInput, LimitedObjectGroup<UIObject>, DebugBoundsColor, MarginOwner {

	protected float scrollSpeed = 0.8f;

	protected float scrollX;
	protected Vector2f scrollXRange = new Vector2f();

	protected float margin;
	protected Anchor2D contentAnchor = Anchor2D.CENTER;

	public PlayUIObjectGroup(final UIObjectGroup parent) {
		super("play.hori", null, parent, Direction2d.HORIZONTAL, 2f, Anchor.CENTER_CENTER, Anchor.CENTER_CENTER);
		super.add(new BoundedUIObjectGroup(this.getId() + "-content", new FlowLayout(false, true, 0.5f, false), Direction2d.HORIZONTAL));
		this.setMargin(0.4f);
	}

	public UIObjectGroup getContent() {
		return (UIObjectGroup) this.get(0);
	}

	@Override
	public void doLayout() {
		this.applyScrollX();
		super.doLayout();
	}

	@Override
	public boolean boundsInput(final WindowInputHandler inputHandler) {
		if (inputHandler.getMouseScroll().y() == 0) {
			return false;
		}

		this.addScrollX((float) inputHandler.getMouseScroll().y() * this.scrollSpeed);
		this.applyScrollX();

		return true;
	}

	public void recomputeRange() {
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty()) {
			return;
		}
		final Rectangle2D contentBounds = this.getContent().getLocalTransformedBounds().getBounds2D();
		final Rectangle2D parentBounds = this.getBounds().getBounds2D();
		if (parentBounds.getWidth() > contentBounds.getWidth()) { // should probably include margin in calculations
			switch (this.contentAnchor) {
			case LEADING -> this.scrollXRange.set(parentBounds.getMinX() - contentBounds.getMinX() + this.getMargin(),
					parentBounds.getMinX() - contentBounds.getMinX() + this.getMargin()); // untested
			case CENTER -> this.scrollXRange.set(parentBounds.getCenterX() - contentBounds.getCenterX(),
					parentBounds.getCenterX() - contentBounds.getCenterX());
			case TRAILING -> this.scrollXRange.set(parentBounds.getMaxX() - contentBounds.getMaxX() - this.getMargin(),
					parentBounds.getMaxX() - contentBounds.getMaxX() - this.getMargin());
			}
		} else {
			this.scrollXRange.set(parentBounds.getMinX() - contentBounds.getMinX(), parentBounds.getMaxX() - contentBounds.getMaxX());
		}
	}

	public void addScrollX(final float f) {
		this.setScrollX(this.scrollX + f);
	}

	public void setScrollX(final float f) {
//		this.recomputeRange();
		this.scrollX = f; // PCUtils.clampRange(this.scrollXRange.x(), this.scrollXRange.y(), this.scrollX);
		this.applyScrollX();
	}

	public void applyScrollX() {
		this.recomputeRange();
		this.scrollX = PCUtils.clampRange(this.scrollXRange.x(), this.scrollXRange.y(), this.scrollX);
		this.getContent().getTransform().translationSetX(this.scrollX).update();
	}

	@Override
	public float getMargin() {
		return this.margin;
	}

	@Override
	public void setMargin(final float m) {
		this.margin = m;
	}

	@Override
	public ColorMaterial getBoundsColor() {
		return ColorMaterial.ORANGE;
	}

	@Override
	public int getMaxItems() {
		return 1;
	}

	@Override
	public String toString() {
		return "PlayUIObjectGroup@" + System.identityHashCode(this) + " [scrollSpeed=" + this.scrollSpeed + ", scrollX=" + this.scrollX
				+ ", scrollXRange=" + this.scrollXRange + ", margin=" + this.margin + ", contentAnchor=" + this.contentAnchor
				+ ", objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", dir=" + this.dir + ", bounds="
				+ this.bounds + ", size=" + this.size + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock
				+ ", subEntities=" + this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform
				+ ", active=" + this.active + ", name=" + this.name + "]";
	}

}
