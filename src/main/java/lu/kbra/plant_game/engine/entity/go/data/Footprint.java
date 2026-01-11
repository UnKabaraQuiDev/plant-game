package lu.kbra.plant_game.engine.entity.go.data;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class Footprint {

	private final Vector2i min;
	private final Vector2i max;

	private final Vector2f center;
	private final Vector2i centerTile;
	private final Vector2i size;

	public Footprint(final Vector2ic min, final Vector2ic max) {
		this.min = new Vector2i(min);
		this.max = new Vector2i(max);

		this.size = new Vector2i();
		this.centerTile = new Vector2i();
		this.center = new Vector2f();

		this.recompute();
	}

	private void recompute() {
		this.size.set(this.max.x() - this.min.x(), this.max.y() - this.min.y());

		this.center.set(this.min.x() + this.size.x() * 0.5f, this.min.y() + this.size.y() * 0.5f);

		this.centerTile.set((int) Math.floor(this.min.x()), (int) Math.floor(this.min.y()));
	}

	public Vector2ic getMin() {
		return this.min;
	}

	public Vector2ic getMax() {
		return this.max;
	}

	public Vector2ic getSize() {
		return this.size;
	}

	public Vector2fc getCenter() {
		return this.center;
	}

	public Vector2ic getCenterTile() {
		return this.centerTile;
	}

	public void setMin(final Vector2ic min) {
		this.min.set(min);
		this.recompute();
	}

	public void setMax(final Vector2ic max) {
		this.max.set(max);
		this.recompute();
	}

	public Footprint getAbsolute(final Direction rotation) {
		if (rotation == Direction.NONE || rotation == Direction.SOUTH) {
			return new Footprint(this.min, this.max);
		}

		final Vector2i a = rotate(this.min, rotation);
		final Vector2i b = rotate(this.max, rotation);

		final Vector2i outMin = new Vector2i(Math.min(a.x, b.x), Math.min(a.y, b.y));
		final Vector2i outMax = new Vector2i(Math.max(a.x, b.x), Math.max(a.y, b.y));

		return new Footprint(outMin, outMax);
	}

	public boolean intersects(
			final Direction thisRotation,
			final Direction otherRotation,
			final Footprint other,
			final Vector2ic pivotsOffset) {

		final Footprint a = this.getAbsolute(thisRotation);
		final Footprint b = other.getAbsolute(otherRotation);

		final Vector2i bMin = new Vector2i(b.min).add(pivotsOffset);
		final Vector2i bMax = new Vector2i(b.max).add(pivotsOffset);

		return a.min.x < bMax.x && a.max.x > bMin.x && a.min.y < bMax.y && a.max.y > bMin.y;
	}

	public boolean contains(final Direction rotation, final Vector2i cell) {
		final Footprint abs = this.getAbsolute(rotation);

		return cell.x >= abs.min.x && cell.y >= abs.min.y && cell.x < abs.max.x && cell.y < abs.max.y;
	}

	public void forEachCell(final Vector2i pivotPos, final Direction rotation, final Consumer<Vector2i> action) {
		final Vector2i tmp = new Vector2i();
		for (int y = this.min.y(); y < this.max.y(); y++) {
			for (int x = this.min.x(); x < this.max.x(); x++) {
				tmp.set(x, y); // local cell
				rotation.rotate(tmp); // rotate
				tmp.add(pivotPos); // pivot offset
				action.accept(tmp);
			}
		}
	}

	public boolean anyCellMatch(final Vector2i pivotPos, final Direction rotation, final Predicate<Vector2i> predicate) {
		final Vector2i tmp = new Vector2i();
		for (int y = this.min.y(); y < this.max.y(); y++) {
			for (int x = this.min.x(); x < this.max.x(); x++) {
				tmp.set(x, y);
				rotation.rotate(tmp);
				tmp.add(pivotPos);
				if (predicate.test(tmp)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean allCellsMatch(final Vector2i pivotPos, final Direction rotation, final Predicate<Vector2i> predicate) {
		final Vector2i tmp = new Vector2i();
		for (int y = this.min.y(); y < this.max.y(); y++) {
			for (int x = this.min.x(); x < this.max.x(); x++) {
				tmp.set(x, y);
				rotation.rotate(tmp);
				tmp.add(pivotPos);
				if (!predicate.test(tmp)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Footprint [min=" + this.min + ", max=" + this.max + ", center=" + this.center + ", centerTile=" + this.centerTile
				+ ", size=" + this.size + "]";
	}

	public static Footprint union(final Footprint a, final Footprint b) {
		final Vector2i min = new Vector2i(Math.min(a.min.x, b.min.x), Math.min(a.min.y, b.min.y));

		final Vector2i max = new Vector2i(Math.max(a.max.x, b.max.x), Math.max(a.max.y, b.max.y));

		return new Footprint(min, max);
	}

	private static Vector2i rotate(final Vector2ic v, final Direction d) {
		return switch (d) {
		case EAST -> new Vector2i(v.y(), -v.x());
		case WEST -> new Vector2i(-v.y(), v.x());
		case NORTH -> new Vector2i(-v.x(), -v.y());
		default -> new Vector2i(v);
		};
	}
}
