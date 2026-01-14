package lu.kbra.plant_game.vanilla.scene.overlay.group.building;

import java.util.Optional;
import java.util.OptionalInt;

import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.ui.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.LimitedObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.utils.geo.GeoAxis;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DShear;

public class BuildingTabButtonUIObjectGroup extends OffsetUIObjectGroup implements NeedsClick, LimitedObjectGroup<UIObject> {

	protected String tabId;
	protected String tabKey;
	protected ColorMaterial activeTabColor;
	protected boolean isClicked = false;

	protected ProgrammaticTextUIObject textComponent;
	protected FlatQuadUIObject backgroundComponent;

	public BuildingTabButtonUIObjectGroup(final String str, final Transform3D transform, final String tabId, final String tabKey,
			final ColorMaterial activeTabColor) {
		super(str, transform);
		this.tabId = tabId;
		this.tabKey = tabKey;
		this.activeTabColor = activeTabColor;
	}

	public BuildingTabButtonUIObjectGroup(final Transform3D transform, final BuildingTabUIObjectGroup tab) {
		this("building-tab-btn-" + tab.getId(), transform, tab.getId(), tab.getTitleKey(), tab.getAccentColor());
	}

	@Override
	public void click(final WindowInputHandler input) {
		this.getLastParentMatching(BuildingPanelUIObjectGroup.class)
				.ifPresentOrElse(panel -> panel.switchTab(this.tabId),
						() -> GlobalLogger
								.warning("Invalid hierarchy: No " + BuildingPanelUIObjectGroup.class.getSimpleName() + " in parents."));
	}

	public ObjectTriggerLatch<? extends BuildingTabButtonUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends BuildingTabButtonUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

//		final float height = 0.1f;
		final float quadY = -0.1f;
		final float textMargins = 0.005f;
//		final float charWidth = height;

		UIObjectFactory
				.createText(ProgrammaticTextUIObject.class,
						OptionalInt.empty(),
						Optional.empty(),
						Optional.of(TextAlignment.TEXT_CENTER),
						Optional.of("btn-" + this.tabId),
						Optional.of(this.tabKey))
				.set(i -> i.setTransform(new Transform3D(1 - 2 * textMargins)))
				.set(i -> i.getTextEmitter().setForegroundColor(ColorMaterial.WHITE.getColor()))
				.add(this)
				.postInit(i -> this.textComponent = i)
				.latch(latch)
				.postInit(txt -> UIObjectFactory.create(FlatQuadUIObject.class)
						.set(i -> i.setTransform(new Transform3DShear().shearAdd(GeoAxis.Z, GeoAxis.X, -0.35f)
								.scaleSet((float) txt.getBounds().getWidth(), 1, (float) txt.getBounds().getHeight())
								.translationSetY(quadY)
								.update()))
						.set(i -> i.setColorMaterial(ColorMaterial.BLACK))
						.add(this)
						.postInit(i -> this.backgroundComponent = i)
						.latch(latch)
						.push())
				.push();

		return latch;
	}

	public String getTabId() {
		return this.tabId;
	}

	public void setTabId(final String tabId) {
		this.tabId = tabId;
	}

	public ColorMaterial getActiveTabColor() {
		return this.activeTabColor;
	}

	public void setActiveTabColor(final ColorMaterial activeTabColor) {
		this.activeTabColor = activeTabColor;
	}

	public ProgrammaticTextUIObject getTextComponent() {
		return this.textComponent;
	}

	public FlatQuadUIObject getBackgroundComponent() {
		return this.backgroundComponent;
	}

	public void setClicked(final boolean isClicked) {
		this.isClicked = isClicked;
		if (isClicked) {
			this.backgroundComponent.setColorMaterial(this.activeTabColor);
			this.textComponent.getTextEmitter().setForegroundColor(ColorMaterial.BLACK.getColor());
		} else {
			this.backgroundComponent.setColorMaterial(ColorMaterial.BLACK);
			this.textComponent.getTextEmitter().setForegroundColor(this.activeTabColor.getColor());
		}
	}

	public boolean isClicked() {
		return this.isClicked;
	}

	@Override
	public int getMaxItems() {
		return 2;
	}

	@Override
	public String toString() {
		return "BuildingTabButtonUIObjectGroup [tabId=" + this.tabId + ", tabKey=" + this.tabKey + ", activeTabColor=" + this.activeTabColor
				+ ", isClicked=" + this.isClicked + ", textComponent=" + this.textComponent + ", backgroundComponent="
				+ this.backgroundComponent + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities
				+ ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
