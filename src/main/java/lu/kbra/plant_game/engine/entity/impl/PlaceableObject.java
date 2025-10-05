package lu.kbra.plant_game.engine.entity.impl;

import org.joml.Vector2i;

import lu.kbra.plant_game.engine.entity.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.scene.WorldLevelScene;
import lu.kbra.standalone.gameengine.utils.gl.consts.Direction;

public interface PlaceableObject {

	Vector2i getFootprint();

	boolean isPlaceable(WorldLevelScene scene, TerrainMesh mesh, Vector2i tile, Direction rotation);

	void placeDown(WorldLevelScene scene, TerrainMesh mesh, Vector2i tile, Direction rotation);

}
