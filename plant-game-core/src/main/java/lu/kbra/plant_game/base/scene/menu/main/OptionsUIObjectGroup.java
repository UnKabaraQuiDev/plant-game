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

public class OptionsUIObjectGroup extends AnchoredFixedPBUIObjectGroup
		implements NeedsBoundsInput, LimitedObjectGroup<UIObject>, DebugBoundsColor, MarginOwner {

	protected float scrollSpeed = 0.15f;

	protected float scrollY;
	protected Vector2f scrollYRange = new Vector2f();

	protected float margin;
	protected Anchor2D contentAnchor = Anchor2D.TRAILING;

	public OptionsUIObjectGroup(final UIObjectGroup parent) {
		super("options.vert", null, parent, Direction2d.VERTICAL, 3f, Anchor.BOTTOM_LEFT, Anchor.BOTTOM_LEFT);
		super.add(new BoundedUIObjectGroup(this.getId() + "-content", new FlowLayout(true, 0.04f, Anchor2D.LEADING), Direction2d.VERTICAL));
		this.setMargin(0.05f);
	}

	public UIObjectGroup getContent() {
		return (UIObjectGroup) this.get(0);
	}

	@Override
	public void doLayout() {
		this.applyScrollY();
		super.doLayout();
	}

	@Override
	public boolean boundsInput(final WindowInputHandler inputHandler) {
		if (inputHandler.getMouseScroll().y() == 0) {
			return false;
		}

		this.addScrollY((float) inputHandler.getMouseScroll().y() * this.scrollSpeed);
		this.applyScrollY();

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
			case LEADING -> this.scrollYRange.set(parentBounds.getMinY() - contentBounds.getMinY() + this.getMargin(),
					parentBounds.getMinY() - contentBounds.getMinY() + this.getMargin()); // untested
			case CENTER -> this.scrollYRange.set(parentBounds.getCenterY() - contentBounds.getCenterY(),
					parentBounds.getCenterY() - contentBounds.getCenterY());
			case TRAILING -> this.scrollYRange.set(parentBounds.getMaxY() - contentBounds.getMaxY() - this.getMargin(),
					parentBounds.getMaxY() - contentBounds.getMaxY() - this.getMargin());
			}
		} else {
			this.scrollYRange.set(parentBounds.getMinY() - contentBounds.getMinY(), parentBounds.getMaxY() - contentBounds.getMaxY());
		}
	}

	public void addScrollY(final float f) {
		this.setScrollY(this.scrollY + f);
	}

	public void setScrollY(final float f) {
//		this.recomputeRange();
		this.scrollY = f; // PCUtils.clampRange(this.scrollXRange.x(), this.scrollXRange.y(), this.scrollX);
		this.applyScrollY();
	}

	public void applyScrollY() {
		if (this.scrollYRange == null) {
			return;
		}
		this.recomputeRange();
		this.scrollY = PCUtils.clampRange(this.scrollYRange.x(), this.scrollYRange.y(), this.scrollY);
		this.getContent().getTransform().translationSetZ(this.scrollY).update();
	}

	@Override
	public int getMaxItems() {
		return 1;
	}

	@Override
	public ColorMaterial getBoundsColor() {
		return ColorMaterial.YELLOW;
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
		return "OptionsUIObjectGroup@" + System.identityHashCode(this) + " [scrollSpeed=" + this.scrollSpeed + ", scrollY=" + this.scrollY
				+ ", scrollYRange=" + this.scrollYRange + ", dir=" + this.dir + ", bounds=" + this.bounds + ", objectAnchor="
				+ this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", layout=" + this.layout + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds=" + this.computedBounds + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
