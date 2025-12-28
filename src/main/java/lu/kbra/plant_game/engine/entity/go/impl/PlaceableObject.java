package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector2f;
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

	Direction getRotation();

	void setRotation(Direction dir);

	default boolean isPlaceable(final WorldLevelScene scene, final Vector2i tile, final Direction rotation) {
		final TerrainMesh mesh = scene.getTerrain().getMesh();
		final int cellHeight = mesh.getCellHeight(tile.x, tile.y);

		if (cellHeight <= scene.getWaterLevel().getTransform().getTranslation().y) {
			return false;
		}

		final Vector2i footprint = new Vector2i();
		final Vector2i offset = new Vector2i();

		this.getRotated(footprint, offset);

		final int startX = tile.x - offset.x;
		final int endX = tile.x + (footprint.x - 1 - offset.x);

		final int startY = tile.y - offset.y;
		final int endY = tile.y + (footprint.y - 1 - offset.y);

		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (!mesh.isInBounds(x, y)) {
					return false;
				}

				if (mesh.getCellHeight(x, y) != cellHeight) {
					return false;
				}
			}
		}

		return true;
	}

	default void placeDown(final TerrainObject terrain, final Vector2i tile, final Direction rotation) {
		final Transform3D transform = this.getTransform();

		rotation.rotation(transform.getRotation());
		transform.translationSet(terrain.getCellPosition(tile));
		transform.updateMatrix();

		this.setRotation(rotation);
	}

	default void getRotated(final Vector2i footprint) {
		rotateFootprint(this.getFootprint(), this.getOriginOffset(), this.getRotation(), footprint);
	}

	default Vector2i getRotated(final Vector2i v) {
		final Direction dir = this.getRotation();

		return switch (dir) {
		case SOUTH -> new Vector2i(v);
		case EAST -> new Vector2i(-v.y, v.x);
		case NORTH -> new Vector2i(-v.x, -v.y);
		case WEST -> new Vector2i(v.y, -v.x);
		default -> new Vector2i(0, 0);
		};
	}

	default Vector2f getRotated(final Vector2f v) {
		final Direction dir = this.getRotation();

		return switch (dir) {
		case SOUTH -> new Vector2f(v);
		case EAST -> new Vector2f(-v.y, v.x);
		case NORTH -> new Vector2f(-v.x, -v.y);
		case WEST -> new Vector2f(v.y, -v.x);
		default -> new Vector2f(0, 0);
		};
	}

	default boolean intersects(final Vector2i thisPos, final Vector2i otherPos, final PlaceableObject other) {
		final Vector2i offsetA = new Vector2i();
		final Vector2i footprintA = new Vector2i();

		this.getRotated(footprintA, offsetA);

		final int aStartX = thisPos.x - offsetA.x;
		final int aEndX = thisPos.x + (footprintA.x - 1 - offsetA.x);
		final int aStartY = thisPos.y - offsetA.y;
		final int aEndY = thisPos.y + (footprintA.y - 1 - offsetA.y);

		final Vector2i offsetB = new Vector2i();
		final Vector2i footprintB = new Vector2i();

		other.getRotated(footprintB, offsetB);

		final int bStartX = otherPos.x - offsetB.x;
		final int bEndX = otherPos.x + (footprintB.x - 1 - offsetB.x);
		final int bStartY = otherPos.y - offsetB.y;
		final int bEndY = otherPos.y + (footprintB.y - 1 - offsetB.y);

//		System.err
//				.println(this.getClass().getSimpleName() + " " + aStartX + " " + aEndX + " " + aStartY + " " + aEndY + " / " + bStartX + " "
//						+ bEndX + " " + bStartY + " " + bEndY + " = "
//						+ intersects(aStartX, aEndX, aStartY, aEndY, bStartX, bEndX, bStartY, bEndY));

		return intersects(aStartX, aEndX, aStartY, aEndY, bStartX, bEndX, bStartY, bEndY);
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

	static void rotateFootprint(final Vector2i footprint, final Vector2i offset, final Direction rot, final Vector2i outFootprint) {
		final int w = footprint.x;
		final int h = footprint.y;
		final int ox = offset.x;
		final int oy = offset.y;

		switch (rot) {
		case SOUTH -> {
			outFootprint.set(w, h);
		}
		case WEST -> { // 90°
			outFootprint.set(h, w);
		}
		case NORTH -> { // 180°
			outFootprint.set(w, h);
		}
		case EAST -> { // 270°
			outFootprint.set(h, w);
		}
		case NONE -> {
			outFootprint.set(footprint);
		}
		default -> {
			throw new IllegalArgumentException("Null rotation");
		}
		}
	}

}
