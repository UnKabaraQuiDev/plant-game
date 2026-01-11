package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.AnchoredProgressBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.prim.BuildingItemFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageSignedIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.EnergyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.MoneyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.WaterIconUIObject;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.geo.GeoAxis;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DShear;

public class OverlayUIScene extends UIScene implements LayoutOwner, PaddingOwner {

	private static final float STATS_GROUP_SCALE = 0.35f;

	protected float margin = 0.02f;

	protected final AnchoredLayoutUIObjectGroup statsGroup = new AnchoredLayoutUIObjectGroup("stats",
			new FlowLayout(true, 0.08f * STATS_GROUP_SCALE),
			Anchor.TOP_LEFT,
			Anchor.TOP_LEFT);
	protected IntegerStatLine waterGroup;
	protected IntegerStatLine moneyGroup;
	protected IntegerStatLine energyGroup;
	protected AnchoredProgressBarUIObject progressBar;
	protected ExtAnchoredIntegerStatLine progressGroup;

	protected BuildingInfoUIObjectGroup buildingInfo = new BuildingInfoUIObjectGroup();
	protected BuildingPanelUIObjectGroup buildingPanel = new BuildingPanelUIObjectGroup();

	protected Layout layout;

	public OverlayUIScene(final CacheManager parent) {
		super("game-overlay", parent);
		this.setLayout(new AnchorLayout());
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		super.addAll(this.statsGroup, this.buildingPanel);

		final float height = 0.2f * STATS_GROUP_SCALE;

		this.buildingInfo.init(workers, renderDispatcher).then(this::add);

		final BuildingTabUIObjectGroup buildings = new BuildingTabUIObjectGroup("Tab OwO", 0, ColorMaterial.CYAN);
		this.buildingPanel.addTab(buildings);

		final BuildingTabUIObjectGroup buildings2 = new BuildingTabUIObjectGroup("Tab :3c", 1, ColorMaterial.GREEN);
		this.buildingPanel.addTab(buildings2);

		final BuildingTabUIObjectGroup buildings3 = new BuildingTabUIObjectGroup("Tab 3", 2, ColorMaterial.PINK);
		this.buildingPanel.addTab(buildings3);

		IntStream.range(0, 10)
				.forEach(index -> UIObjectFactory.create(BuildingItemFlatQuadUIObject.class)
						.set(i -> i.setIndex(index))
						.set(j -> j.setTransform(new Transform3D(0.3f)))
						.set(j -> j.setColorMaterial(ColorMaterial.byId(ColorMaterial.CYAN.getId() - index - 1)))
						.add(buildings2.getContainer())
						.push());

		IntStream.range(0, 10)
				.forEach(index -> UIObjectFactory.create(BuildingItemFlatQuadUIObject.class)
						.set(i -> i.setIndex(index))
						.set(j -> j.setTransform(new Transform3D(0.3f)))
						.set(j -> j.setColorMaterial(ColorMaterial.byId(ColorMaterial.WHITE.getId() - index - 1)))
						.add(buildings.getContainer())
						.push());

		this.waterGroup = new IntegerStatLine("water-counter");
		this.waterGroup
				.init(workers,
						renderDispatcher,
						height,
						WaterIconUIObject.class,
						IntegerTextUIObject.class,
						SignedIntegerTextUIObject.class)
				.then(obj -> {
					obj.getValue().setValue(1000).flushValue();

					obj.getPopup().setValue(999).flushValue();
				});
		this.statsGroup.add(this.waterGroup);

		this.moneyGroup = new IntegerStatLine("money-counter");
		this.moneyGroup
				.init(workers,
						renderDispatcher,
						height,
						MoneyIconUIObject.class,
						IntegerTextUIObject.class,
						SignedIntegerTextUIObject.class)
				.then(obj -> {
					obj.getValue().setValue(65).flushValue();

					obj.getPopup().setValue(10).flushValue();
				});
		this.statsGroup.add(this.moneyGroup);

		this.energyGroup = new IntegerStatLine("energy-counter");
		this.energyGroup
				.init(workers,
						renderDispatcher,
						height,
						EnergyIconUIObject.class,
						IntegerTextUIObject.class,
						SignedIntegerTextUIObject.class)
				.then(obj -> {
					obj.getValue().setValue(99999).flushValue();

					obj.getPopup().setValue(100).flushValue();
				});
		this.statsGroup.add(this.energyGroup);

		this.progressBar = new AnchoredProgressBarUIObject("level-progress-bar",
				this,
				new Transform3DShear().shearSet(GeoAxis.Z, GeoAxis.X, -0.8f).scaleSet(1.8f, 1, 0.05f),
				Anchor.TOP_RIGHT,
				Anchor.TOP_RIGHT,
				0.01f,
				0.5f);
		this.progressBar.init(workers, renderDispatcher, FlatQuadUIObject.class, FlatQuadUIObject.class).then(pb -> {
			this.progressGroup = new ExtAnchoredIntegerStatLine("level-progress-counter");
			this.progressGroup
					.init(workers,
							renderDispatcher,
							height,
							4,
							3,
							null,
							PercentageIntTextUIObject.class,
							PercentageSignedIntTextUIObject.class)
					.then(obj -> {
						obj.getValue().setValue(0).flushValue();
						obj.getPopup().setValue(0).flushValue();
						obj.getPopup().setPadding(false);
						obj.setTarget(pb.getForeground(), Anchor.TOP_CENTER, Anchor.BOTTOM_RIGHT);
					});
			super.add(this.progressGroup);
		});
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		super.input(inputHandler, frameState);

//		if (inputHandler.wasResized()) {
//		final Rectangle2D bounds = this.statsGroup.getBounds().getBounds2D();
//		this.statsGroup.getTransform().getTranslation().x = -(float) super.getBounds().getWidth() - (float) bounds.getMinX() + this.margin;
//		this.statsGroup.getTransform().getTranslation().z = -1f + this.margin;
//		this.statsGroup.getTransform().scaleSet(0.35f).updateMatrix();
//		}

//		final Rectangle2D objBounds = this.statsGroup.getBounds().getBounds2D();
//		final Rectangle2D.Float screenBounds = super.getBounds();
//		this.statsGroup.getTransform().scaleSet(0.35f);
//		alignAnchors(this.statsGroup.getTransform(), objBounds, screenBounds, Anchor.TOP_LEFT, Anchor.TOP_LEFT, 0, 0);

//		this.progressBar.getTransform().scaleSet(1.8f, 1, 0.5f).updateMatrix();
//		((Transform3DShear) this.progressBar.getTransform()).shearSet(GeoAxis.Z, GeoAxis.X, -0.8f).update();

		if (this.progressGroup != null && this.progressBar != null) {
			this.progressBar.setForegroundColor(GameEngineUtils.hsvToColorToVec4f((float) Math.sin(PGLogic.TOTAL_TIME()), 1, 1, 1));
			this.progressBar.setValue((float) Math.sin(PGLogic.TOTAL_TIME()) / 2 + 0.5f).updateScaling();

			this.progressGroup.set((int) (this.progressBar.getValue() * 101)).flushValue();
		}

//		this.buildingPanel.getBuildingTabs().values().forEach(c -> c.setScrollX((float) Math.sin(PGLogic.TOTAL_TIME() * 1.5f) * 2 - 0.5f));
//		this.buildingPanel.doLayout();

//		this.setPadding(((float) Math.sin(PGLogic.TOTAL_TIME()) / 2 + 0.5f) * 0.2f);
		// this.doLayout();
	}

	public BuildingInfoUIObjectGroup getBuildingInfo() {
		return this.buildingInfo;
	}

	public BuildingPanelUIObjectGroup getBuildingPanel() {
		return this.buildingPanel;
	}

	@Override
	public void setLayout(final Layout layout) {
		this.layout = layout;
		if (layout instanceof final ParentAwareNode pa) {
			ParentAwareComponent.checkHierarchy(this, pa);
			pa.setParent(this);
		}
	}

	@Override
	public Layout getLayout() {
		return this.layout;
	}

	@Override
	public void doLayout() {
		synchronized (this.getEntitiesLock()) {
			this.getEntities().values().stream().filter(e -> e instanceof final LayoutOwner lp).forEach(e -> ((LayoutOwner) e).doLayout());
			if (this.layout == null) {
				return;
			}
			this.layout.doLayout(this.getEntities()
					.values()
					.stream()
					.filter(UIObject.class::isInstance)
					.map(UIObject.class::cast)
					.collect(Collectors.toList()));
		}
	}

	@Override
	public float getPadding() {
		return this.margin;
	}

	@Override
	public void setPadding(final float p) {
		this.margin = p;
	}

	@Override
	public String toString() {
		return "OverlayUIScene [margin=" + this.margin + ", statsGroup=" + this.statsGroup + ", waterGroup=" + this.waterGroup
				+ ", moneyGroup=" + this.moneyGroup + ", energyGroup=" + this.energyGroup + ", progressBar=" + this.progressBar
				+ ", progressGroup=" + this.progressGroup + ", layout=" + this.layout + ", uiCache=" + this.uiCache + ", hovering="
				+ this.hovering + ", focused=" + this.focused + ", entities=" + this.entities + ", name=" + this.name + ", camera="
				+ this.camera + "]";
	}

}
