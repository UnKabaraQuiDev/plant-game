package lu.kbra.plant_game.engine.scene.ui;

import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;

public class UIObjectGroup extends ObjectGroup<UIObject> {

	public UIObjectGroup(UIObject... values) {
		super(values);
	}

	public UIObjectGroup(List<UIObject> entities) {
		super(entities);
	}

}
