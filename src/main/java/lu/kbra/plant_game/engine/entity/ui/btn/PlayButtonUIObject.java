package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:btn.play")
public class PlayButtonUIObject extends GrowOnHoverTextUIObject {

	public PlayButtonUIObject(String str, TextEmitter text) {
		super(str, text);
	}

	public PlayButtonUIObject(String str, TextEmitter text, Transform3D transform) {
		super(str, text, transform);
	}

}
