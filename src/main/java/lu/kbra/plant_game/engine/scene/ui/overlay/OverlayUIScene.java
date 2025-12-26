package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.util.stream.Collectors;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.ProgressBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.prim.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.EnergyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.MoneyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.WaterIconUIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutParent;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.objs.entity.ParentAware;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OverlayUIScene extends UIScene implements LayoutParent, PaddingOwner {

	protected float margin = 0.02f;

	protected final LayoutOffsetUIObjectGroup statsGroup = new LayoutOffsetUIObjectGroup("stats", new FlowLayout(true, 0.08f));
	protected OverlayIntegerStatLine waterGroup, moneyGroup, energyGroup;
	protected ProgressBarUIObject progressBar;

	protected Layout layout;

	public OverlayUIScene(final CacheManager parent) {
		super("game-overlay", parent);
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		this.setLayout(new AnchorLayout());
		super.addEntity(this.statsGroup);

		this.statsGroup.addComponent(new AnchorComponent(Anchor.TOP_LEFT, Anchor.TOP_LEFT));

		final float iconScale = 0.1f, textScale = iconScale * 2;

		this.waterGroup = new OverlayIntegerStatLine("water");
		this.waterGroup
				.init(workers,
						renderDispatcher,
						textScale,
						WaterIconUIObject.class,
						IntegerTextUIObject.class,
						SignedIntegerTextUIObject.class)
				.then(obj -> {
					obj.getValue().setValue(1000).flushValue();

					obj.getPopup().setValue(999).flushValue();
				});
		this.statsGroup.add(this.waterGroup);

		this.moneyGroup = new OverlayIntegerStatLine("money");
		this.moneyGroup
				.init(workers,
						renderDispatcher,
						textScale,
						MoneyIconUIObject.class,
						IntegerTextUIObject.class,
						SignedIntegerTextUIObject.class)
				.then(obj -> {
					obj.getValue().setValue(65).flushValue();

					obj.getPopup().setValue(10).flushValue();
				});
		this.statsGroup.add(this.moneyGroup);

		this.energyGroup = new OverlayIntegerStatLine("energy");
		this.energyGroup
				.init(workers,
						renderDispatcher,
						textScale,
						EnergyIconUIObject.class,
						IntegerTextUIObject.class,
						SignedIntegerTextUIObject.class)
				.then(obj -> {
					obj.getValue().setValue(99999).flushValue();

					obj.getPopup().setValue(100).flushValue();
				});
		this.statsGroup.add(this.energyGroup);

		this.progressBar = new ProgressBarUIObject("...", this, new Transform3D(), 0.02f, 0.5f);
		this.progressBar.init(workers, renderDispatcher, FlatQuadUIObject.class, FlatQuadUIObject.class);
		this.progressBar.addComponent(new AnchorComponent(Anchor.TOP_CENTER, Anchor.TOP_CENTER));
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		super.input(inputHandler, dTime, frameState);

		if (inputHandler.wasResized()) {
			this.doLayout();
		}

//		if (inputHandler.wasResized()) {
//		final Rectangle2D bounds = this.statsGroup.getBounds().getBounds2D();
//		this.statsGroup.getTransform().getTranslation().x = -(float) super.getBounds().getWidth() - (float) bounds.getMinX() + this.margin;
//		this.statsGroup.getTransform().getTranslation().z = -1f + this.margin;
		this.statsGroup.getTransform().scaleSet(0.35f);
		this.statsGroup.getTransform().updateMatrix();
//		}

//		final Rectangle2D objBounds = this.statsGroup.getBounds().getBounds2D();
//		final Rectangle2D.Float screenBounds = super.getBounds();
//		this.statsGroup.getTransform().scaleSet(0.35f);
//		alignAnchors(this.statsGroup.getTransform(), objBounds, screenBounds, Anchor.TOP_LEFT, Anchor.TOP_LEFT, 0, 0);

		this.progressBar.getTransform().scaleSet(2, 1, 0.1f).update();
		this.progressBar.setForegroundColor(GameEngineUtils.hsvToColorToVec4f((float) Math.sin(PGLogic.TOTAL_TIME()), 1, 1, 1));
		this.progressBar.setValue((float) Math.sin(PGLogic.TOTAL_TIME()) / 2 + 0.5f).updateScaling();

//		this.setPadding(((float) Math.sin(PGLogic.TOTAL_TIME()) / 2 + 0.5f) * 0.2f);
		this.doLayout();
	}

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final float dTime,
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		super.update(inputHandler, dTime, compositor, workers, render);
	}

	@Override
	public void setLayout(final Layout layout) {
		this.layout = layout;
		if (layout instanceof final ParentAware pa) {
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
			this
					.getEntities()
					.values()
					.stream()
					.filter(e -> e instanceof final LayoutParent lp)
					.forEach(e -> ((LayoutParent) e).doLayout());
			if (this.layout == null) {
				return;
			}
			this.layout
					.doLayout(this
							.getEntities()
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

}
