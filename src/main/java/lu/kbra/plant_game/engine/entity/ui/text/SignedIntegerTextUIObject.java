package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.BufferSize;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
@BufferSize(10)
public class SignedIntegerTextUIObject extends IntegerTextUIObject {

	protected ColorMaterial neutralColor;
	protected ColorMaterial negativeColor;
	protected ColorMaterial positiveColor;

	public SignedIntegerTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
		super.value = 0;
		super.forceSign = true;
		super.padding = true;
		super.paddingZero = false;
		super.paddingLength = 4;
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
		return "SignedIntegerTextUIObject [neutralColor=" + this.neutralColor + ", negativeColor=" + this.negativeColor + ", positiveColor="
				+ this.positiveColor + ", key=" + this.key + ", value=" + this.value + ", forceSign=" + this.forceSign + ", padding="
				+ this.padding + ", paddingZero=" + this.paddingZero + ", paddingLength=" + this.paddingLength + ", colorMaterial="
				+ this.colorMaterial + ", transform=" + this.transform + ", parent=" + this.parent + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
