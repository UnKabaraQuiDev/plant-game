package lu.kbra.plant_game.engine.entity.ui.text;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@BufferSize(10)
public class IntegerTextUIObject extends ProgrammaticTextUIObject implements NeedsPostConstruct {

	protected String key;
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
	public void init() {
		this.updateTextContent();
	}

	public void updateTextContent() {
		super.getTextEmitter().setText(this.buildText());
		if (this.colorMaterial != null) {
			this.getTextEmitter().setForegroundColor(this.colorMaterial.getColor());
		}
	}

	public String buildText() {
		return (this.forceSign && this.value >= 0 ? "+" : "") + (this.padding ? PCUtils
				.leftPadString(Integer.toString(this.value), this.paddingZero ? "0" : " ", this.paddingLength + (this.forceSign ? -1 : 0))
				: Integer.toString(this.value));
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

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public void setKey(final String key) {
		this.key = key;
	}

	public boolean isPaddingZero() {
		return this.paddingZero;
	}

	public void setPaddingZero(final boolean paddingZero) {
		this.paddingZero = paddingZero;
	}

	@Override
	public String toString() {
		return "IntegerTextUIObject [key=" + this.key + ", value=" + this.value + ", forceSign=" + this.forceSign + ", padding="
				+ this.padding + ", paddingZero=" + this.paddingZero + ", paddingLength=" + this.paddingLength + ", colorMaterial="
				+ this.colorMaterial + ", transform=" + this.transform + ", parent=" + this.parent + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
