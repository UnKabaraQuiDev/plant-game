package lu.kbra.plant_game.base.scene.menu.main;

import static lu.kbra.pclib.PCUtils.clamp;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.base.scene.menu.main.CubicCurve2DTransformer.GeneratedMeshData;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;

public final class Path2DTransformer {

	private Path2DTransformer() {
	}

	public static GeneratedMeshData generateTriangleMesh(
			final Path2D.Float path,
			final float width,
			final int resolution,
			float factor,
			final GeoPlane plane) {

		factor = clamp(factor, -1f, 1f);
		final float leftWidth = width * (1f - factor) * 0.5f;
		final float rightWidth = width * (1f + factor) * 0.5f;

		final List<Vector3f> vertices = new ArrayList<>();
		final List<Integer> indices = new ArrayList<>();

		float lastLeftX = 0, lastLeftY = 0;
		float lastRightX = 0, lastRightY = 0;
		boolean hasPrevious = false;

		float prevEndLeftX = 0, prevEndLeftY = 0;
		float prevEndRightX = 0, prevEndRightY = 0;
		boolean hasPrevEnd = false;

		final float[] coords = new float[6];
		float startX = 0, startY = 0;

		// Flatten the path using a small flatness value
		final PathIterator it = path.getPathIterator(null, 1f / resolution);

		while (!it.isDone()) {
			int segType = it.currentSegment(coords);

			switch (segType) {
			case PathIterator.SEG_MOVETO -> {
				startX = coords[0];
				startY = coords[1];

				// Connect previous end to new start with a small triangle
				if (hasPrevEnd) {
					float lx = startX + leftWidth;
					float ly = startY;
					float rx = startX - rightWidth;
					float ry = startY;

					int base = vertices.size();
					vertices.add(new Vector3f(prevEndLeftX, prevEndLeftY, 0));
					vertices.add(new Vector3f(prevEndRightX, prevEndRightY, 0));
					vertices.add(new Vector3f(plane.project(new Vector2f(lx, ly))));
					vertices.add(new Vector3f(plane.project(new Vector2f(rx, ry))));

					// Two triangles to bridge
					indices.add(base);
					indices.add(base + 2);
					indices.add(base + 1);

					indices.add(base + 1);
					indices.add(base + 2);
					indices.add(base + 3);
				}

				hasPrevious = false;
			}
			case PathIterator.SEG_LINETO -> {
				float x0 = hasPrevious ? (lastLeftX + lastRightX) / 2 : startX;
				float y0 = hasPrevious ? (lastLeftY + lastRightY) / 2 : startY;

				float x1 = coords[0];
				float y1 = coords[1];

				float dx = x1 - x0;
				float dy = y1 - y0;
				float len = (float) Math.sqrt(dx * dx + dy * dy);
				if (len == 0) {
					len = 1;
				}
				dx /= len;
				dy /= len;
				float nx = -dy;
				float ny = dx;

				float lx0 = x0 + nx * leftWidth;
				float ly0 = y0 + ny * leftWidth;
				float rx0 = x0 - nx * rightWidth;
				float ry0 = y0 - ny * rightWidth;

				float lx1 = x1 + nx * leftWidth;
				float ly1 = y1 + ny * leftWidth;
				float rx1 = x1 - nx * rightWidth;
				float ry1 = y1 - ny * rightWidth;

				vertices.add(plane.project(new Vector2f(lx0, ly0)));
				vertices.add(plane.project(new Vector2f(rx0, ry0)));
				vertices.add(plane.project(new Vector2f(lx1, ly1)));
				vertices.add(plane.project(new Vector2f(rx1, ry1)));

				int base = vertices.size() - 4;
				indices.add(base);
				indices.add(base + 2);
				indices.add(base + 1);

				indices.add(base + 1);
				indices.add(base + 2);
				indices.add(base + 3);

				lastLeftX = lx1;
				lastLeftY = ly1;
				lastRightX = rx1;
				lastRightY = ry1;
				hasPrevious = true;
				hasPrevEnd = true;

				prevEndLeftX = lx1;
				prevEndLeftY = ly1;
				prevEndRightX = rx1;
				prevEndRightY = ry1;
			}
			}

			it.next();
		}

		Vector3f[] vertexArray = vertices.toArray(Vector3f[]::new);
		int[] indexArray = indices.stream().mapToInt(Integer::intValue).toArray();

		return new GeneratedMeshData(vertexArray, indexArray);
	}

	private static float cubicDerivative(final float p0, final float p1, final float p2, final float p3, final float t) {
		float it = 1f - t;
		return 3f * it * it * (p1 - p0) + 6f * it * t * (p2 - p1) + 3f * t * t * (p3 - p2);
	}

}
