package lu.kbra.plant_game.base.scene.menu.main;

import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;

public final class BezierStripGenerator {

	private BezierStripGenerator() {
	}

	public static Vector3f[] generateTriangleStrip(
			final CubicCurve2D.Float curve,
			final float width,
			final int resolution,
			float factor,
			final GeoPlane plane) {
		factor = clamp(factor, -1f, 1f);

		final float left = width * (1f - factor) * 0.5f;
		final float right = width * (1f + factor) * 0.5f;

		final List<Vector3f> vertices = new ArrayList<>((resolution + 1) * 2);

		float lastX = 0;
		float lastY = 0;

		for (int i = 0; i <= resolution; i++) {
			final float t = i / (float) resolution;

			final float x = cubic(curve.x1, curve.ctrlx1, curve.ctrlx2, curve.x2, t);
			final float y = cubic(curve.y1, curve.ctrly1, curve.ctrly2, curve.y2, t);

			float dx;
			float dy;

			if (i == 0) {
				final float t2 = 1f / resolution;
				final float x2 = cubic(curve.x1, curve.ctrlx1, curve.ctrlx2, curve.x2, t2);
				final float y2 = cubic(curve.y1, curve.ctrly1, curve.ctrly2, curve.y2, t2);
				dx = x2 - x;
				dy = y2 - y;
			} else {
				dx = x - lastX;
				dy = y - lastY;
			}

			float len = (float) Math.sqrt(dx * dx + dy * dy);
			if (len == 0) {
				len = 1;
			}

			dx /= len;
			dy /= len;

			final float nx = -dy;
			final float ny = dx;

			final float lx = x + nx * left;
			final float ly = y + ny * left;

			final float rx = x - nx * right;
			final float ry = y - ny * right;

			vertices.add(plane.project(new Vector2f(lx, ly)));
			vertices.add(plane.project(new Vector2f(rx, ry)));

			lastX = x;
			lastY = y;
		}

		return vertices.toArray(Vector3f[]::new);
	}

	private static float cubic(final float p0, final float p1, final float p2, final float p3, final float t) {
		final float it = 1f - t;
		return it * it * it * p0 + 3f * it * it * t * p1 + 3f * it * t * t * p2 + t * t * t * p3;
	}

	private static float clamp(final float v, final float min, final float max) {
		return Math.max(min, Math.min(max, v));
	}

}
