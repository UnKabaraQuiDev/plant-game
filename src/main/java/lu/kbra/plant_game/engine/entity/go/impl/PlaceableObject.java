package lu.kbra.plant_game.engine.entity.go.impl;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

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

		rotation.rotate(transform.getRotation());
		transform.translationSet(terrain.getCellPosition(tile));

		transform.updateMatrix();
	}

	default void getRotated(final Vector2i footprint, final Vector2i origin) {
		final int rot = Math
				.floorMod(Math.round(this.getTransform().getRotation().getEulerAnglesXYZ(new Vector3f()).y / (float) (Math.PI / 2)), 4);

		rotateFootprint(this.getFootprint(), this.getOriginOffset(), rot, footprint, origin);
	}

	default int getRotationStep() {
		final float y = this.getTransform().getRotation().getEulerAnglesXYZ(new Vector3f()).y;
		return Math.floorMod(Math.round(y / (float) (Math.PI / 2)), 4);
	}

	default Direction getRotationDirection() {
		return Direction.getByIndex((this.getRotationStep() - 0) % 4);
	}

	default Vector2i getRotated(final Vector2i in) {
		final Vector2i origin = this.getOriginOffset();
		final Direction dir = this.getRotationDirection();

		final int x = in.x - origin.x;
		final int y = in.y - origin.y;

		return switch (dir) {
		case NORTH -> new Vector2i(x + origin.x, y + origin.y);
		case EAST -> new Vector2i(-y + origin.x, x + origin.y);
		case SOUTH -> new Vector2i(-x + origin.x, -y + origin.y);
		case WEST -> new Vector2i(y + origin.x, -x + origin.y);
		case NONE -> new Vector2i(in);
		};
	}

	default Vector2f getRotated(final Vector2f in) {
		final Vector2f origin = new Vector2f(this.getOriginOffset().x, this.getOriginOffset().y);
		final Direction dir = this.getRotationDirection();

		final float x = in.x - origin.x;
		final float y = in.y - origin.y;

		return switch (dir) {
		case NORTH -> new Vector2f(x + origin.x, y + origin.y);
		case EAST -> new Vector2f(-y + origin.x, x + origin.y);
		case SOUTH -> new Vector2f(-x + origin.x, -y + origin.y);
		case WEST -> new Vector2f(y + origin.x, -x + origin.y);
		case NONE -> new Vector2f(in);
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

	static void rotateFootprint(
			final Vector2i footprint,
			final Vector2i offset,
			final int rot,
			final Vector2i outFootprint,
			final Vector2i outOffset) {
		final int w = footprint.x;
		final int h = footprint.y;
		final int ox = offset.x;
		final int oy = offset.y;

		switch (rot) {
		case 0 -> {
			outFootprint.set(w, h);
			outOffset.set(ox, oy);
		}
		case 1 -> { // 90°
			outFootprint.set(h, w);
			outOffset.set(oy, w - 1 - ox);
		}
		case 2 -> { // 180°
			outFootprint.set(w, h);
			outOffset.set(w - 1 - ox, h - 1 - oy);
		}
		case 3 -> { // 270°
			outFootprint.set(h, w);
			outOffset.set(h - 1 - oy, ox);
		}
		}
	}

}
