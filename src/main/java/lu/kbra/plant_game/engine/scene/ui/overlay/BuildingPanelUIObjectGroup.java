package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Vector2f;

import lu.pcy113.pclib.concurrency.FutureTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.BuildingTabListUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsBoundsInput;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;;

public class BuildingPanelUIObjectGroup extends AnchoredLayoutUIObjectGroup implements BoundsOwnerParentAware, NeedsBoundsInput {

	protected Rectangle2D.Float fixedBounds;
	protected float boundsMarginX = 0.05f;
	protected float boundsMarginY = 0f;
	protected float heightRatio = 1f / 10 * 2;

	protected BuildingTabListUIObjectGroup tabList = new BuildingTabListUIObjectGroup();
	protected Map<String, BuildingTabUIObjectGroup> buildingTabs = new HashMap<>();
	protected String activeBuildingTabIndex;
	protected float buildingTabScrollSpeed = 0.75f;

	public BuildingPanelUIObjectGroup() {
		super("building-tab", new AnchorLayout(), Anchor.BOTTOM_CENTER, Anchor.BOTTOM_CENTER);
		this.fixedBounds = new Rectangle2D.Float(0, 0, 1, 1);

		this.add(this.tabList);
	}

	@Override
	public boolean input(final WindowInputHandler inputHandler) {
		if (inputHandler.getMouseScroll().y() == 0) {
			return false;
		}
		final BuildingTabUIObjectGroup buildingTab = this.buildingTabs.get(this.activeBuildingTabIndex);
		if (buildingTab != null) {
			buildingTab.addScrollX((float) inputHandler.getMouseScroll().y() * this.buildingTabScrollSpeed);
			buildingTab.applyScrollX();
//			this.doLayout();
			return true;
		}
		return false;
	}

	public <T extends BuildingTabUIObjectGroup> FutureTriggerLatch<T> addTab(final T tab) {
		final FutureTriggerLatch<T> latch = new FutureTriggerLatch<>(1, tab);

		UIObjectFactory.createText(ProgrammaticTextUIObject.class,
//						OptionalInt.of(4),
				OptionalInt.empty(),
				Optional.of(new Vector2f(0.1f)),
				Optional.empty(),
				Optional.of("btn-" + tab.getId()),
				Optional.of(tab.getTitleKey())).set(i -> i.setTransform(new Transform3D()))
//				.set(i -> i.setText("Text").flushText())
				.add(this.tabList)
				.postInit(i -> this.buildingTabs.put(tab.getId(), tab))
				.postInit(i -> this.add(tab))
				.postInit(i -> this.doLayout())
				.latch(latch)
				.push();

		if (this.activeBuildingTabIndex == null) {
			this.activeBuildingTabIndex = tab.getId();
		}

		return latch;
	}

	@Override
	public boolean recomputeBounds() {
		if (!this.hasBoundsOwnerParent()) {
			return false;
		}
		final Rectangle2D superBounds = this.getBoundsOwnerParent().getBounds().getBounds2D();
		final Rectangle2D.Float newBounds = new Rectangle2D.Float((float) superBounds.getMinX() + this.boundsMarginX,
				(float) superBounds.getMaxY() - this.boundsMarginY,
				(float) superBounds.getWidth() - 2 * this.boundsMarginX,
				(float) superBounds.getHeight() * this.heightRatio);
		final boolean changed = newBounds.equals(this.fixedBounds);
		this.fixedBounds.setFrame(newBounds);
		return changed;
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

	public BuildingTabListUIObjectGroup getTabList() {
		return this.tabList;
	}

	public String getActiveBuildingTabIndex() {
		return this.activeBuildingTabIndex;
	}

	public Map<String, BuildingTabUIObjectGroup> getBuildingTabs() {
		return this.buildingTabs;
	}

	@Override
	public String toString() {
		return "BuildingTabUIObjectGroup [objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", bounds=" + this.computedBounds + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
