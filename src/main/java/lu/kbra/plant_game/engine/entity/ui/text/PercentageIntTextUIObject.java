package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class PercentageIntTextUIObject extends IntegerTextUIObject {

	public PercentageIntTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public void init() {
		super.paddingLength--; // for the % sign
		super.init();
	}

	@Override
	public String buildText() {
		return super.buildText() + "%";
	}

	@Override
	public String toString() {
		return "PercentageIntTextUIObject [key=" + this.key + ", value=" + this.value + ", forceSign=" + this.forceSign + ", padding="
				+ this.padding + ", paddingZero=" + this.paddingZero + ", paddingLength=" + this.paddingLength + ", colorMaterial="
				+ this.colorMaterial + ", transform=" + this.transform + ", parent=" + this.parent + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
