package lu.kbra.plant_game.engine.entity.go.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.standalone.gameengine.utils.consts.Direction;

public abstract class Footprint {

	public record Line(Vector2fc a, Vector2fc b) {
	}

	protected Set<Vector2ic> cachedCells;
	protected Set<Line> cachedLines;
	protected List<List<Vector2fc>> cachedPolygons;

	public abstract void forEachCell(Vector2ic pivotPos, Direction rotation, Consumer<Vector2i> action);

	public abstract boolean contains(Direction rotation, Vector2i cell);

	public abstract QuadFootprint toQuad();

	public Set<Vector2ic> computeLocalCells(final Direction rotation) {
		final Set<Vector2ic> cells = new HashSet<>();

		this.forEachCell(new Vector2i(0), rotation, c -> cells.add(new Vector2i(c)));

		return cells;
	}

	protected final Set<Vector2ic> getLocalCells() {
		if (this.cachedCells == null) {
			final Set<Vector2ic> raw = this.computeLocalCells(Direction.DEFAULT);

			this.cachedCells = Set.copyOf(raw);
		}
		return this.cachedCells;
	}

	public boolean anyCellMatch(final Vector2ic pivotPos, final Direction rotation, final Predicate<Vector2i> predicate) {
		final boolean[] found = { false };

		this.forEachCell(pivotPos, rotation, cell -> {
			if (!found[0] && predicate.test(cell)) {
				found[0] = true;
			}
		});

		return found[0];
	}

	public boolean allCellsMatch(final Vector2ic pivotPos, final Direction rotation, final Predicate<Vector2i> predicate) {
		final boolean[] all = { true };

		this.forEachCell(pivotPos, rotation, cell -> {
			if (all[0] && !predicate.test(cell)) {
				all[0] = false;
			}
		});

		return all[0];
	}

	public boolean intersects(final Direction thisRotation, final Direction otherRotation, final Footprint other, final Vector2ic offset) {
		return this.anyCellMatch(new Vector2i(), thisRotation, cell -> other.contains(otherRotation, new Vector2i(cell).sub(offset)));
	}

	public int getCellCount() {
		if (this.cachedCells == null) {
			this.cachedCells = this.computeLocalCells(Direction.DEFAULT);
		}
		return this.cachedCells.size();
	}

	@Override
	public String toString() {
		return "Footprint@" + System.identityHashCode(this) + " []";
	}

	public final Set<Line> getLineBounds() {
		if (this.cachedLines != null) {
			return this.cachedLines;
		}

		final Set<Vector2ic> cells = this.getLocalCells();
		final Set<Line> lines = new HashSet<>();

		for (final Vector2ic c : cells) {
			final int x = c.x();
			final int y = c.y();

			checkEdge(cells, lines, x, y + 1, x, y + 1, x + 1, y + 1); // top
			checkEdge(cells, lines, x, y, x, y, x + 1, y); // bottom
			checkEdge(cells, lines, x - 1, y, x, y, x, y + 1); // left
			checkEdge(cells, lines, x + 1, y, x + 1, y, x + 1, y + 1); // right
		}

		this.cachedLines = Set.copyOf(lines);
		return this.cachedLines;
	}

	public final List<List<Vector2fc>> getOutlinePolygons() {
		if (this.cachedPolygons != null) {
			return this.cachedPolygons;
		}

		final Set<Line> merged = mergeCollinear(this.getLineBounds());
		this.cachedPolygons = List.copyOf(buildLoops(merged));

		return this.cachedPolygons;
	}

	private static void checkEdge(
			final Set<Vector2ic> cells,
			final Set<Line> lines,
			final int nx,
			final int ny,
			final int x1,
			final int y1,
			final int x2,
			final int y2) {
		// if neighbor is NOT inside → this edge is boundary
		if (!cells.contains(new Vector2i(nx, ny))) {
			lines.add(new Line(new Vector2f(x1, y1), new Vector2f(x2, y2)));
		}
	}

	private static Line normalize(final Line l) {
		if (l.a().x() < l.b().x()) {
			return l;
		}
		if (l.a().x() > l.b().x()) {
			return new Line(l.b(), l.a());
		}

		return l.a().y() <= l.b().y() ? l : new Line(l.b(), l.a());
	}

	public static List<List<Vector2fc>> buildLoops(final Set<Line> lines) {
		final Map<Vector2fc, List<Vector2fc>> graph = new HashMap<>();

		for (final Line l : lines) {
			graph.computeIfAbsent(l.a(), k -> new ArrayList<>()).add(l.b());
			graph.computeIfAbsent(l.b(), k -> new ArrayList<>()).add(l.a());
		}

		final List<List<Vector2fc>> loops = new ArrayList<>();
		final Set<Vector2fc> visited = new HashSet<>();

		for (final Vector2fc start : graph.keySet()) {
			if (visited.contains(start)) {
				continue;
			}

			final List<Vector2fc> loop = new ArrayList<>();
			Vector2fc current = start;
			Vector2fc prev = null;

			do {
				loop.add(current);
				visited.add(current);

				final List<Vector2fc> neighbors = graph.get(current);

				Vector2fc next = null;
				for (final Vector2fc n : neighbors) {
					if (!n.equals(prev)) {
						next = n;
						break;
					}
				}

				prev = current;
				current = next;

			} while (current != null && !current.equals(start));

			if (!loop.isEmpty()) {
				loops.add(loop);
			}
		}

		return loops;
	}

	public static Set<Line> mergeCollinear(final Set<Line> input) {
		final Map<String, List<Line>> groups = new HashMap<>();

		for (Line l : input) {
			l = normalize(l);

			final boolean horizontal = l.a().y() == l.b().y();
			final String key = horizontal ? "H:" + l.a().y() : "V:" + l.a().x();

			groups.computeIfAbsent(key, k -> new ArrayList<>()).add(l);
		}

		final Set<Line> result = new HashSet<>();

		for (final List<Line> group : groups.values()) {
			group.sort(Comparator.comparingDouble(l -> l.a().x() + l.a().y()));

			Line current = group.get(0);

			for (int i = 1; i < group.size(); i++) {
				final Line next = group.get(i);

				if (touching(current, next)) {
					current = new Line(current.a(), next.b());
				} else {
					result.add(current);
					current = next;
				}
			}

			result.add(current);
		}

		return result;
	}

	private static boolean touching(final Line a, final Line b) {
		return a.b().equals(b.a());
	}

	public static float polygonArea(final List<Vector2f> poly) {
		float area = 0;

		for (int i = 0; i < poly.size(); i++) {
			final Vector2f a = poly.get(i);
			final Vector2f b = poly.get((i + 1) % poly.size());

			area += a.x * b.y - b.x * a.y;
		}

		return area * 0.5f;
	}

	public static Footprint union(final Footprint a, final Footprint b) {
		final Set<Vector2ic> cells = new HashSet<>(a.getLocalCells());
		cells.addAll(b.getLocalCells());
		return buildBestFootprint(cells);
	}

	public static Footprint intersect(final Footprint a, final Footprint b) {
		final Set<Vector2ic> cells = new HashSet<>(a.getLocalCells());
		cells.retainAll(b.getLocalCells());
		return buildBestFootprint(cells);
	}

	public static Footprint difference(final Footprint a, final Footprint b) {
		final Set<Vector2ic> cells = new HashSet<>(a.getLocalCells());
		cells.removeAll(b.getLocalCells());
		return buildBestFootprint(cells);
	}

	private static Footprint buildBestFootprint(final Set<Vector2ic> cells) {
		if (cells.isEmpty()) {
			return new FreeformFootprint(new HashSet<>());
		}

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;

		for (final Vector2ic c : cells) {
			minX = Math.min(minX, c.x());
			minY = Math.min(minY, c.y());
			maxX = Math.max(maxX, c.x());
			maxY = Math.max(maxY, c.y());
		}

		final int width = maxX - minX + 1;
		final int height = maxY - minY + 1;

		if (cells.size() == width * height) {
			return new QuadFootprint(new Vector2i(minX, minY), new Vector2i(maxX + 1, maxY + 1));
		}

		return new FreeformFootprint(cells);
	}

}