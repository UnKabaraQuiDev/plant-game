package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:key.foward")
public class ForwardButtonUIObject extends TextUIObject {

	public ForwardButtonUIObject(String str, TextEmitter text) {
		super(str, text);
	}

	public ForwardButtonUIObject(String str, TextEmitter text, Transform3D transform) {
		super(str, text, transform);
	}

}
