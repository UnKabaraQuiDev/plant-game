package lu.kbra.plant_game.engine.entity.go.impl;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class Footprint {

	private final Vector2i origin;
	private final Vector2i size;

	public Footprint(final Vector2i origin, final Vector2i size) {
		this.origin = new Vector2i(origin);
		this.size = new Vector2i(size);
//		this.rotation = Direction.DEFAULT();
	}

//	public void rotate(final Direction dir) {
//		switch (dir) {
//		case NORTH -> this.rotation = Direction.NORTH;
//		case EAST -> this.rotation = Direction.EAST;
//		case SOUTH -> this.rotation = Direction.SOUTH;
//		case WEST -> this.rotation = Direction.WEST;
//		case NONE -> this.rotation = Direction.DEFAULT();
//		default -> this.rotation = Direction.DEFAULT();
//		}
//	}

	public Vector2i getRotatedSize(final Direction rotation) {
		return switch (rotation) {
		case EAST, WEST -> new Vector2i(this.size.y, this.size.x);
		default -> new Vector2i(this.size);
		};
	}

	public boolean intersects(
			final Direction thisRotation,
			final Direction otherRotation,
			final Footprint otherFootprint,
			final Vector2ic relativePos) {
		final Vector2ic aSize = this.getRotatedSize(thisRotation);
		final Vector2ic bSize = otherFootprint.getRotatedSize(otherRotation);

		final int ax0 = this.origin.x();
		final int ay0 = this.origin.y();
		final int ax1 = ax0 + aSize.x();
		final int ay1 = ay0 + aSize.y();

		final int bx0 = relativePos.x() + otherFootprint.origin.x();
		final int by0 = relativePos.y() + otherFootprint.origin.y();
		final int bx1 = bx0 + bSize.x();
		final int by1 = by0 + bSize.y();

		return ax0 < bx1 && ax1 > bx0 && ay0 < by1 && ay1 > by0;
	}

	public Footprint getAbsolute(final Direction rotation) {
		final Vector2i absSize = this.getRotatedSize(rotation);
		Vector2i absOrigin = new Vector2i(this.origin);

		absOrigin = switch (rotation) {
		case EAST -> new Vector2i(this.origin.x - (this.size.y - 1), this.origin.y);
		case NORTH -> new Vector2i(this.origin.x - (this.size.x - 1), this.origin.y - (this.size.y - 1));
		case WEST -> new Vector2i(this.origin.x, this.origin.y - (this.size.x - 1));
		default -> new Vector2i(this.origin);
		};

		return new Footprint(absOrigin, absSize);
	}

	public boolean contains(final Direction rotation, final Vector2i cell) {
		final Vector2i rel = new Vector2i(cell).sub(this.origin);
		final Vector2i s = this.getRotatedSize(rotation);

		return switch (rotation) {
		case EAST -> rel.x >= -s.x + 1 && rel.x <= 0 && rel.y >= 0 && rel.y < s.y;
		case NORTH -> rel.x >= -s.x + 1 && rel.x <= 0 && rel.y >= -s.y + 1 && rel.y <= 0;
		case WEST -> rel.x >= 0 && rel.x < s.x && rel.y >= -s.y + 1 && rel.y <= 0;
		default -> rel.x >= 0 && rel.x < s.x && rel.y >= 0 && rel.y < s.y;
		};
	}

	public void forEachCell(final Vector2i pivotPos, final Direction rotation, final Consumer<Vector2i> action) {
		final Vector2i s = this.getRotatedSize(rotation);

		for (int y = -this.origin.y; y < s.y - this.origin.y; y++) {
			for (int x = -this.origin.x; x < s.x - this.origin.x; x++) {
				final Vector2i cell = switch (rotation) {
				default -> new Vector2i(pivotPos.x + x, pivotPos.y + y);
				case EAST -> new Vector2i(pivotPos.x - y, pivotPos.y + x);
				case NORTH -> new Vector2i(pivotPos.x - x, pivotPos.y - y);
				case WEST -> new Vector2i(pivotPos.x + y, pivotPos.y - x);
				};
				action.accept(cell);
			}
		}
	}

	public boolean anyCellMatch(final Vector2i pivotPos, final Direction rotation, final Predicate<Vector2i> predicate) {
		final boolean[] found = { false };
		this.forEachCell(pivotPos, rotation, cell -> {
			if (predicate.test(cell)) {
				found[0] = true;
			}
		});
		return found[0];
	}

	public boolean allCellsMatch(final Vector2i pivotPos, final Direction rotation, final Predicate<Vector2i> predicate) {
		final boolean[] allMatch = { true };
		this.forEachCell(pivotPos, rotation, cell -> {
			if (!predicate.test(cell)) {
				allMatch[0] = false;
			}
		});
		return allMatch[0];
	}

	public Vector2ic getOrigin() {
		return this.origin;
	}

	public void setOrigin(final Vector2i origin) {
		this.origin.set(origin);
	}

	public Vector2ic getSize() {
		return this.size;
	}

	public void setSize(final Vector2i size) {
		this.size.set(size);
	}

	@Override
	public String toString() {
		return "Footprint [origin=" + this.origin + ", size=" + this.size + "]";
	}

	public static Footprint union(final Footprint a, final Footprint b) {
		final Vector2ic aOrigin = a.getOrigin();
		final Vector2ic aSize = a.getSize();
		final Vector2ic bOrigin = b.getOrigin();
		final Vector2ic bSize = b.getSize();

		final int ax0 = -aOrigin.x();
		final int ay0 = -aOrigin.y();
		final int ax1 = ax0 + aSize.x();
		final int ay1 = ay0 + aSize.y();

		final int bx0 = -bOrigin.x();
		final int by0 = -bOrigin.y();
		final int bx1 = bx0 + bSize.x();
		final int by1 = by0 + bSize.y();

		final int ux0 = Math.min(ax0, bx0);
		final int uy0 = Math.min(ay0, by0);
		final int ux1 = Math.max(ax1, bx1);
		final int uy1 = Math.max(ay1, by1);

		final Vector2i newSize = new Vector2i(ux1 - ux0, uy1 - uy0);

		final Vector2i newOrigin = new Vector2i(-ux0, -uy0 + 1);

		return new Footprint(newOrigin, newSize);
	}

}
