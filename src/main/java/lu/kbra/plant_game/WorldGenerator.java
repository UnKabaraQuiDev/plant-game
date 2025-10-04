package lu.kbra.plant_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.SimplexNoise;
import org.joml.Vector3f;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.GameObject;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UByteAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3iAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;

public class WorldGenerator {

	private int width = 10, length = 15;

	private List<SquareFace> faces = new ArrayList<>();
	private Vector3f[] verts;
	private int[] indices;
	private Vector3f[] normals;
	private byte[] materialIds;
	private Vector3i[] objectIds;

	private Integer[][] noiseCompute;

	public WorldGenerator(int width, int length) {
		this.width = width;
		this.length = length;
		noiseCompute = new Integer[width][length];
	}

	public void compute() {
		generateFaces();
		generateVerts();
	}

	public Mesh generateMesh(CacheManager cache) {
		final Mesh mesh = new Mesh("terrain", null, new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, 1, verts),
				new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, 1, indices, BufferType.ELEMENT_ARRAY),
				new Vec3fAttribArray(Mesh.ATTRIB_NORMALS_NAME, Mesh.ATTRIB_NORMALS_ID, 1, normals),
				new UByteAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_NAME, GameObject.MESH_ATTRIB_MATERIAL_ID_ID, 1, materialIds),
				new Vec3iAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_NAME, GameObject.MESH_ATTRIB_OBJECT_ID_ID, 1, objectIds));
		cache.addMesh(mesh);
		return mesh;
	}

	protected void generateFaces() {
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				final int cellHeight = getCellHeight(x, z);

				faces
						.add(new SquareFace(new Vector3f(x, cellHeight, z), new Vector3f(x + 1, cellHeight, z + 1), GameEngine.Y_POS,
								cellHeight < 0 ? MaterialType.STONE : MaterialType.GRASS));

				final int cellHeightXPos = getCellHeight(x + 1, z);
				if (cellHeight > cellHeightXPos) {
					for (int y = cellHeightXPos; y < cellHeight; y++) {
						faces
								.add(new SquareFace(new Vector3f(x + 1, y, z), new Vector3f(x + 1, y + 1, z + 1), GameEngine.X_POS,
										MaterialType.DIRT));
					}
				}

				final int cellHeightZPos = getCellHeight(x, z + 1);
				if (cellHeight > cellHeightZPos) {
					for (int y = cellHeightZPos; y < cellHeight; y++) {
						faces
								.add(new SquareFace(new Vector3f(x, y, z + 1), new Vector3f(x + 1, y + 1, z + 1), GameEngine.Z_POS,
										MaterialType.DIRT));
					}
				}

				final int cellHeightXNeg = getCellHeight(x - 1, z);
				if (cellHeight > cellHeightXNeg) {
					for (int y = cellHeightXNeg; y < cellHeight; y++) {
						faces
								.add(new SquareFace(new Vector3f(x, y, z), new Vector3f(x, y + 1, z + 1), GameEngine.X_NEG,
										MaterialType.DIRT));
					}
				}

				final int cellHeightZNeg = getCellHeight(x, z - 1);
				if (cellHeight > cellHeightZNeg) {
					for (int y = cellHeightZNeg; y < cellHeight; y++) {
						faces
								.add(new SquareFace(new Vector3f(x, y, z), new Vector3f(x + 1, y + 1, z), GameEngine.Z_NEG,
										MaterialType.DIRT));
					}
				}
			}
		}
	}

	protected void generateVerts() {
		verts = new Vector3f[faces.size() * 4];
		indices = new int[faces.size() * 6];
		normals = new Vector3f[faces.size() * 4];
		materialIds = new byte[faces.size() * 4];
		objectIds = new Vector3i[faces.size() * 4];

		int faceCount = 0;
		for (SquareFace face : faces) {
			final Vector3f[] corners = face.corners();
			System.arraycopy(corners, 0, verts, faceCount * 4, 4);
			System.arraycopy(face.indices(faceCount * 4), 0, indices, faceCount * 6, 6);
			Arrays.fill(normals, faceCount * 4, faceCount * 4 + 4, face.normal);
			Arrays.fill(materialIds, faceCount * 4, faceCount * 4 + 4, face.material().getId());
			Arrays.fill(objectIds, faceCount * 4, faceCount * 4 + 4, new Vector3i(0, 0, 1));
			faceCount++;
		}
	}

	public int getCellHeight(int x, int z) {
		if (x >= width || z >= length || x < 0 || z < 0)
			return -1;

		if (noiseCompute[x][z] == null)
			return (noiseCompute[x][z] = genNoise(x, z));

		return noiseCompute[x][z];
	}

	protected Integer genNoise(int x, int z) {
		return Math.round(SimplexNoise.noise(x + 0.5f, z + 0.5f));
	}

	public enum MaterialType {
		GRASS((byte) 1), DIRT((byte) 2), STONE((byte) 3), WATER((byte) 4);

		private final byte id;

		private MaterialType(byte id) {
			this.id = id;
		}

		public byte getId() {
			return id;
		}
	}

	public record SquareFace(Vector3f corner1, Vector3f corner2, Vector3f normal, MaterialType material) {
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

}
