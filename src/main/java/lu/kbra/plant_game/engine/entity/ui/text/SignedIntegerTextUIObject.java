package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:string-placeholder")
@BufferSize(10)
public class SignedIntegerTextUIObject extends IntegerTextUIObject {

	protected ColorMaterial neutralColor, negativeColor, positiveColor;

	public SignedIntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final ColorMaterial neutralColor,
			final ColorMaterial negativeColor,
			final ColorMaterial positiveColor) {
		super(str, text, key, 0, true, true, false, 4, neutralColor);
		this.neutralColor = neutralColor;
		this.negativeColor = negativeColor;
		this.positiveColor = positiveColor;
	}

	public SignedIntegerTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final ColorMaterial neutralColor,
			final ColorMaterial negativeColor,
			final ColorMaterial positiveColor,
			final Transform3D transform) {
		super(str, text, key, 0, true, true, false, 4, neutralColor, transform);
		this.neutralColor = neutralColor;
		this.negativeColor = negativeColor;
		this.positiveColor = positiveColor;
	}

	@Override
	public void updateTextContent() {
		if (this.value < 0) {
			super.setColorMaterial(this.negativeColor);
		} else if (this.value > 0) {
			super.setColorMaterial(this.positiveColor);
		} else {
			super.setColorMaterial(this.neutralColor);
		}
		super.updateTextContent();
	}

	public ColorMaterial getNeutralColor() {
		return this.neutralColor;
	}

	public void setNeutralColor(final ColorMaterial neutralColor) {
		this.neutralColor = neutralColor;
	}

	public ColorMaterial getNegativeColor() {
		return this.negativeColor;
	}

	public void setNegativeColor(final ColorMaterial negativeColor) {
		this.negativeColor = negativeColor;
	}

	public ColorMaterial getPositiveColor() {
		return this.positiveColor;
	}

	public void setPositiveColor(final ColorMaterial positiveColor) {
		this.positiveColor = positiveColor;
	}

	@Override
	public String toString() {
		return "SignedTextUIObject [neutralColor=" + this.neutralColor + ", negativeColor=" + this.negativeColor + ", positiveColor="
				+ this.positiveColor + ", key=" + this.key + ", value=" + this.value + ", forceSign=" + this.forceSign + ", padding="
				+ this.padding + ", paddingLength=" + this.paddingLength + ", colorMaterial=" + this.colorMaterial + ", active="
				+ this.active + ", name=" + this.name + ", getTextEmitter()=" + this.getTextEmitter() + ", getTransform()="
				+ this.getTransform() + "]";
	}

}
