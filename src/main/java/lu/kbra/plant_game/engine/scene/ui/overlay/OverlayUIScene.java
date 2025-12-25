package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.UpdateFrameState;
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
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OverlayUIScene extends UIScene {

	protected final float margin = 0.02f;

	protected final LayoutOffsetUIObjectGroup statsGroup = new LayoutOffsetUIObjectGroup("stats", new FlowLayout(true, 0.08f));
	protected OverlayIntegerStatLine waterGroup, moneyGroup, energyGroup;
	protected ProgressBarUIObject progressBar;

	public OverlayUIScene(final CacheManager parent) {
		super("game-overlay", parent);
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		super.addEntity(this.statsGroup);

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
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		super.input(inputHandler, dTime, frameState);

//		if (inputHandler.wasResized()) {
		final Rectangle2D bounds = this.statsGroup.getBounds().getBounds2D();
		this.statsGroup.getTransform().getTranslation().x = -(float) super.getBounds().getWidth() - (float) bounds.getMinX() + this.margin;
		this.statsGroup.getTransform().getTranslation().z = -1f + this.margin;
		this.statsGroup.getTransform().scaleSet(0.35f);
		this.statsGroup.getTransform().updateMatrix();
//		}

		this.progressBar.getTransform().scaleSet(2, 1, 0.1f).update();
		this.progressBar.setForegroundColor(GameEngineUtils.hsvToColorToVec4f((float) Math.sin(PGLogic.TOTAL_TIME()), 1, 1, 1));
		this.progressBar.setValue((float) Math.sin(PGLogic.TOTAL_TIME()) / 2 + 0.5f).updateScaling();
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

}
