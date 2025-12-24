package lu.kbra.plant_game.engine.entity.ui.slider;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsInput;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("")
public class SliderUIObject extends TextUIObject implements NeedsInput {

	protected float min;
	protected float max;
	protected float value;
	protected int divisors;

	public SliderUIObject(
			final String str,
			final TextEmitter text,
			final Transform3D transform,
			final float min,
			final float max,
			final float value,
			final int divisors) {
		super(str, text, transform);
		this.min = min;
		this.max = max;
		this.value = value;
		this.divisors = divisors;

		this.updateText(true);
	}

	public SliderUIObject(
			final String str,
			final TextEmitter text,
			final float min,
			final float max,
			final float value,
			final int divisors) {
		super(str, text);
		this.min = min;
		this.max = max;
		this.value = value;
		this.divisors = divisors;

		this.updateText(true);
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final Scene scene) {
		if (!inputHandler.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			return;
		}

		final Vector2f coords = ((UIScene) scene).getMouseCoords(inputHandler);
		coords.sub(GeoPlane.XZ.projectToPlane(this.getTransform().getTranslation()));
		coords.y += this.getTextEmitter().getCharSize().y() / 2;

		assert this.getTextEmitter().getTextAlignment() == TextAlignment.TEXT_LEFT : this.getTextEmitter().getTextAlignment();
		assert this.getTextEmitter().getLineCount() == 1;

		final Vector2fc charSize = this
				.getTextEmitter()
				.getCharSize()
				.mul(GeoPlane.XZ.projectToPlane(this.getTransform().getScale()), new Vector2f());

		final Vector2fc firstCharBounds = charSize;
		final Vector2fc lastCharBounds = charSize.mul(this.getTextEmitter().getColumnsCount(), 1, new Vector2f());

		final boolean dirty;
		if (coords.x() >= firstCharBounds.x() - charSize.x() && firstCharBounds.y() - charSize.y() >= 0 && coords.x() <= firstCharBounds.x()
				&& coords.y() <= firstCharBounds.y()) {
			if (!inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				return;
			}

			this.value = Math.clamp(this.min, this.max, this.value - (this.max - this.min) / this.divisors);
			dirty = true;
		} else if (coords.x() >= lastCharBounds.x() - charSize.x() && coords.y() >= 0 && coords.x() <= lastCharBounds.x()
				&& coords.y() <= lastCharBounds.y()) {
			if (!inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				return;
			}

			this.value = Math.clamp(this.min, this.max, this.value + (this.max - this.min) / this.divisors);
			dirty = true;
		} else {
			final float startX = firstCharBounds.x();
			final float endX = lastCharBounds.x() - firstCharBounds.x() - charSize.x() * 2;
			final float usableWidth = endX - startX;

			float t = (coords.x() - startX) / usableWidth;
			t = Math.clamp(0f, 1f, t);
			this.value = Math.lerp(this.min, this.max, t);

			dirty = true;
		}

		this.updateText(dirty);
	}

	public String buildString() {
		final int dCount = (int) (this.value / (this.max - this.min) * this.divisors);
		return "<[" + ("+".repeat(dCount) + " ".repeat(this.divisors - dCount)) + "]>";
	}

	public void updateText(final boolean push) {
		final String str = this.buildString();
		this.getTextEmitter().setText(str);
		if (push) {
			if (this.getTextEmitter().getBufferLength() < str.length()) {
				PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> this.getTextEmitter().resize(str.length()));
			}
			PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> this.getTextEmitter().updateText());
		}
	}

	public float getValue() {
		return this.value;
	}

	public float getMin() {
		return this.min;
	}

	public float getMax() {
		return this.max;
	}

	public int getDivisors() {
		return this.divisors;
	}

	public void setValue(final float value) {
		this.value = value;
	}

	public void setMin(final float min) {
		this.min = min;
	}

	public void setMax(final float max) {
		this.max = max;
	}

	public void setDivisors(final int divisors) {
		this.divisors = divisors;
	}

}
