package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:string-placeholder")
public class ProgrammaticTextUIObject extends TextUIObject implements ProgrammaticUIObject {

	protected final String key;

	public ProgrammaticTextUIObject(final String str, final TextEmitter text, final String key) {
		super(str, text);
		this.key = key;
	}

	public ProgrammaticTextUIObject(final String str, final TextEmitter text, final String key, final Transform3D transform) {
		super(str, text, transform);
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}

}
