package lu.kbra.plant_game.engine.scene.ui;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticGrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.locale.LocalizationService;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.future.ScheduledTask;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:string-placeholder")
public class OptionKeyUIObject extends ProgrammaticGrowOnHoverTextUIObject implements NeedsClick, NeedsInput {

	private String textContent = null;

	public OptionKeyUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final Scale2dDir dir,
			final Transform3D transform) {
		super(str, text, key, dir, transform);

		this.setKeyValue("x");
	}

	public OptionKeyUIObject(final String str, final TextEmitter text, final String key, final Scale2dDir dir) {
		this(str, text, key, dir, null);
	}

	@Override
	public void click(final WindowInputHandler input, final float dTime, final Scene scene) {
		this.textContent = this.getTextEmitter().getText();
		this.totalCharCount = this.textContent.length() - this.textContent.replaceAll("[^ ]", "").length();
	}

	private int totalCharCount;
	private float time = 0f;
	protected ScheduledTask updateTask;

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final Scene scene) {
		if (this.updateTask != null && this.updateTask.wasRan()) {
			this.updateTask = null;
		}
		if (this.textContent == null) {
			return;
		}

		final float duration = 1f;

		this.time += dTime;
		final float t = Interpolators.CIRC_IN.evaluate(org.joml.Math.clamp(0f, 1f, this.time / duration));

		final int replaced = Math.round(t * this.totalCharCount);

		this.getTextEmitter().setText(PCUtils.replace(this.textContent, ' ', '-', replaced, false));

		if (this.updateTask == null || this.updateTask.wasRan()) {
			this.updateTask = PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> super.getTextEmitter().updateText());
		}
	}

	public void flush() {
		this.getTextEmitter().setText(this.textContent);
		this.textContent = null;

		this.updateTask = PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> super.getTextEmitter().updateText());
	}

	public void setKeyValue(String value) {
		final int length = this.getTextEmitter().getBufferLength();
		final String loc = LocalizationService.get("key." + super.key);
		value = "[" + value + "]";
		this.getTextEmitter().setText(loc + " ".repeat(Math.max(length - loc.length() - value.length(), 0)) + value);
	}

}
