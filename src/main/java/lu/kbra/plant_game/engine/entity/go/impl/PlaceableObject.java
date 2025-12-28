package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainObject;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.standalone.gameengine.impl.UniqueID;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface PlaceableObject extends Transform3DOwner, UniqueID, SceneEntity, FootprintOwner {

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

		final Footprint thisFootprint = this.getStaticMeshFootprint();

		return thisFootprint.allCellsMatch(tile, rotation, v -> mesh.getCellHeight(v) == cellHeight);
	}

	default void placeDown(final TerrainObject terrain, final Vector2i tile, final Direction rotation) {
		final Transform3D transform = this.getTransform();

		rotation.rotation(transform.getRotation());
		transform.translationSet(terrain.getCellPosition(tile));
		transform.updateMatrix();

		this.setRotation(rotation);
	}

	default Vector2i getRotated(final Vector2ic v) {
		final Direction dir = this.getRotation();
		final Vector2i pivot = this.getStaticMeshFootprint().getOrigin();

		final int dx = v.x() - pivot.x;
		final int dy = v.y() - pivot.y;

		final Vector2i rotated = switch (dir) {
		default -> new Vector2i(dx, dy);
		case EAST -> new Vector2i(-dy, dx);
		case NORTH -> new Vector2i(-dx, -dy);
		case WEST -> new Vector2i(dy, -dx);
		};

		rotated.add(pivot);
		return rotated;
	}

	default Vector2f getRotated(final Vector2fc v) {
		final Direction dir = this.getRotation();
		final Vector2i pivot = this.getStaticMeshFootprint().getOrigin();

		final float dx = v.x() - pivot.x;
		final float dy = v.y() - pivot.y;

		final Vector2f rotated = switch (dir) {
		default -> new Vector2f(dx, dy);
		case EAST -> new Vector2f(-dy, dx);
		case NORTH -> new Vector2f(-dx, -dy);
		case WEST -> new Vector2f(dy, -dx);
		};

		rotated.add(pivot.x(), pivot.y());
		return rotated;
	}

	default boolean intersects(final Vector2i thisPos, final Vector2i otherPos, final PlaceableObject other) {
		return this
				.getStaticMeshFootprint()
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

}
