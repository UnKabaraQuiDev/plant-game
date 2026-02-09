package lu.kbra.plant_game.base.scene.menu.main;

import static lu.kbra.pclib.PCUtils.clamp;
import static lu.kbra.plant_game.base.scene.menu.main.CubicCurve2DTransformer.cubic;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.google.protobuf.ExperimentalApi;

import lu.kbra.plant_game.base.scene.menu.main.CubicCurve2DTransformer.GeneratedMeshData;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;

@ExperimentalApi
public final class Path2DTransformer {

	private Path2DTransformer() {
	}

	public static GeneratedMeshData generateTriangleMesh(
			final Path2D.Float curve,
			final float width,
			final int resolution,
			float factor,
			final GeoPlane plane) {

		factor = clamp(factor, -1f, 1f);
		final float leftWidth = width * (1f - factor) * 0.5f;
		final float rightWidth = width * (1f + factor) * 0.5f;

		final List<Vector3f> vertices = new ArrayList<>();
		final List<Integer> indices = new ArrayList<>();

		final PathIterator pi = curve.getPathIterator(null);

		float x0 = 0;
		float y0 = 0;

		while (!pi.isDone()) {
			final float[] coords = new float[6];
			final int segType = pi.currentSegment(coords);

			switch (segType) {
			case PathIterator.SEG_MOVETO -> {
				x0 = coords[0];
				y0 = coords[1];
				// skip
			}
			case PathIterator.SEG_CUBICTO -> {
				final float cx1 = coords[0];
				final float cy1 = coords[1];
				final float cx2 = coords[2];
				final float cy2 = coords[3];
				final float x1 = coords[4];
				final float y1 = coords[5];

				for (int i = 0; i <= resolution; i++) {
					final float t = i / (float) resolution;

					// position
					final float x = cubic(x0, cx1, cx2, x1, t);
					final float y = cubic(y0, cy1, cy2, y1, t);

					// derivative = tangent
					final float dx = cubicDerivative(x0, cx1, cx2, x1, t);
					final float dy = cubicDerivative(y0, cy1, cy2, y1, t);

					final Vector3f tan = plane.project(new Vector2f(dx, dy).normalize());
					final Vector2f norm = plane.projectToPlane(new Vector3f(tan)
							.rotateAxis((float) Math.PI / 2, plane.getNormal().x(), plane.getNormal().y(), plane.getNormal().z()));

					// normal
					final float nx = norm.x();
					final float ny = norm.y();

					final float lx = x + nx * leftWidth;
					final float ly = y + ny * leftWidth;
					final float rx = x - nx * rightWidth;
					final float ry = y - ny * rightWidth;

					vertices.add(plane.project(new Vector2f(lx, ly)));
					vertices.add(plane.project(new Vector2f(rx, ry)));

					if (i > 0) {
						final int base = vertices.size() - 4;
						indices.add(base);
						indices.add(base + 1);
						indices.add(base + 2);

						indices.add(base + 1);
						indices.add(base + 3);
						indices.add(base + 2);
					}

				}

				x0 = coords[4];
				y0 = coords[5];
			}
			default -> {
				throw new IllegalArgumentException("Unsupported: " + segType);
			}
			}

			pi.next();
		}

		final Vector3f[] vertexArray = vertices.toArray(Vector3f[]::new);
		final int[] indexArray = indices.stream().mapToInt(Integer::intValue).toArray();

		return new GeneratedMeshData(vertexArray, indexArray);
	}

	private static float cubicDerivative(final float p0, final float p1, final float p2, final float p3, final float t) {
		final float it = 1f - t;
		return 3f * it * it * (p1 - p0) + 6f * it * t * (p2 - p1) + 3f * t * t * (p3 - p2);
	}

}
