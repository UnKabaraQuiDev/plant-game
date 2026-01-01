package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:title.volume")
public class VolumeTextUIObject extends TextUIObject {

	public VolumeTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

}
