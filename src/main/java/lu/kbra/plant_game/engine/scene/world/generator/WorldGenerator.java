package lu.kbra.plant_game.engine.scene.world.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.joml.SimplexNoise;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UByteAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3iAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;

public class WorldGenerator {

	private int width = 10, length = 15, maxHeight = 5;

	private final List<SquareFace> faces = new ArrayList<>();
	private Vector3f[] verts;
	private int[] indices;
	private Vector3f[] normals;
	private byte[] materialIds;
	private Vector3i[] objectIds;

	private int meshId;
	protected TerrainMaterialType[][] materialType;
	protected Integer[][] noiseCompute;

	public WorldGenerator(final int width, final int length, final int maxHeight) {
		this.width = width;
		this.length = length;
		this.maxHeight = maxHeight;

		this.noiseCompute = new Integer[width][length];
	}

	protected WorldGenerator() {
	}

	protected void setSize(final int width, final int length, final int maxHeight) {
		this.width = width;
		this.length = length;
		this.maxHeight = maxHeight;

		this.noiseCompute = new Integer[width][length];
	}

	public void compute() {
		this.generateFaces();
		this.generateVerts();
	}

	public TerrainMesh generateMesh(final CacheManager cache) {
		final TerrainMesh mesh = new TerrainMesh("terrain-" + this.width + "x" + this.length + "@" + System.identityHashCode(this),
				this.meshId, this.width, this.length, this.maxHeight, this.noiseCompute, this.materialType,
				new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, 1, this.verts),
				new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, 1, this.indices, BufferType.ELEMENT_ARRAY),
				new Vec3fAttribArray(Mesh.ATTRIB_NORMALS_NAME, Mesh.ATTRIB_NORMALS_ID, 1, this.normals),
				new UByteAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_NAME, GameObject.MESH_ATTRIB_MATERIAL_ID_ID, 1, this.materialIds),
				new Vec3iAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_NAME, GameObject.MESH_ATTRIB_OBJECT_ID_ID, 1, this.objectIds));
		cache.addMesh(mesh);
		return mesh;
	}

	protected void generateFaces() {
		this.materialType = new TerrainMaterialType[this.width][this.length];

		for (int x = 0; x < this.width; x++) {
			for (int z = 0; z < this.length; z++) {
				final int cellHeight = this.getCellHeight(x, z);

				this.faces
						.add(new SquareFace(new Vector2i(x, z), new Vector3f(x, cellHeight, z), new Vector3f(x + 1, cellHeight, z + 1),
								GameEngine.Y_POS, cellHeight < 0 ? TerrainMaterialType.STONE : TerrainMaterialType.GRASS));

				this.materialType[x][z] = cellHeight < 0 ? TerrainMaterialType.WATER : TerrainMaterialType.GRASS;

				final int cellHeightXPos = this.getCellHeight(x + 1, z);
				if (cellHeight > cellHeightXPos) {
					for (int y = cellHeightXPos; y < cellHeight; y++) {
						this.faces
								.add(new SquareFace(null, new Vector3f(x + 1, y, z), new Vector3f(x + 1, y + 1, z + 1), GameEngine.X_POS,
										TerrainMaterialType.DIRT));
					}
				}

				final int cellHeightZPos = this.getCellHeight(x, z + 1);
				if (cellHeight > cellHeightZPos) {
					for (int y = cellHeightZPos; y < cellHeight; y++) {
						this.faces
								.add(new SquareFace(null, new Vector3f(x, y, z + 1), new Vector3f(x + 1, y + 1, z + 1), GameEngine.Z_POS,
										TerrainMaterialType.DIRT));
					}
				}

				final int cellHeightXNeg = this.getCellHeight(x - 1, z);
				if (cellHeight > cellHeightXNeg) {
					for (int y = cellHeightXNeg; y < cellHeight; y++) {
						this.faces
								.add(new SquareFace(null, new Vector3f(x, y, z), new Vector3f(x, y + 1, z + 1), GameEngine.X_NEG,
										TerrainMaterialType.DIRT));
					}
				}

				final int cellHeightZNeg = this.getCellHeight(x, z - 1);
				if (cellHeight > cellHeightZNeg) {
					for (int y = cellHeightZNeg; y < cellHeight; y++) {
						this.faces
								.add(new SquareFace(null, new Vector3f(x, y, z), new Vector3f(x + 1, y + 1, z), GameEngine.Z_NEG,
										TerrainMaterialType.DIRT));
					}
				}
			}
		}
	}

	protected void generateVerts() {
		this.verts = new Vector3f[this.faces.size() * 4];
		this.indices = new int[this.faces.size() * 6];
		this.normals = new Vector3f[this.faces.size() * 4];
		this.materialIds = new byte[this.faces.size() * 4];
		this.objectIds = new Vector3i[this.faces.size() * 4];
		this.meshId = new Random().nextInt(0, 255);

		int faceCount = 0;
		for (final SquareFace face : this.faces) {
			final Vector3f[] corners = face.corners();
			System.arraycopy(corners, 0, this.verts, faceCount * 4, 4);
			System.arraycopy(face.indices(faceCount * 4), 0, this.indices, faceCount * 6, 6);
			Arrays.fill(this.normals, faceCount * 4, faceCount * 4 + 4, face.normal);
			Arrays.fill(this.materialIds, faceCount * 4, faceCount * 4 + 4, face.material().getId());
			if (face.cellPosition != null) {
				Arrays
						.fill(this.objectIds,
								faceCount * 4,
								faceCount * 4 + 4,
								new Vector3i(this.meshId, face.cellPosition.x(), face.cellPosition.y()));
			} else {
				Arrays.fill(this.objectIds, faceCount * 4, faceCount * 4 + 4, new Vector3i(this.meshId, 0, 0));
			}
			faceCount++;
		}
	}

	public int getCellHeight(final int x, final int z) {
		if (x >= this.width || z >= this.length || x < 0 || z < 0) {
			return -1;
		}

		if (this.noiseCompute[x][z] == null) {
			return (this.noiseCompute[x][z] = this.genNoise(x, z));
		}

		return this.noiseCompute[x][z];
	}

	protected Integer genNoise(final int x, final int z) {
		return Math.round(SimplexNoise.noise(x + 0.5f, z + 0.5f));
	}

	public enum TerrainMaterialType {
		GRASS((byte) 1), DIRT((byte) 2), STONE((byte) 3), WATER((byte) 4);

		private final byte id;

		private TerrainMaterialType(final byte id) {
			this.id = id;
		}

		public byte getId() {
			return this.id;
		}
	}

	public record SquareFace(Vector2ic cellPosition, Vector3fc corner1, Vector3fc corner2, Vector3fc normal, TerrainMaterialType material) {
		public Vector3f[] corners() {
			// Extract coordinates
			final float x1 = this.corner1.x(), y1 = this.corner1.y(), z1 = this.corner1.z();
			final float x2 = this.corner2.x(), y2 = this.corner2.y(), z2 = this.corner2.z();

			final Vector3f[] verts = new Vector3f[4];

			// Determine ordering based on dominant axis of the normal
			if (Math.abs(this.normal.x()) > Math.abs(this.normal.y()) && Math.abs(this.normal.x()) > Math.abs(this.normal.z())) {
				// X is dominant
				if (this.normal.x() > 0) {
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
			} else if (Math.abs(this.normal.y()) > Math.abs(this.normal.z())) {
				// Y is dominant
				if (this.normal.y() > 0) {
					verts[3] = new Vector3f(x1, y2, z1);
					verts[2] = new Vector3f(x2, y2, z1);
					verts[1] = new Vector3f(x2, y2, z2);
					verts[0] = new Vector3f(x1, y2, z2);
				} else {
					verts[0] = new Vector3f(x1, y1, z1);
					verts[1] = new Vector3f(x1, y1, z2);
					verts[2] = new Vector3f(x2, y1, z2);
					verts[3] = new Vector3f(x2, y1, z1);
				}
			} else // Z is dominant
			if (this.normal.z() > 0) {
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

			return verts;
		}

		public int[] indices(final int base) {
			return new int[] { base, base + 1, base + 2, base, base + 2, base + 3 };
		}
	}

}
