package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.engine.entity.ui.impl.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:btn.options")
public class OptionsButtonUIObject extends GrowOnHoverTextUIObject implements NeedsClick {

	public OptionsButtonUIObject(String str, TextEmitter text) {
		super(str, text);
	}

	public OptionsButtonUIObject(String str, TextEmitter text, Transform3D transform) {
		super(str, text, transform);
	}

	@Override
	public void click(WindowInputHandler input, float dTime) {

	}

}
