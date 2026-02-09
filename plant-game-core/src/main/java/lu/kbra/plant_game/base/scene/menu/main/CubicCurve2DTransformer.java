package lu.kbra.plant_game.base.scene.menu.main;

import static lu.kbra.pclib.PCUtils.clamp;

import java.awt.geom.CubicCurve2D;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;

public final class CubicCurve2DTransformer {

	public record GeneratedMeshData(Vector3f[] vertices, int[] indices) {
	}

	private CubicCurve2DTransformer() {
	}

	public static GeneratedMeshData generateTriangleMesh(
			final CubicCurve2D.Float curve,
			final float width,
			final int resolution,
			float factor,
			final GeoPlane plane) {

		factor = clamp(factor, -1f, 1f);

		final float left = width * (1f - factor) * 0.5f;
		final float right = width * (1f + factor) * 0.5f;

		final int vertexCount = (resolution + 1) * 2;
		final Vector3f[] vertices = new Vector3f[vertexCount];

		float lastX = 0;
		float lastY = 0;

		int v = 0;

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

			vertices[v++] = plane.project(new Vector2f(lx, ly));
			vertices[v++] = plane.project(new Vector2f(rx, ry));

			lastX = x;
			lastY = y;
		}

		final int quadCount = resolution;
		final int indexCount = quadCount * 6;
		final int[] indices = new int[indexCount];

		int idx = 0;
		for (int i = 0; i < resolution; i++) {
			final int base = i * 2;

			final int l0 = base;
			final int r0 = base + 1;
			final int l1 = base + 2;
			final int r1 = base + 3;

			// triangle 1
			indices[idx++] = l0;
			indices[idx++] = l1;
			indices[idx++] = r0;

			// triangle 2
			indices[idx++] = r0;
			indices[idx++] = l1;
			indices[idx++] = r1;
		}

		return new GeneratedMeshData(vertices, indices);
	}

	public static float cubic(final float p0, final float p1, final float p2, final float p3, final float t) {
		final float it = 1f - t;
		return it * it * it * p0 + 3f * it * it * t * p1 + 3f * it * t * t * p2 + t * t * t * p3;
	}

}
