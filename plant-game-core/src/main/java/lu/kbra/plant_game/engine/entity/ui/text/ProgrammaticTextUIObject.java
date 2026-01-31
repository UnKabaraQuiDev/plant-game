package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
public class ProgrammaticTextUIObject extends TextUIObject implements ProgrammaticUIObject {

	protected String key;

	public ProgrammaticTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

}
