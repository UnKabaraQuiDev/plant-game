package lu.kbra.plant_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.SimplexNoise;
import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;

public class WorldGenerator {

	public record SquareFace(Vector3f corner1, Vector3f corner2, Vector3f normal) {
		public Vector3f[] corners() {
			// Extract coordinates
			final float x1 = corner1.x, y1 = corner1.y, z1 = corner1.z;
			final float x2 = corner2.x, y2 = corner2.y, z2 = corner2.z;

			Vector3f[] verts = new Vector3f[4];

			// Determine ordering based on dominant axis of the normal
			if (Math.abs(normal.x) > Math.abs(normal.y) && Math.abs(normal.x) > Math.abs(normal.z)) {
				// X is dominant
				if (normal.x > 0) {
					verts[0] = new Vector3f(x2, y1, z1);
					verts[1] = new Vector3f(x2, y2, z1);
					verts[2] = new Vector3f(x2, y2, z2);
					verts[3] = new Vector3f(x2, y1, z2);
				} else {
					verts[0] = new Vector3f(x1, y1, z1);
					verts[1] = new Vector3f(x1, y1, z2);
					verts[2] = new Vector3f(x1, y2, z2);
					verts[3] = new Vector3f(x1, y2, z1);
				}
			} else if (Math.abs(normal.y) > Math.abs(normal.z)) {
				// Y is dominant
				if (normal.y > 0) {
					verts[0] = new Vector3f(x1, y2, z1);
					verts[1] = new Vector3f(x2, y2, z1);
					verts[2] = new Vector3f(x2, y2, z2);
					verts[3] = new Vector3f(x1, y2, z2);
				} else {
					verts[0] = new Vector3f(x1, y1, z1);
					verts[1] = new Vector3f(x1, y1, z2);
					verts[2] = new Vector3f(x2, y1, z2);
					verts[3] = new Vector3f(x2, y1, z1);
				}
			} else {
				// Z is dominant
				if (normal.z > 0) {
					verts[0] = new Vector3f(x1, y1, z2);
					verts[1] = new Vector3f(x2, y1, z2);
					verts[2] = new Vector3f(x2, y2, z2);
					verts[3] = new Vector3f(x1, y2, z2);
				} else {
					verts[0] = new Vector3f(x1, y1, z1);
					verts[1] = new Vector3f(x1, y2, z1);
					verts[2] = new Vector3f(x2, y2, z1);
					verts[3] = new Vector3f(x2, y1, z1);
				}
			}

			return verts;
		}

		public int[] indices(int base) {
			// base = starting index of these 4 vertices in your vertex array
			// Return two triangles (quad split)
			return new int[] { base, base + 1, base + 2, base, base + 2, base + 3 };
		}
	}

	private int width = 10, length = 15;

	private List<SquareFace> faces = new ArrayList<>();
	private Vector3f[] verts;
	private int[] indices;
	private Vector3f[] normals;

	private Integer[][] noiseCompute = new Integer[width][length];

	public void compute() {
		generateFaces();
		generateVerts();
	}

	public Mesh generateMesh(CacheManager cache) {
		final Mesh mesh = new Mesh("terrain", null, new Vec3fAttribArray("pos", 0, 1, verts),
				new UIntAttribArray("ind", -1, 1, indices, BufferType.ELEMENT_ARRAY), new Vec3fAttribArray("normal", 1, 1, normals));
		cache.addMesh(mesh);
		return mesh;
	}

	protected void generateFaces() {
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				final int cellHeight = getCellHeight(x, z);

				faces.add(new SquareFace(new Vector3f(x, cellHeight, z), new Vector3f(x + 1, cellHeight, z + 1), GameEngine.Y_POS));

				final int cellHeightXPos = getCellHeight(x + 1, z);
				if (cellHeight > cellHeightXPos) {
					for (int y = cellHeightXPos; y < cellHeight; y++) {
						faces.add(new SquareFace(new Vector3f(x + 1, y, z), new Vector3f(x + 1, y + 1, z + 1), GameEngine.X_POS));
					}
				}

				final int cellHeightZPos = getCellHeight(x, z + 1);
				if (cellHeight > cellHeightZPos) {
					for (int y = cellHeightZPos; y < cellHeight; y++) {
						faces.add(new SquareFace(new Vector3f(x, y, z + 1), new Vector3f(x + 1, y + 1, z + 1), GameEngine.Z_POS));
					}
				}

				final int cellHeightXNeg = getCellHeight(x - 1, z);
				if (cellHeight > cellHeightXNeg) {
					for (int y = cellHeightXNeg; y < cellHeight; y++) {
						faces.add(new SquareFace(new Vector3f(x, y, z), new Vector3f(x, y + 1, z + 1), GameEngine.X_NEG));
					}
				}

				final int cellHeightZNeg = getCellHeight(x, z - 1);
				if (cellHeight > cellHeightZNeg) {
					for (int y = cellHeightZNeg; y < cellHeight; y++) {
						faces.add(new SquareFace(new Vector3f(x, y, z), new Vector3f(x + 1, y + 1, z), GameEngine.Z_NEG));
					}
				}
			}
		}
	}

	protected void generateVerts() {
		verts = new Vector3f[faces.size() * 4];
		indices = new int[faces.size() * 6];
		normals = new Vector3f[faces.size() * 4];

		int faceCount = 0;
		for (SquareFace face : faces) {
			final Vector3f[] corners = face.corners();
			System.arraycopy(corners, 0, verts, faceCount * 4, 4);
			System.arraycopy(face.indices(faceCount * 4), 0, indices, faceCount * 6, 6);
			Arrays.fill(normals, faceCount * 4, faceCount * 4 + 4, face.normal);
			faceCount++;
		}
	}

	protected int getCellHeight(int x, int z) {
		if (x >= width || z >= length || x < 0 || z < 0)
			return -1;

		if (noiseCompute[x][z] == null)
			return (noiseCompute[x][z] = Math.round(SimplexNoise.noise(x + 0.5f, z + 0.5f)));

		return noiseCompute[x][z];
	}

}
