package lu.kbra.plant_game.engine.entity.ui.building;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.impl.NeedsBoundsInput;
import lu.kbra.plant_game.engine.entity.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.AnchoredLayoutUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.BuildingTabListUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.prim.IBAnchoredFlatQuadUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.scene.world.data.GameData;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BuildingPanelUIObjectGroup extends AnchoredLayoutUIObjectGroup
		implements BoundsOwnerParentAware, NeedsBoundsInput, NeedsUpdate, MarginOwner {

	protected Rectangle2D.Float fixedBounds = new Rectangle2D.Float(0, 0, 1, 1);
	protected float boundsMarginX = 0.05f;
	protected float boundsMarginY = 0f;
	protected float heightRatio = 1f / 10 * 2;

	protected final Object tabLock = new Object();
	protected BuildingTabListUIObjectGroup tabList = new BuildingTabListUIObjectGroup();
	protected Map<String, BuildingTabUIObjectGroup> buildingTabs = new HashMap<>();
	protected String activeBuildingTabKey;
	protected float buildingTabScrollSpeed = 0.75f;

	protected ObjectPointer<UIObject> backdrop = new ObjectPointer<>();

	public BuildingPanelUIObjectGroup() {
		super("building-tab", new AnchorLayout(), Anchor.BOTTOM_CENTER, Anchor.BOTTOM_CENTER);
	}

	public ObjectTriggerLatch<? extends BuildingPanelUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends BuildingPanelUIObjectGroup> latch = new ObjectTriggerLatch<>(1, this);

		this.add(this.tabList);

		UIObjectFactory.create(IBAnchoredFlatQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, -0.2f, 0),
						new Quaternionf(),
						new Vector3f(this.getBounds().width, 1, this.getBounds().height))))
				.set(i -> i.setColor(new Vector4f(ColorMaterial.DARK_GRAY.getColor()).mul(1, 1, 1, 0.5f)))
				.set(i -> i.setAnchors(Anchor.CENTER_CENTER, Anchor.CENTER_CENTER))
				.add(this)
				.get(this.backdrop)
				.latch(latch)
				.push();

		latch.then((Consumer<BuildingPanelUIObjectGroup>) c -> {
			c.doLayout();
			c.setActive(false);
		});

		return latch;
	}

	@Override
	public boolean boundsInput(final WindowInputHandler inputHandler) {
		if (inputHandler.getMouseScroll().y() == 0) {
			return false;
		}

		final BuildingTabUIObjectGroup buildingTab = this.buildingTabs.get(this.activeBuildingTabKey);
		if (buildingTab == null) {
			return false;
		}

		buildingTab.addScrollX((float) inputHandler.getMouseScroll().y() * this.buildingTabScrollSpeed);

		return true;
	}

	@Override
	public void update(final WindowInputHandler input) {
		final WorldLevelScene world = PGLogic.INSTANCE.getWorldScene();
		if (world == null) {
			return;
		}
		final GameData gameData = world.getGameData();

		final BuildingTabUIObjectGroup buildingTab = this.buildingTabs.get(this.activeBuildingTabKey);
		if (buildingTab == null) {
			return;
		}

		buildingTab.applyScrollX();

		buildingTab.getContent()
				.parallelStream()
				.filter(BuildingItemUIObjectGroup.class::isInstance)
				.map(BuildingItemUIObjectGroup.class::cast)
				.forEach(c -> c.updateTintStatus(gameData, world));
	}

	public <T extends BuildingTabUIObjectGroup> ObjectTriggerLatch<T> addTab(final T tab) {
		final ObjectTriggerLatch<T> latch = new ObjectTriggerLatch<>(1, tab);

		new BuildingTabButtonUIObjectGroup(new Transform3D(new Vector3f(0, -0.5f, 0)), tab).init()
				.then((Consumer<BuildingTabButtonUIObjectGroup>) obj -> {
					obj.getTransform().scaleMul(this.tabList.getFixedHeight() / (float) obj.getBounds().getBounds2D().getHeight()).update();

					this.tabList.add(obj);
					this.buildingTabs.put(tab.getTitleKey(), tab);
					this.add(tab);

					synchronized (this.tabLock) {
						if (this.activeBuildingTabKey == null) {
							this.setClickedTab(tab.getTitleKey(), true);
							this.activeBuildingTabKey = tab.getTitleKey();
						} else {
							this.setClickedTab(tab.getTitleKey(), false);
						}
					}

					this.getFirstParentMatching(LayoutOwner.class, true).ifPresent(LayoutOwner::doLayout);
					latch.trigger(obj);
				});

		return latch;
	}

	public void switchTab(final String tabKey) {
		synchronized (this.tabLock) {
			if (tabKey.equals(this.activeBuildingTabKey)) {
				return;
			}

			this.setClickedTab(tabKey, true);
			this.setClickedTab(this.activeBuildingTabKey, false);

			this.getFirstParentMatching(LayoutOwner.class, true).ifPresent(LayoutOwner::doLayout);

			this.activeBuildingTabKey = tabKey;
		}
	}

	private void setClickedTab(final String tabKey, final boolean active) {
		synchronized (this.tabLock) {
			final BuildingTabUIObjectGroup tab = this.buildingTabs.get(tabKey);
			if (tab != null) {
				tab.setActive(active);
				this.tabList.getButton(tabKey)
						.ifPresentOrElse(btn -> btn.setClicked(active), () -> GlobalLogger.warning("No button for tab named: " + tabKey));
//			this.activeBuildingTabKey = active ? tabKey : null;
			} else {
				GlobalLogger.warning("No tab named: " + tabKey);
			}
		}
	}

	@Override
	public boolean recomputeBounds() {
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty()) {
			return false;
		}
		final Rectangle2D superBounds = obo.get().getBounds().getBounds2D();
		final Rectangle2D.Float newBounds = new Rectangle2D.Float((float) superBounds.getMinX() + this.boundsMarginX,
				(float) superBounds.getMaxY() - this.boundsMarginY,
				(float) superBounds.getWidth() - 2 * this.boundsMarginX,
				(float) superBounds.getHeight() * this.heightRatio);
		final boolean changed = newBounds.equals(this.fixedBounds);
		this.fixedBounds.setFrame(newBounds);
		this.backdrop.ifSet(p -> p.getTransform().scaleSet(this.fixedBounds.width, 1, this.fixedBounds.height).update());
		return changed;
	}

	@Override
	public void doLayout() {
		this.recomputeBounds();
		super.doLayout();
	}

	@Override
	public Rectangle2D.Float getBounds() {
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
		return this.activeBuildingTabKey;
	}

	public Map<String, BuildingTabUIObjectGroup> getBuildingTabs() {
		return this.buildingTabs;
	}

	@Override
	public String toString() {
		return "BuildingPanelUIObjectGroup@" + System.identityHashCode(this) + " [fixedBounds=" + this.fixedBounds + ", boundsMarginX="
				+ this.boundsMarginX + ", boundsMarginY=" + this.boundsMarginY + ", heightRatio=" + this.heightRatio + ", tabList="
				+ this.tabList + ", buildingTabs=" + this.buildingTabs + ", activeBuildingTabId=" + this.activeBuildingTabKey
				+ ", buildingTabScrollSpeed=" + this.buildingTabScrollSpeed + ", objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities="
				+ this.subEntities + ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

	@Override
	public float getMargin() {
		return 0.02f;
	}

	@Override
	public void setMargin(final float m) {
		throw new UnsupportedOperationException();
	}

}
