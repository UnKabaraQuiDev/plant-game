package lu.kbra.plant_game.base.entity.go.obj;

import java.util.function.Consumer;

import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;

public interface StarterPodObject extends PlaceableObject, Consumer<LevelData> {

}
