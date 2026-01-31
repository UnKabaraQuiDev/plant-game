package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:title.volume")
public class VolumeTextUIObject extends AnchoredTextUIObject {

	public VolumeTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public String toString() {
		return "VolumeTextUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
