package lu.kbra.plant_game.engine.scene.ui;

import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.standalone.gameengine.objs.entity.Component;

public class UIObjectGroup extends ObjectGroup<UIObject> {

	public UIObjectGroup(String str, Component... cs) {
		super(str, cs);
	}

	public UIObjectGroup(String str, List<UIObject> entities, Component... cs) {
		super(str, entities, cs);
	}

	public UIObjectGroup(String str, UIObject... values) {
		super(str, values);
	}

}
