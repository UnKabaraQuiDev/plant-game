package lu.kbra.plant_game.engine.entity.go.data;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.standalone.gameengine.utils.consts.Direction;

public class FreeformFootprint extends Footprint {

	protected final Set<Vector2ic> cells = new HashSet<>();

	public FreeformFootprint(final Set<Vector2ic> cells) {
		for (final Vector2ic c : cells) {
			this.cells.add(new Vector2i(c));
		}
	}

	@Override
	public void forEachCell(final Vector2ic pivotPos, final Direction rotation, final Consumer<Vector2i> action) {
		final Vector2i tmp = new Vector2i();

		for (final Vector2ic c : this.cells) {
			tmp.set(c);
			rotation.rotate(tmp);
			tmp.add(pivotPos);
			action.accept(tmp);
		}
	}

	@Override
	public boolean contains(final Direction rotation, final Vector2i cell) {
		final Vector2i local = new Vector2i(cell);
		rotation.inverse().rotate(local);

		return this.cells.contains(local);
	}

	@Override
	public QuadFootprint toQuad() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;

		for (final Vector2ic c : this.cells) {
			minX = Math.min(minX, c.x());
			minY = Math.min(minY, c.y());
			maxX = Math.max(maxX, c.x());
			maxY = Math.max(maxY, c.y());
		}

		return new QuadFootprint(new Vector2i(minX, minY), new Vector2i(maxX + 1, maxY + 1));
	}

	@Override
	public String toString() {
		return "FreeformFootprint@" + System.identityHashCode(this) + " [cells=" + this.cells + ", cachedCells=" + this.cachedCells
				+ ", cachedLines=" + this.cachedLines + ", cachedPolygons=" + this.cachedPolygons + "]";
	}

}