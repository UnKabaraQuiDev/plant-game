package lu.kbra.plant_game.engine.entity.ui.text;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:string-placeholder")
@BufferSize(10)
public class IntegerTextUIObject extends ProgrammaticTextUIObject {

	protected final String key;
	protected int value;
	protected boolean forceSign = false;
	protected boolean padding = false;
	protected boolean paddingZero = false;
	protected int paddingLength = 4;
	protected ColorMaterial colorMaterial = ColorMaterial.WHITE;

	public IntegerTextUIObject(final String str, final TextEmitter text, final String key) {
		super(str, text, key);
		this.key = key;
		this.updateTextContent();
	}

	public IntegerTextUIObject(final String str, final TextEmitter text, final String key, final Transform3D transform) {
		super(str, text, key, transform);
		this.key = key;
		this.updateTextContent();
	}

	public IntegerTextUIObject(final String str, final TextEmitter text, final String key, final ColorMaterial mat) {
		super(str, text, key);
		this.key = key;
		this.colorMaterial = mat;
		this.updateTextContent();
	}

	public IntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final ColorMaterial mat,
			final Transform3D transform) {
		super(str, text, key, transform);
		this.key = key;
		this.colorMaterial = mat;
		this.updateTextContent();
	}

	public IntegerTextUIObject(final String str, final TextEmitter text, final String key, final int value) {
		super(str, text, key);
		this.key = key;
		this.value = value;
		this.updateTextContent();
	}

	public IntegerTextUIObject(final String str, final TextEmitter text, final String key, final int value, final Transform3D transform) {
		super(str, text, key, transform);
		this.key = key;
		this.value = value;
		this.updateTextContent();
	}

	public IntegerTextUIObject(final String str, final TextEmitter text, final String key, final int value, final boolean forceSign) {
		super(str, text, key);
		this.key = key;
		this.value = value;
		this.forceSign = forceSign;
		this.updateTextContent();
	}

	public IntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final Transform3D transform) {
		super(str, text, key, transform);
		this.key = key;
		this.value = value;
		this.forceSign = forceSign;
		this.updateTextContent();
	}

	public IntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final int padLength) {
		super(str, text, key);
		this.key = key;
		this.value = value;
		this.forceSign = forceSign;
		this.padding = padding;
		this.paddingLength = padLength;
		this.updateTextContent();
	}

	public IntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final int padLength,
			final Transform3D transform) {
		super(str, text, key, transform);
		this.key = key;
		this.value = value;
		this.forceSign = forceSign;
		this.padding = padding;
		this.paddingLength = padLength;
		this.updateTextContent();
	}

	public IntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final boolean paddingZero,
			final int padLength,
			final ColorMaterial mat) {
		super(str, text, key);
		this.key = key;
		this.value = value;
		this.forceSign = forceSign;
		this.padding = padding;
		this.paddingZero = paddingZero;
		this.paddingLength = padLength;
		this.colorMaterial = mat;
		this.updateTextContent();
	}

	public IntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final boolean paddingZero,
			final int padLength,
			final ColorMaterial mat,
			final Transform3D transform) {
		super(str, text, key, transform);
		this.key = key;
		this.value = value;
		this.forceSign = forceSign;
		this.padding = padding;
		this.paddingZero = paddingZero;
		this.paddingLength = padLength;
		this.colorMaterial = mat;
		this.updateTextContent();
	}

	public void updateTextContent() {
		super.getTextEmitter()
				.setText((this.forceSign && this.value >= 0 ? "+" : "")
						+ (this.padding
								? PCUtils
										.leftPadString(Integer.toString(this.value),
												this.paddingZero ? "0" : " ",
												this.paddingLength + (this.forceSign ? -1 : 0))
								: Integer.toString(this.value)));
		if (this.colorMaterial != null) {
			this.getTextEmitter().setForegroundColor(this.colorMaterial.getColor());
		}
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
		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
			this.updateText();
		});
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

}
