package lu.kbra.plant_game.engine.entity.impl;

import java.util.function.Consumer;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.data.LevelData;

public interface StarterPodObject extends GameObject, PlaceableObject, Consumer<LevelData> {

}
