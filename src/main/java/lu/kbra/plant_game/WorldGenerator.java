package lu.kbra.plant_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.SimplexNoise;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec2fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;

public class WorldGenerator {

	public record SquareFace(Vector3f corner1, Vector3f corner2) {
		public Vector3f[] corners() {
			float x1 = corner1.x, y1 = corner1.y, z1 = corner1.z;
			float x2 = corner2.x, y2 = corner2.y, z2 = corner2.z;

			// Determine which axis is constant (face normal)
			boolean varyX = x1 != x2;
			boolean varyY = y1 != y2;
			boolean varyZ = z1 != z2;

			Vector3f c0, c1, c2, c3;

			if (!varyX) { // X is constant, face in YZ plane
				c0 = new Vector3f(x1, y1, z1);
				c1 = new Vector3f(x1, y2, z1);
				c2 = new Vector3f(x1, y2, z2);
				c3 = new Vector3f(x1, y1, z2);
			} else if (!varyY) { // Y is constant, face in XZ plane
				c0 = new Vector3f(x1, y1, z1);
				c1 = new Vector3f(x2, y1, z1);
				c2 = new Vector3f(x2, y1, z2);
				c3 = new Vector3f(x1, y1, z2);
			} else if (!varyZ) { // Z is constant, face in XY plane
				c0 = new Vector3f(x1, y1, z1);
				c1 = new Vector3f(x2, y1, z1);
				c2 = new Vector3f(x2, y2, z1);
				c3 = new Vector3f(x1, y2, z1);
			} else {
				// General case: all axes vary, pick consistent order along XYZ
				c0 = new Vector3f(x1, y1, z1);
				c1 = new Vector3f(x2, y1, z1);
				c2 = new Vector3f(x2, y2, z2);
				c3 = new Vector3f(x1, y2, z2);
			}

			return new Vector3f[] { c0, c1, c2, c3 };
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

	private Integer[][] noiseCompute = new Integer[width][length];

	public void compute() {
		generateFaces();
		generateVerts();
	}

	public Mesh generateMesh(CacheManager cache) {
		final Mesh mesh = new Mesh("terrain", null, new Vec3fAttribArray("pos", 0, 1, verts),
				new UIntAttribArray("ind", -1, 1, indices, BufferType.ELEMENT_ARRAY),
				new Vec3fAttribArray("normal", 1, 1, new Vector3f[indices.length]),
				new Vec2fAttribArray("uv", 2, 1, new Vector2f[indices.length]));
		cache.addMesh(mesh);
		return mesh;
	}

	protected void generateFaces() {
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				final int cellHeight = getCellHeight(x, z);

				faces.add(new SquareFace(new Vector3f(x, cellHeight, z), new Vector3f(x + 1, cellHeight, z + 1)));

				final int cellHeightX = getCellHeight(x + 1, z);
				if (cellHeight != cellHeightX) {
					final int minHeight = Math.min(cellHeight, cellHeightX);
					final int maxHeight = Math.max(cellHeight, cellHeightX);
					for (int y = minHeight; y < maxHeight; y++) {
						System.err.println(new SquareFace(new Vector3f(x + 1, y, z), new Vector3f(x + 1, y + 1, z + 1)));
						System.err
								.println(Arrays
										.toString(new SquareFace(new Vector3f(x + 1, y, z), new Vector3f(x + 1, y + 1, z + 1)).corners()));
						faces.add(new SquareFace(new Vector3f(x + 1, y, z), new Vector3f(x + 1, y + 1, z + 1)));
					}
				}

				final int cellHeightZ = getCellHeight(x, z + 1);
				if (cellHeight != cellHeightZ) {
					final int minHeight = Math.min(cellHeight, cellHeightZ);
					final int maxHeight = Math.max(cellHeight, cellHeightZ);
					for (int y = minHeight; y < maxHeight; y++) {
						faces.add(new SquareFace(new Vector3f(x, y, z + 1), new Vector3f(x + 1, y + 1, z + 1)));
					}
				}
			}
		}
	}

	protected void generateVerts() {
		verts = new Vector3f[faces.size() * 4];
		indices = new int[faces.size() * 6];

		int faceCount = 0;
		for (SquareFace face : faces) {
			System.arraycopy(face.corners(), 0, verts, faceCount * 4, 4);
			System.arraycopy(face.indices(faceCount * 4), 0, indices, faceCount * 6, 6);
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
