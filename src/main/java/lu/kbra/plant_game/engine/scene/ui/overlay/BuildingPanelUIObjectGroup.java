package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.ui.group.BuildingTabListUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsBoundsInput;
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
	protected String activeBuildingTabId;
	protected float buildingTabScrollSpeed = 0.75f;

	public BuildingPanelUIObjectGroup() {
		super("building-tab", new AnchorLayout(), Anchor.BOTTOM_CENTER, Anchor.BOTTOM_CENTER);
		this.fixedBounds = new Rectangle2D.Float(0, 0, 1, 1);

		this.add(this.tabList);
	}

	@Override
	public boolean boundsInput(final WindowInputHandler inputHandler) {
		if (inputHandler.getMouseScroll().y() == 0) {
			return false;
		}

		final BuildingTabUIObjectGroup buildingTab = this.buildingTabs.get(this.activeBuildingTabId);
		if (buildingTab == null) {
			return false;
		}

		buildingTab.addScrollX((float) inputHandler.getMouseScroll().y() * this.buildingTabScrollSpeed);
		buildingTab.applyScrollX();

		return true;
	}

	public <T extends BuildingTabUIObjectGroup> ObjectTriggerLatch<T> addTab(final T tab) {
		final ObjectTriggerLatch<T> latch = new ObjectTriggerLatch<>(1, tab);

		new BuildingTabButtonUIObjectGroup(new Transform3D(), tab).init().then(obj -> {
			obj.getTransform().scaleMul(this.tabList.getFixedHeight() / (float) obj.getBounds().getBounds2D().getHeight()).update();
			this.tabList.add(obj);
			this.buildingTabs.put(tab.getId(), tab);
			this.add(tab);
			if (this.activeBuildingTabId == null) {
				this.setClickedTab(tab.getId(), true);
				this.activeBuildingTabId = tab.getId();
			} else {
				this.setClickedTab(tab.getId(), false);
			}
			this.doLayout();
			latch.trigger(obj);
		});

		return latch;
	}

	public void switchTab(final String tabId) {
		this.setClickedTab(tabId, true);
		this.setClickedTab(this.activeBuildingTabId, false);

		this.activeBuildingTabId = tabId;
	}

	private void setClickedTab(final String tabId, final boolean active) {
		final BuildingTabUIObjectGroup tab = this.buildingTabs.get(tabId);
		if (tab != null) {
			tab.setActive(active);
			this.tabList.getButton(tabId)
					.ifPresentOrElse(btn -> btn.setClicked(active), () -> GlobalLogger.warning("No button for tab named: " + tabId));
		} else {
			GlobalLogger.warning("No tab named: " + tabId);
		}
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
		return this.activeBuildingTabId;
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
