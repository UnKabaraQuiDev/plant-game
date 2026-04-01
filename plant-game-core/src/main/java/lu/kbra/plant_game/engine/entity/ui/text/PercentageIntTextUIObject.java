package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@Deprecated
public class PercentageIntTextUIObject extends IntegerTextUIObject {

	@Deprecated
	public PercentageIntTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Deprecated
	@Override
	public void postConstruct() {
		super.paddingLength--; // for the % sign
		super.postConstruct();
	}

	@Deprecated
	@Override
	public String buildText() {
		return super.buildText() + "%";
	}

	@Deprecated
	@Override
	public String toString() {
		return "PercentageIntTextUIObject@" + System.identityHashCode(this) + " [value=" + this.value + ", forceSign=" + this.forceSign
				+ ", padding=" + this.padding + ", paddingZero=" + this.paddingZero + ", paddingLength=" + this.paddingLength
				+ ", colorMaterial=" + this.colorMaterial + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
