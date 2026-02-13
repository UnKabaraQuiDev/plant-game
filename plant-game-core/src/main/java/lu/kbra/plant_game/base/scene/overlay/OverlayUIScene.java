package lu.kbra.plant_game.base.scene.overlay;

import java.util.stream.Collectors;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.base.scene.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.building.BuildingPanelToggleButtonUIObject;
import lu.kbra.plant_game.base.scene.overlay.group.building.BuildingPanelUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.building.BuildingTabUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.impl.AnchoredLayoutUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.stat_line.integer.ExtAnchoredIntegerStatLine;
import lu.kbra.plant_game.base.scene.overlay.stat_line.integer.IntegerStatLine;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.AnchoredProgressBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.icon.EnergyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.MoneyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.WaterIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.PaddingOwner;
import lu.kbra.plant_game.engine.entity.ui.prim.BuildingItemUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageSignedIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.plugin.registry.BuildingRegistry;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
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
		super.addAll(this.statsGroup);

		final float height = 0.2f * STATS_GROUP_SCALE;

		this.buildingPanel.init().then(c -> {
			this.add(c);
			UIObjectFactory.create(BuildingPanelToggleButtonUIObject.class)
					.set(i -> i.setTransform(new Transform3D(0.2f).rotationSet(0, (float) Math.PI, 0).update()))
					.set(i -> i.setTarget(c, Anchor.BOTTOM_CENTER, Anchor.TOP_CENTER))
					.add(this)
					.push();
		});

		this.buildingInfo.init().then(c -> {
			c.getTransform().translationAddY(0.5f);
			c.setActive(false);
			this.add(c);
		});

		BuildingRegistry.BUILDING_DEFS.keySet()
				.forEach(p -> this.buildingPanel
						.addTab(new BuildingTabUIObjectGroup(p.getLocalizationKey(), p.getIndex(), p.getAccentColor()))
						.then(tab -> BuildingRegistry.BUILDING_DEFS.get(p)
								.forEach(f -> new BuildingItemUIObjectGroup(f).init().then(obj -> {
									obj.setTransform(new Transform3D(0.3f));
									obj.setIndex(f.getIndex());
									tab.getContent().add(obj);
								}))));

		this.waterGroup = new IntegerStatLine("water-counter");
		this.waterGroup.init(workers,
				renderDispatcher,
				height,
				WaterIconUIObject.class,
				IntegerTextUIObject.class,
				SignedIntegerTextUIObject.class);
		this.statsGroup.add(this.waterGroup);

		this.moneyGroup = new IntegerStatLine("money-counter");
		this.moneyGroup.init(workers,
				renderDispatcher,
				height,
				MoneyIconUIObject.class,
				IntegerTextUIObject.class,
				SignedIntegerTextUIObject.class);
		this.statsGroup.add(this.moneyGroup);

		this.energyGroup = new IntegerStatLine("energy-counter");
		this.energyGroup.init(workers,
				renderDispatcher,
				height,
				EnergyIconUIObject.class,
				IntegerTextUIObject.class,
				SignedIntegerTextUIObject.class);
		this.statsGroup.add(this.energyGroup);

		this.progressBar = new AnchoredProgressBarUIObject("level-progress-bar",
				this,
				new Transform3DShear().shearSet(GeoAxis.Z, GeoAxis.X, -0.8f).scaleSet(1.8f, 1, 0.05f),
				Anchor.TOP_RIGHT,
				Anchor.TOP_RIGHT,
				0.01f,
				0.5f);
		this.progressBar.init(FlatQuadUIObject.class, FlatQuadUIObject.class).then(pb -> {
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
						obj.doLayout();
					});
			this.progressGroup.doLayout();
			super.add(this.progressGroup);
		});
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		super.input(inputHandler, frameState);

		if (this.progressGroup != null && this.progressBar != null) {
			this.progressBar.setForegroundColor(GameEngineUtils.hsvToColorToVec4f((float) Math.sin(PGLogic.TOTAL_TIME()), 1, 1, 1));
			this.progressBar.setValue((float) Math.sin(PGLogic.TOTAL_TIME()) / 2 + 0.5f).updateScaling();

			this.progressGroup.set((int) (this.progressBar.getValue() * 101)).flushValue();
		}
	}

	int frameCounter = 0;

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		super.update(inputHandler, compositor, workers, render);
//		PGLogic.INSTANCE.getGameData()
//				.getResources()
//				.compute(DefaultResourceType.WATER, (k, v) -> v + (this.frameCounter++ % 200 == 0 ? (int) Math.rint(Math.random()) : 0));

		this.waterGroup.setTarget(PGLogic.INSTANCE.getGameData().getResources().get(DefaultResourceType.WATER));
		this.energyGroup.setTarget(PGLogic.INSTANCE.getGameData().getResources().get(DefaultResourceType.ENERGY));
		this.moneyGroup.setTarget(PGLogic.INSTANCE.getGameData().getResources().get(DefaultResourceType.MONEY));
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
