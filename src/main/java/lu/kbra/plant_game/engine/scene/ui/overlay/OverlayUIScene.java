package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
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

public class OverlayUIScene extends UIScene {

	private final LayoutOffsetUIObjectGroup statsGroup = new LayoutOffsetUIObjectGroup("stats", new FlowLayout(true, 0.01f));
	private UIObject waterIcon, moneyIcon, energyIcon;
	private IntegerTextUIObject waterText, moneyText, energyText;
	private SignedIntegerTextUIObject waterPopupText, moneyPopupText, energyPopupText;

	public OverlayUIScene(final CacheManager parent) {
		super("game-overlay", parent);
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		super.addEntity(this.statsGroup);

		final float iconScale = 0.1f, textScale = iconScale * 2;

		final OverlayIntegerStatLine waterGroup = new OverlayIntegerStatLine("water");
		waterGroup
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
		this.statsGroup.add(waterGroup);

		final OverlayIntegerStatLine moneyGroup = new OverlayIntegerStatLine("money");
		moneyGroup
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
		this.statsGroup.add(moneyGroup);

		final OverlayIntegerStatLine energyGroup = new OverlayIntegerStatLine("energy");
		energyGroup
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
		this.statsGroup.add(energyGroup);
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		super.input(inputHandler, dTime, frameState);

		if (inputHandler.wasResized()) {
			this.statsGroup.doLayout();
			final Rectangle2D bounds = this.statsGroup.getLocalTransformedBounds().getBounds2D();
			this.statsGroup.getTransform().getTranslation().x = (float) super.getBounds().getMinX();
			this.statsGroup.getTransform().updateMatrix();
		}
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
