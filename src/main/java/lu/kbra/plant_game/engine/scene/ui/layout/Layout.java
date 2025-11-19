package lu.kbra.plant_game.engine.scene.ui.layout;

import java.util.List;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;

public interface Layout {

	void doLayout(List<UIObject> children, float aspectRatio);

}
