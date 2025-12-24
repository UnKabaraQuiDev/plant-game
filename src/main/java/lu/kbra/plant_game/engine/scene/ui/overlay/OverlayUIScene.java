package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.util.function.Consumer;

import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
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
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

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

		final TextData td = new TextData(UIObjectFactory.DEFAULT_CHAR_SIZE, TextAlignment.TEXT_RIGHT, 4);

		this.<WaterIconUIObject>add(workers, WaterIconUIObject.class, obj -> this.waterIcon = obj, new Transform3D().scaleMul(iconScale));
		this.<MoneyIconUIObject>add(workers, MoneyIconUIObject.class, obj -> this.moneyIcon = obj, new Transform3D().scaleMul(iconScale));
		this
				.<EnergyIconUIObject>add(workers,
						EnergyIconUIObject.class,
						obj -> this.energyIcon = obj,
						new Transform3D().scaleMul(iconScale));

		this
				.<IntegerTextUIObject>add(workers,
						IntegerTextUIObject.class,
						obj -> this.waterText = obj,
						td,
						"water-count",
						new Transform3D().scaleMul(textScale));
		this
				.<IntegerTextUIObject>add(workers,
						IntegerTextUIObject.class,
						obj -> this.moneyText = obj,
						td,
						"money-count",
						new Transform3D().scaleMul(textScale));
		this
				.<IntegerTextUIObject>add(workers,
						IntegerTextUIObject.class,
						obj -> this.energyText = obj,
						td,
						"energy-count",
						new Transform3D().scaleMul(textScale));

		td.setBufferSize(3);

		this
				.<SignedIntegerTextUIObject>add(workers,
						SignedIntegerTextUIObject.class,
						obj -> this.waterPopupText = obj,
						td,
						"water-popup",
						ColorMaterial.LIGHT_GREEN,
						ColorMaterial.BLACK,
						ColorMaterial.RED,
						new Transform3D().scaleMul(textScale));
		this
				.<SignedIntegerTextUIObject>add(workers,
						SignedIntegerTextUIObject.class,
						obj -> this.moneyPopupText = obj,
						td,
						"money-popup",
						ColorMaterial.LIGHT_GREEN,
						ColorMaterial.BLACK,
						ColorMaterial.RED,
						new Transform3D().scaleMul(textScale));
		this
				.<SignedIntegerTextUIObject>add(workers,
						SignedIntegerTextUIObject.class,
						obj -> this.energyPopupText = obj,
						td,
						"energy-popup",
						ColorMaterial.LIGHT_GREEN,
						ColorMaterial.BLACK,
						ColorMaterial.RED,
						new Transform3D().scaleMul(textScale));
	}

	private <T extends UIObject> void add(
			final Dispatcher workers,
			final Class<T> clazz,
			final Consumer<T> consumer,
			final Object... args) {
		UIObjectFactory.create(clazz, this.statsGroup, args).then(workers, consumer).push();
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		super.input(inputHandler, dTime, frameState);
	}

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final float dTime,
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		super.update(inputHandler, dTime, compositor, workers, render);

		if (this.waterText != null && this.waterText.getValue() != 100) {
			this.waterText.setValue(100);
			this.waterText.flushValue();
		}
		if (this.energyText != null && this.energyText.getValue() != 99) {
			this.energyText.setValue(99);
			this.energyText.flushValue();
		}

		if (this.waterPopupText != null && this.waterPopupText.getValue() != 100) {
			this.waterPopupText.setValue(100);
			this.waterPopupText.flushValue();
		}
		if (this.energyPopupText != null && this.energyPopupText.getValue() != -99) {
			this.energyPopupText.setValue(-99);
			this.energyPopupText.flushValue();
		}
	}

}
