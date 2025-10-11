package lu.kbra.plant_game.engine.entity.impl;

import org.joml.Vector2i;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.scene.WorldLevelScene;
import lu.kbra.standalone.gameengine.impl.UniqueID;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.utils.gl.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface PlaceableObject extends Transform3Deable, UniqueID, SceneEntity {

	Vector2i getFootprint();

	Vector2i getOriginOffset();

	default boolean isPlaceable(WorldLevelScene scene, Vector2i tile, Direction rotation) {
		final TerrainMesh mesh = (TerrainMesh) scene.getTerrain().getMesh();
		final int firstLevel = mesh.getCellHeight(tile.x, tile.y);

		if (firstLevel <= scene.getWaterLevel().getTransform().getTranslation().y) {
			return false;
		}

		final Vector2i footprint = getFootprint(), offset = getOriginOffset();

		final int startX = tile.x - offset.x;
		final int endX = tile.x + (footprint.x - 1 - offset.x);

		final int startY = tile.y - offset.y;
		final int endY = tile.y + (footprint.y - 1 - offset.y);

		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (!mesh.isInBounds(x, y)) {
					return false;
				}

				if (mesh.getCellHeight(x, y) != firstLevel) {
					return false;
				}
			}
		}

		return true;
	}

	default void placeDown(WorldLevelScene scene, Vector2i tile, Direction rotation) {
		final Transform3D transform = getTransform();

		final TerrainMesh mesh = (TerrainMesh) scene.getTerrain().getMesh();
		final Vector3f meshTranslation = scene.getTerrain().getTransform().getTranslation();
		final int cellHeight = mesh.getCellHeight(tile.x, tile.y);

		rotation.rotate(transform.getRotation());
		transform.getTranslation().set(meshTranslation.x + tile.x + 0.5f, +meshTranslation.y + cellHeight,
				meshTranslation.z + tile.y + 0.5f);

		transform.updateMatrix();
	}

}
