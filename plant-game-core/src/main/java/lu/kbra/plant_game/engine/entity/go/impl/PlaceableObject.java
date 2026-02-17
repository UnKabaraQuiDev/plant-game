package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector2i;

import lu.kbra.plant_game.engine.entity.go.data.Footprint;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainGameObject;
import lu.kbra.plant_game.engine.entity.impl.FootprintOwner;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.impl.UniqueID;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface PlaceableObject extends Transform3DOwner, UniqueID, SceneEntity, FootprintOwner {

	String LOCALIZATION_KEY = "placeable.";

	Footprint getStaticMeshFootprint();

	@Override
	Direction getRotation();

	void setRotation(Direction dir);

	default boolean isPlaceable(final WorldLevelScene scene, final Vector2i tile, final Direction rotation) {
		final TerrainMesh mesh = scene.getTerrain().getMesh();

		if (!mesh.isInBounds(tile)) {
			return false;
		}

		final int cellHeight = mesh.getCellHeight(tile);

		if (cellHeight <= scene.getWaterHeight()) {
			return false;
		}

		return this.getFootprint().allCellsMatch(tile, rotation, v -> mesh.getCellHeight(v) == cellHeight);
	}

	default void placeDown(final TerrainGameObject terrain, final Vector2i tile, final Direction rotation) {
		final Transform3D transform = this.getTransform();

		rotation.rotation(transform.getRotation());
		transform.translationSet(terrain.getCellPosition(tile));
		transform.updateMatrix();

		this.setRotation(rotation);
	}

	default boolean intersects(final Vector2i thisPos, final Vector2i otherPos, final PlaceableObject other) {
		return this.getStaticMeshFootprint()
				.intersects(this.getRotation(), other.getRotation(), other.getStaticMeshFootprint(), otherPos.sub(thisPos, new Vector2i()));
	}

	static boolean intersects(
			final int aStartX,
			final int aEndX,
			final int aStartY,
			final int aEndY,
			final int bStartX,
			final int bEndX,
			final int bStartY,
			final int bEndY) {
		return aStartX <= bEndX && aEndX >= bStartX && aStartY <= bEndY && aEndY >= bStartY;
	}

	void confirmPlaceDown(
			final TerrainGameObject terrain,
			final Vector2i source,
			final Direction sourceRotation,
			final Vector2i currentPos,
			final Direction targetRotation);

}
