package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector2i;

import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.impl.UniqueID;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface PlaceableObject extends Transform3DOwner, UniqueID, SceneEntity {

	Vector2i getFootprint();

	Vector2i getOriginOffset();

	default boolean isPlaceable(final WorldLevelScene scene, final Vector2i tile, final Direction rotation) {
		final TerrainMesh mesh = scene.getTerrain().getMesh();
		final int firstLevel = mesh.getCellHeight(tile.x, tile.y);

		if (firstLevel <= scene.getWaterLevel().getTransform().getTranslation().y) {
			return false;
		}

		final Vector2i footprint = this.getFootprint(), offset = this.getOriginOffset();

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

	default void placeDown(final TerrainObject terrain, final Vector2i tile, final Direction rotation) {
		final Transform3D transform = this.getTransform();

		rotation.rotate(transform.getRotation());
		transform.translationSet(terrain.getCellPosition(tile));

		transform.updateMatrix();
	}

}
