package lu.kbra.plant_game.engine.entity.ui.slider;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:slider.volume")
public class VolumeSliderUIObject extends AnchoredSliderUIObject {

	public VolumeSliderUIObject(final String str, final TextEmitter text) {
		super(str, text);
		super.min = 0;
		super.max = 100;
		super.value = 50;
		super.divisors = 10;
	}

	@Override
	public String toString() {
		return "VolumeSliderUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", min=" + this.min + ", max=" + this.max + ", value=" + this.value + ", divisors=" + this.divisors
				+ ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
