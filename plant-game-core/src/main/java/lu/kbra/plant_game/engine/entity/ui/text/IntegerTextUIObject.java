package lu.kbra.plant_game.engine.entity.ui.text;

import java.util.function.Function;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@BufferSize(10)
public class IntegerTextUIObject extends ProgrammaticTextUIObject implements NeedsPostConstruct {

	protected int value;
	protected boolean forceSign = false;
	protected boolean padding = false;
	protected boolean paddingZero = false;
	protected int paddingLength = 4;
	protected ColorMaterial colorMaterial = ColorMaterial.WHITE;

	public IntegerTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public void postConstruct() {
		this.updateTextContent();
	}

	public void updateTextContent() {
		super.getTextEmitter().setText(this.buildText());
		if (this.colorMaterial != null) {
			this.getTextEmitter().setForegroundColor(this.colorMaterial.getColor());
		}
	}

	public void updateTextContent(final String suffix) {
		super.getTextEmitter().setText(this.buildText(suffix));
		if (this.colorMaterial != null) {
			this.getTextEmitter().setForegroundColor(this.colorMaterial.getColor());
		}
	}

	public void updateTextContent(final Function<String, String> transformer) {
		super.getTextEmitter().setText(transformer.apply(this.buildText()));
		if (this.colorMaterial != null) {
			this.getTextEmitter().setForegroundColor(this.colorMaterial.getColor());
		}
	}

	public String buildText() {
		return this.buildText(null);
	}

	public String buildText(final String suffix) {
		final String sign = (this.forceSign && this.value >= 0) ? "+" : "";
		final String number = Integer.toString(this.value);
		final String fullSuffix = suffix == null ? "" : suffix;

		if (!this.padding) {
			return sign + number + fullSuffix;
		}

		final int availableLength = this.paddingLength - fullSuffix.length() - sign.length();

		String trimmedNumber = number;

		if (trimmedNumber.length() > availableLength) {
			trimmedNumber = trimmedNumber.substring(trimmedNumber.length() - availableLength);
		}

		final String padded = PCUtils.leftPadString(trimmedNumber, this.paddingZero ? "0" : " ", Math.max(0, availableLength));

		return sign + padded + fullSuffix;
	}

	public int getValue() {
		return this.value;
	}

	public IntegerTextUIObject setValue(final int value) {
		this.value = value;
		return this;
	}

	public void flushValue() {
		this.updateTextContent();
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(this::updateText);
	}

	public void flushValue(final String suffix) {
		this.updateTextContent(suffix);
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(this::updateText);
	}

	public boolean isForceSign() {
		return this.forceSign;
	}

	public void setForceSign(final boolean forceSign) {
		this.forceSign = forceSign;
	}

	public boolean isPadding() {
		return this.padding;
	}

	public void setPadding(final boolean padding) {
		this.padding = padding;
	}

	public int getPaddingLength() {
		return this.paddingLength;
	}

	public void setPaddingLength(final int paddingLength) {
		this.paddingLength = paddingLength;
	}

	public ColorMaterial getColorMaterial() {
		return this.colorMaterial;
	}

	public void setColorMaterial(final ColorMaterial colorMaterial) {
		this.colorMaterial = colorMaterial;
	}

	public boolean isPaddingZero() {
		return this.paddingZero;
	}

	public void setPaddingZero(final boolean paddingZero) {
		this.paddingZero = paddingZero;
	}

	@Override
	public String toString() {
		return "IntegerTextUIObject@" + System.identityHashCode(this) + " [value=" + this.value + ", forceSign=" + this.forceSign
				+ ", padding=" + this.padding + ", paddingZero=" + this.paddingZero + ", paddingLength=" + this.paddingLength
				+ ", colorMaterial=" + this.colorMaterial + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
