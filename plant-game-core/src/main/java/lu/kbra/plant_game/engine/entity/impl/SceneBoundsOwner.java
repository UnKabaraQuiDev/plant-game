package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.standalone.gameengine.scene.Scene;

public interface SceneBoundsOwner<T extends UIObject> extends BoundsOwner, Scene<T> {

}
