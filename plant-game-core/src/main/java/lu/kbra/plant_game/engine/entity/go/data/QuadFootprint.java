package lu.kbra.plant_game.engine.entity.go.data;

import java.util.function.Consumer;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class QuadFootprint extends Footprint {

	protected final Vector2ic min;
	protected final Vector2ic max;

	protected final Vector2fc center;
	protected final Vector2ic centerTile;
	protected final Vector2ic size;

	public QuadFootprint(final Vector2ic min, final Vector2ic max) {
		this(min, max, false);
	}

	public QuadFootprint(final Vector2ic min, final Vector2ic max, final boolean inclusive) {
		this.min = new Vector2i(min);
		this.max = inclusive ? new Vector2i(max).add(1, 1) : new Vector2i(max);

		this.size = new Vector2i(this.max.x() - this.min.x(), this.max.y() - this.min.y());
		this.center = new Vector2f(this.min.x() + this.size.x() * 0.5f, this.min.y() + this.size.y() * 0.5f);
		this.centerTile = new Vector2i((int) Math.floor(this.min.x()), (int) Math.floor(this.min.y()));
	}

	@Override
	public void forEachCell(final Vector2ic pivotPos, final Direction rotation, final Consumer<Vector2i> action) {
		final Vector2i tmp = new Vector2i();

		for (int y = this.min.y(); y < this.max.y(); y++) {
			for (int x = this.min.x(); x < this.max.x(); x++) {
				tmp.set(x, y);
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

		return local.x >= this.min.x() && local.y >= this.min.y() && local.x < this.max.x() && local.y < this.max.y();
	}

	@Override
	public QuadFootprint toQuad() {
		return this;
	}

	public Vector2ic getMin() {
		return this.min;
	}

	public Vector2ic getMax() {
		return this.max;
	}

	public Vector2fc getCenter() {
		return this.center;
	}

	public Vector2ic getCenterTile() {
		return this.centerTile;
	}

	public Vector2ic getSize() {
		return this.size;
	}

	@Override
	public String toString() {
		return "QuadFootprint@" + System.identityHashCode(this) + " [min=" + this.min + ", max=" + this.max + ", center=" + this.center
				+ ", centerTile=" + this.centerTile + ", size=" + this.size + ", cachedCells=" + this.cachedCells + ", cachedLines="
				+ this.cachedLines + ", cachedPolygons=" + this.cachedPolygons + "]";
	}

}