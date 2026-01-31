package lu.kbra.plant_game.engine.entity.ui.slider;

import java.awt.geom.Rectangle2D;
import java.util.Optional;

import org.joml.Math;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsBoundsInput;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class SliderUIObject extends TextUIObject
		implements NeedsBoundsInput, NeedsPostConstruct, UISceneParentAware, AbsoluteTransformedBoundsOwner {

	protected float min;
	protected float max;
	protected float value;
	protected int divisors;

	public SliderUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public void init() {
		this.updateText(true);
	}

	@Override
	public boolean boundsInput(final WindowInputHandler inputHandler) {
		if (!inputHandler.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			return false;
		}

		final Optional<UIScene> uiScene = this.getUISceneParent();
		if (uiScene.isEmpty()) {
			return false;
		}

//		assert this.getTextEmitter().getTextAlignment() == TextAlignment.TEXT_LEFT : this.getTextEmitter().getTextAlignment();
		assert this.getTextEmitter().getLineCount() == 1;

		final Vector2f click = uiScene.get().getMouseCoords(inputHandler);
		final float clickX = click.x();
		final Rectangle2D bounds = this.getAbsoluteTransformedBounds().getBounds2D();
		final int charCount = this.getTextEmitter().getStringLength();
		final float partWidth = (float) bounds.getWidth() / charCount;
		final float localX = clickX - (float) bounds.getMinX() /*- this.getTextEmitter().getCharSize().x() / 2*/;
		final int index = (int) ((localX + this.getTextEmitter().getCharSize().x() / 2) / partWidth);

		assert index >= 0 && index < charCount : "Invalid index: " + index + " in range: [0, " + charCount + "]";

		if (index == 0) {
			if (!inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				return false;
			}

			this.value = Math.clamp(this.min, this.max, this.value - (this.max - this.min) / this.divisors);
		} else if (index == charCount - 1) {
			if (!inputHandler.isMouseButtonPressedOnce(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				return false;
			}

			this.value = Math.clamp(this.min, this.max, this.value + (this.max - this.min) / this.divisors);
		} else {
			float t = (index) / (charCount - 3f); // idk why this is 3 but it works

			t = Math.clamp(0f, 1f, t);
			this.value = Math.lerp(this.min, this.max, t);
		}

		this.updateText(true);

		return true;
	}

	public String buildString() {
		final int dCount = (int) (this.value / (this.max - this.min) * this.divisors);
		return "<[" + ("O".repeat(dCount) + " ".repeat(this.divisors - dCount)) + "]>";
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

	@Override
	public String toString() {
		return "SliderUIObject [min=" + this.min + ", max=" + this.max + ", value=" + this.value + ", divisors=" + this.divisors
				+ ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
