package lu.kbra.plant_game.base.scene.overlay.group.building;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Optional;

import org.joml.Vector2f;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.base.scene.overlay.group.impl.AnchoredLayoutUIObjectGroup;
import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.LimitedObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.ObjectGroup;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.generated.ColorMaterial;

// TODO: switch this to BoundedUIObjectGroup
public class BuildingTabUIObjectGroup extends AnchoredLayoutUIObjectGroup
		implements IndexOwner, BoundsOwnerParentAware, LimitedObjectGroup<UIObject> {

	protected String titleKey;
	protected int index;
	protected float scrollX = 0;
	protected Vector2f scrollXRange = new Vector2f();
	protected Rectangle2D.Float fixedBounds = new Rectangle.Float();

	protected ColorMaterial accentColor;

	public BuildingTabUIObjectGroup(final String titleKey, final int index, final ColorMaterial accentColor) {
		super("building-tab-" + titleKey, null, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_CENTER);
		this.getTransform().translationAddY(0.1f);
		this.index = index;
		this.titleKey = titleKey;
		this.accentColor = accentColor;
		this.add(new LayoutOffsetUIObjectGroup(this.getId() + "-container", new FlowLayout(false, 0.05f, true)));
	}

	public UIObjectGroup getContent() {
		return (UIObjectGroup) this.get(0);
	}

	@Override
	public <V extends UIObject> V add(final V e) {
		this.checkItemCount(1);
		return super.add(e);
	}

	@Override
	public <V extends UIObject> boolean addAll(final Collection<? extends V> c) {
		this.checkItemCount(c.size());
		return super.addAll(c);
	}

	@Override
	public <V extends UIObject> V[] addAll(final V... e) {
		this.checkItemCount(e.length);
		return super.addAll(e);
	}

	@Override
	public <V extends UIObject> boolean addChildren(final ObjectGroup<? extends V> c) {
		this.checkItemCount(c.size());
		return super.addChildren(c);
	}

	@Override
	public void doLayout() {
		this.applyScrollX();
		super.doLayout();
	}

	public void recomputeRange() {
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty()) {
			return;
		}
		final Rectangle2D contentBounds = this.getContent().getLocalTransformedBounds().getBounds2D();
		final Rectangle2D parentBounds = obo.get().getBounds().getBounds2D();
		if (parentBounds.getWidth() > contentBounds.getWidth()) {
			this.scrollXRange.set(parentBounds.getCenterX() - contentBounds.getCenterX(),
					parentBounds.getCenterX() - contentBounds.getCenterX());
		} else {
			this.scrollXRange.set(parentBounds.getMinX() - contentBounds.getMinX(), parentBounds.getMaxX() - contentBounds.getMaxX());
		}
	}

	@Override
	public boolean recomputeBounds() {
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty()) {
			return true;
		}
		final Rectangle2D containerBounds = this.getContent().getLocalTransformedBounds().getBounds2D();
		final Rectangle2D superBounds = obo.get().getBounds().getBounds2D();
		final Rectangle.Float newFixedBounds = new Rectangle.Float((float) superBounds.getMinX(),
				(float) containerBounds.getMinY(),
				(float) superBounds.getWidth(),
				(float) containerBounds.getHeight());
		final boolean changed = !newFixedBounds.equals(this.fixedBounds);
		this.fixedBounds.setFrame(newFixedBounds);
		return changed;
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

	public float getScrollX() {
		return this.scrollX;
	}

	public String getTitleKey() {
		return this.titleKey;
	}

	@Deprecated
	public String getTitle() {
		return LocalizationService.get(this.titleKey);
	}

	public ColorMaterial getAccentColor() {
		return this.accentColor;
	}

	public void setAccentColor(final ColorMaterial accentColor) {
		this.accentColor = accentColor;
	}

	@Override
	public int getMaxItems() {
		return 1;
	}

	@Override
	public Shape getBounds() {
		return this.fixedBounds;
	}

	@Override
	public String toString() {
		return "BuildingTabUIObjectGroup [index=" + this.index + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", bounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
