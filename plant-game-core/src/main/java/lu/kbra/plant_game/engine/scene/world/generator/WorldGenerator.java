package lu.kbra.plant_game.engine.scene.world.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.SimplexNoise;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainEdgeMesh;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainMesh;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UByteAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LineLoadedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;

public abstract class WorldGenerator {

	private int width = 10, length = 15, maxHeight = 5;
	private final float cellSize = 1f;

	private final List<SquareFace> faces = new ArrayList<>();
	private final List<Edge> edges = new ArrayList<>();
	private SquareFace[][] topFaces;
	private Vector3f[] verts;
	private int[] indices;
	private Vector3f[] normals;
	private byte[] materialIds;
//	private Vector3i[] objectIds;
	private Vector3f[] edgeVertices;
	private int[] edgeIndices;
	private byte[] edgeMaterialIds;
	private int[][] faceIndices;

	private int meshId;
	protected ColorMaterial[][] materialType;
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

	public void compute(final IntPointer progress) {
		progress.setValue(0);
		this.generateFaces(progress);
		progress.setValue(100);
		this.generateVerts(progress);
		progress.setValue(200);
		this.generateEdges(progress);
		progress.setValue(300);
	}

	public TerrainMesh generateMesh(final IntPointer progress, final CacheManager cache) {
		final TerrainMesh mesh = new TerrainMesh("terrain-" + this.width + "x" + this.length + "@" + System.identityHashCode(this),
				this.meshId,
				this.width,
				this.length,
				this.maxHeight,
				this.noiseCompute,
				this.materialType,
				this.faceIndices,
				new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, this.verts),
				new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, this.indices, BufferType.ELEMENT_ARRAY),
				new Vec3fAttribArray(Mesh.ATTRIB_NORMALS_NAME, Mesh.ATTRIB_NORMALS_ID, this.normals),
				new UByteAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_NAME,
						GameObject.MESH_ATTRIB_MATERIAL_ID_ID,
						this.materialIds,
						BufferType.ARRAY,
						false));
//				new Vec3iAttribArray(GameObject.MESH_ATTRIB_OBJECT_ID_NAME, GameObject.MESH_ATTRIB_OBJECT_ID_ID, this.objectIds));

		cache.addMesh(mesh);

		progress.add(100);

		return mesh;
	}

	public TerrainEdgeMesh generateEdgeMesh(final IntPointer progress, final CacheManager cache) {
		final Map<Vector3f, Integer> indexMap = new HashMap<>();
		final List<Vector3f> verts = new ArrayList<>();
		final List<Integer> inds = new ArrayList<>();
		final List<Byte> mats = new ArrayList<>();

		final int prevValue = progress.getValue();

		final int totalEdgeCount = this.edges.size();
		int edgeCount = 0;
		for (final Edge e : this.edges) {
			final Vector3f a = e.p1();
			final Vector3f b = e.p2();

			Integer ia = indexMap.get(a);
			if (ia == null) {
				ia = verts.size();
				verts.add(a);
				mats.add((byte) e.material.getId());
				indexMap.put(a, ia);
			}

			Integer ib = indexMap.get(b);
			if (ib == null) {
				ib = verts.size();
				verts.add(b);
				mats.add((byte) e.material.darker().getId());
				indexMap.put(b, ib);
			}

			inds.add(ia);
			inds.add(ib);

			edgeCount++;
			progress.setValue(prevValue + 50 * edgeCount / totalEdgeCount);
		}

		progress.setValue(prevValue + 50);

		this.edgeVertices = verts.toArray(new Vector3f[0]);
		this.edgeMaterialIds = PCUtils.byteListToPrimitive(mats);
		this.edgeIndices = new int[inds.size()];
		for (int i = 0; i < inds.size(); i++) {
			this.edgeIndices[i] = inds.get(i);
		}

		progress.setValue(prevValue + 75);

		final TerrainEdgeMesh mesh = new TerrainEdgeMesh(
				"terrain-edges-" + this.width + "x" + this.length + "@" + System.identityHashCode(this),
				new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, this.edgeVertices),
				new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, this.edgeIndices, BufferType.ELEMENT_ARRAY),
				new UByteAttribArray(GameObject.MESH_ATTRIB_MATERIAL_ID_NAME, GameObject.MESH_ATTRIB_MATERIAL_ID_ID, this.edgeMaterialIds));

		cache.addMesh(mesh);

		progress.setValue(prevValue + 100);

		return mesh;
	}

	public Mesh generateHighlightMesh(final IntPointer progress, final CacheManager cache) {
		final float height = 0.1f;
		final float dist = 0.6f;
		final float far = 3 * dist;

		final Vector3f[] edgeVertices = {
				new Vector3f(-dist * this.cellSize, height, -far * this.cellSize), // 0
				new Vector3f(-dist * this.cellSize, height, far * this.cellSize), // 1

				new Vector3f(dist * this.cellSize, height, -far * this.cellSize), // 2
				new Vector3f(dist * this.cellSize, height, far * this.cellSize), // 3

				new Vector3f(-far * this.cellSize, height, -dist * this.cellSize), // 4
				new Vector3f(far * this.cellSize, height, -dist * this.cellSize), // 5

				new Vector3f(-far * this.cellSize, height, dist * this.cellSize), // 6
				new Vector3f(far * this.cellSize, height, dist * this.cellSize), // 7
		};

		final int[] edgeIndices = { 0, 1, 2, 3, 4, 5, 6, 7 };

		final Mesh mesh = new LineLoadedMesh("terrain-highlight-3x3@" + System.identityHashCode(this),
				null,
				1f,
				new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, edgeVertices),
				new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, edgeIndices, BufferType.ELEMENT_ARRAY));

		cache.addMesh(mesh);

		progress.add(100);
		return mesh;
	}

	protected abstract ColorMaterial getCellMaterial(int x, int z, int cellHeight);

	protected void generateFaces(final IntPointer progress) {
		this.materialType = new ColorMaterial[this.width][this.length];
		this.topFaces = new SquareFace[this.width][this.length];

		for (int x = 0; x < this.width; x++) {
			for (int z = 0; z < this.length; z++) {
				final int cellHeight = this.getCellHeight(x, z);

				final ColorMaterial material = this.materialType[x][z] = this.getCellMaterial(x, z, cellHeight);

				final SquareFace face = new SquareFace(new Vector2i(x, z),
						new Vector3f(x, cellHeight, z),
						new Vector3f(x + 1, cellHeight, z + 1),
						GameEngine.Y_POS,
						this.materialType[x][z]);

				this.faces.add(face);

				this.topFaces[x][z] = face;

				final int cellHeightXPos = this.getCellHeight(x + 1, z);
				if (cellHeight > cellHeightXPos) {
					for (int y = cellHeightXPos; y < cellHeight; y++) {
						this.faces.add(new SquareFace(null,
								new Vector3f(x + 1, y, z),
								new Vector3f(x + 1, y + 1, z + 1),
								GameEngine.X_POS,
								material));
					}
				}

				final int cellHeightZPos = this.getCellHeight(x, z + 1);
				if (cellHeight > cellHeightZPos) {
					for (int y = cellHeightZPos; y < cellHeight; y++) {
						this.faces.add(new SquareFace(null,
								new Vector3f(x, y, z + 1),
								new Vector3f(x + 1, y + 1, z + 1),
								GameEngine.Z_POS,
								material));
					}
				}

				final int cellHeightXNeg = this.getCellHeight(x - 1, z);
				if (cellHeight > cellHeightXNeg) {
					for (int y = cellHeightXNeg; y < cellHeight; y++) {
						this.faces.add(
								new SquareFace(null, new Vector3f(x, y, z), new Vector3f(x, y + 1, z + 1), GameEngine.X_NEG, material));
					}
				}

				final int cellHeightZNeg = this.getCellHeight(x, z - 1);
				if (cellHeight > cellHeightZNeg) {
					for (int y = cellHeightZNeg; y < cellHeight; y++) {
						this.faces.add(
								new SquareFace(null, new Vector3f(x, y, z), new Vector3f(x + 1, y + 1, z), GameEngine.Z_NEG, material));
					}
				}

				progress.setValue((x * this.width + z) * 100 / (this.width * this.length));
			}
		}
	}

	protected void generateEdges(final IntPointer progress) {
		for (int x = 0; x < this.width; x++) {
			for (int z = 0; z < this.length; z++) {
				final int h = this.getCellHeight(x, z);
				final ColorMaterial mat = this.getTopFaceMaterial(x, z);

				// Horizontal edge on X axis
				if (x + 1 < this.width) {
					final int hx = this.getCellHeight(x + 1, z);
					if (h != hx) {
						final int top = Math.max(h, hx);
						this.edges.add(new Edge(new Vector3f(x + 1, top, z), new Vector3f(x + 1, top, z + 1), mat));
					}
				}

				// Horizontal edge on Z axis
				if (z + 1 < this.length) {
					final int hz = this.getCellHeight(x, z + 1);
					if (h != hz) {
						final int top = Math.max(h, hz);
						this.edges.add(new Edge(new Vector3f(x, top, z + 1), new Vector3f(x + 1, top, z + 1), mat));
					}
				}

				progress.setValue(200 + 100 * (x * this.width + z) / (this.width * this.length));
			}
		}
	}

	protected void generateVerts(final IntPointer progress) {
		this.verts = new Vector3f[this.faces.size() * 4];
		this.indices = new int[this.faces.size() * 6];
		this.normals = new Vector3f[this.faces.size() * 4];
		this.materialIds = new byte[this.faces.size() * 4];
		this.meshId = PCUtils.randomIntRange(0, 255);
		this.faceIndices = new int[this.width][this.length];

		final int totalFaceCount = this.faces.size();
		int faceCount = 0;
		for (final SquareFace face : this.faces) {
			final Vector3f[] corners = face.corners();
			System.arraycopy(corners, 0, this.verts, faceCount * 4, 4);
			System.arraycopy(face.indices(faceCount * 4), 0, this.indices, faceCount * 6, 6);
			Arrays.fill(this.normals, faceCount * 4, faceCount * 4 + 4, face.normal);
			Arrays.fill(this.materialIds, faceCount * 4, faceCount * 4 + 4, (byte) face.material().getId());

			if (face.cellPosition() != null) {
				final Vector2ic cell = face.cellPosition();
				this.faceIndices[cell.x()][cell.y()] = faceCount * 4;
			}

			faceCount++;

			progress.setValue(100 + 100 * faceCount / totalFaceCount);
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

	public ColorMaterial getTopFaceMaterial(final int x, final int z) {
		if (x >= this.width || z >= this.length || x < 0 || z < 0) {
			return null;
		}

		return this.topFaces[x][z].material;
	}

	protected Integer genNoise(final int x, final int z) {
		return Math.round(SimplexNoise.noise(x + 0.5f, z + 0.5f));
	}

	public record Edge(Vector3f p1, Vector3f p2, ColorMaterial material) {

	}

	public record SquareFace(Vector2ic cellPosition, Vector3fc corner1, Vector3fc corner2, Vector3fc normal, ColorMaterial material) {
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
