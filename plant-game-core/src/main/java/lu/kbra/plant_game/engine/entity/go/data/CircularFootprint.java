package lu.kbra.plant_game.engine.entity.go.data;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class CircularFootprint extends Footprint {

	protected final Vector2ic center;
	protected final int radius;

	public CircularFootprint(final Vector2ic center, final int radius) {
		this.center = new Vector2i(center);
		this.radius = radius;
	}

	@Override
	public void forEachCell(final Vector2ic pivotPos, final Direction rotation, final Consumer<Vector2i> action) {
		final Vector2i tmp = new Vector2i();

		final int r2 = this.radius * this.radius;

		for (int y = -this.radius; y <= this.radius; y++) {
			for (int x = -this.radius; x <= this.radius; x++) {
				if (x * x + y * y > r2) {
					continue;
				}

				tmp.set(this.center.x() + x, this.center.y() + y);
				rotation.rotate(tmp);
				tmp.add(pivotPos);
				action.accept(tmp);
			}
		}
	}

	@Override
	public boolean contains(final Direction rotation, final Vector2i cell) {
		final Vector2i local = new Vector2i(cell);
		rotation.inverse().rotate(local);

		final int dx = local.x - this.center.x();
		final int dy = local.y - this.center.y();

		return dx * dx + dy * dy <= this.radius * this.radius;
	}

	@Override
	public QuadFootprint toQuad() {
		return new QuadFootprint(new Vector2i(this.center).sub(this.radius, this.radius),
				new Vector2i(this.center).add(this.radius + 1, this.radius + 1));
	}

	@Override
	public Set<Vector2ic> computeLocalCells(final Direction rotation) {
		final Set<Vector2ic> cells = new HashSet<>();

		final int r2 = this.radius * this.radius;

		for (int y = -this.radius; y <= this.radius; y++) {
			for (int x = -this.radius; x <= this.radius; x++) {
				if (x * x + y * y <= r2) {
					cells.add(new Vector2i(this.center.x() + x, this.center.y() + y));
				}
			}
		}

		return cells;
	}

	@Override
	public String toString() {
		return "CircularFootprint@" + System.identityHashCode(this) + " [center=" + this.center + ", radius=" + this.radius
				+ ", cachedCells=" + this.cachedCells + ", cachedLines=" + this.cachedLines + ", cachedPolygons=" + this.cachedPolygons
				+ "]";
	}

}
