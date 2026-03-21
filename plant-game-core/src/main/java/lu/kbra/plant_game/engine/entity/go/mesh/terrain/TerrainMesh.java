package lu.kbra.plant_game.engine.entity.go.mesh.terrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.datastructure.pair.Pair;
import lu.kbra.pclib.datastructure.pair.Pairs;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.go.GenericGameObject;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.attrib.UByteAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;

public class TerrainMesh extends LoadedMesh {

	private final int objectId;
	private final int width;
	private final int length;
	private final int maxHeight;

	private final Integer[][] cellHeight;
	private final ColorMaterial[][] materialType;
	private final int[][] faceIndices;
	private final boolean[][] grown;

	protected List<Pair<Vector2i, ColorMaterial>> updateColorMaterial = Collections.synchronizedList(new ArrayList<>());

	public TerrainMesh(final String name, final int objectId, final int width, final int length, final int maxHeight,
			final Integer[][] cellHeight, final ColorMaterial[][] materialType, final int[][] faceIndices, final Vec3fAttribArray vertices,
			final UIntAttribArray indices, final JavaAttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
		this.objectId = objectId;
		this.width = width;
		this.length = length;
		this.maxHeight = maxHeight;
		this.cellHeight = cellHeight;
		this.materialType = materialType;
		this.faceIndices = faceIndices;
		this.grown = new boolean[width][length];
	}

	public void setGrown(final Vector2i tile, final boolean a) {
		if (a && !this.grown[tile.x()][tile.y()]) {
			this.setColorMaterial(tile, switch (PCUtils.randomIntRange(0, 3)) {
			default -> ColorMaterial.GREEN;
			case 1 -> ColorMaterial.LIGHT_GREEN;
			case 2 -> ColorMaterial.DARK_GREEN;
			});
		} else if (!a && this.grown[tile.x()][tile.y()]) {
			this.setColorMaterial(tile, switch (PCUtils.randomIntRange(0, 4)) {
			default -> ColorMaterial.BROWN;
			case 1 -> ColorMaterial.LIGHT_BROWN;
			case 2 -> ColorMaterial.DARK_BROWN;
			case 3 -> ColorMaterial.DARK_GRAY;
			});
		}
		this.grown[tile.x()][tile.y()] = a;
	}

	public void setColorMaterial(final Vector2i tile, final ColorMaterial cm) {
		if (tile.x < 0 || tile.x > this.width || tile.y < 0 || tile.y > this.length) {
			return;
		}
		synchronized (this.updateColorMaterial) {
			this.updateColorMaterial.add(Pairs.readOnly(tile, cm));
		}
	}

	public void flushColorMaterial() {
		final Map<Integer, byte[]> result = new LinkedHashMap<>();

		synchronized (this.updateColorMaterial) {
			if (this.updateColorMaterial.isEmpty()) {
				return;
			}

			this.updateColorMaterial.sort(Comparator.comparingInt(p -> p.getKey().y() * this.getWidth() + p.getKey().x()));

			final List<Byte> currentValues = new ArrayList<>();
			int currentStart = -1;
			int previousIndex = -1;

			for (Pair<Vector2i, ColorMaterial> pair : this.updateColorMaterial) {

				final Vector2i tile = pair.getKey();
				this.getMaterialTypes()[tile.x()][tile.y()] = pair.getValue();

				final int index = this.getFaceIndex(tile); // (tile.x() + tile.y() * mesh.getWidth());
				final byte value = (byte) pair.getValue().getId();

				if (currentStart == -1) {
					currentStart = index;

					for (int i = 0; i < 4; i++) {
						currentValues.add(value);
					}
				} else if (index == previousIndex + 1) {
					for (int i = 0; i < 4; i++) {
						currentValues.add(value);
					}
				} else {
					result.put(currentStart, PCUtils.byteListToPrimitive(currentValues));
					currentValues.clear();
					currentStart = index;

					for (int i = 0; i < 4; i++) {
						currentValues.add(value);
					}
				}

				previousIndex = index;
			}

			if (!currentValues.isEmpty()) {
				result.put(currentStart, PCUtils.byteListToPrimitive(currentValues));
			}

			this.updateColorMaterial.clear();
		}

		if (result.isEmpty()) {
			return;
		}

		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
			final UByteAttribArray array = (UByteAttribArray) this.getAttribs()
					.stream()
					.filter(c -> c.getIndex() == GenericGameObject.MESH_ATTRIB_MATERIAL_ID_ID)
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("No " + GenericGameObject.MESH_ATTRIB_MATERIAL_ID_NAME + " ("
							+ GenericGameObject.MESH_ATTRIB_MATERIAL_ID_ID + ") found in mesh: " + this.getId()));

			for (Entry<Integer, byte[]> e : result.entrySet()) {
				array.update(e.getKey(), e.getValue());
			}
		});
	}

	public int getCellHeight(final int x, final int z) {
		if (this.isInBounds(x, z)) {
			return this.cellHeight[x][z];
		}

		return Integer.MIN_VALUE;
	}

	public int getCellHeight(final Vector2ic tile) {
		return this.getCellHeight(tile.x(), tile.y());
	}

	public int[][] getFaceIndices() {
		return this.faceIndices;
	}

	public int getFaceIndex(final int x, final int z) {
		if (this.isInBounds(x, z)) {
			return this.faceIndices[x][z];
		}

		return Integer.MIN_VALUE;
	}

	public int getFaceIndex(final Vector2ic tile) {
		return this.getFaceIndex(tile.x(), tile.y());
	}

	public boolean isInBounds(final int x, final int z) {
		return x >= 0 && x < this.width && z >= 0 && z < this.length;
	}

	public boolean isInBounds(final Vector2ic tile) {
		return this.isInBounds(tile.x(), tile.y());
	}

	public Integer[][] getCellHeights() {
		return this.cellHeight;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public int getWidth() {
		return this.width;
	}

	public int getLength() {
		return this.length;
	}

	public ColorMaterial getCellMaterial(final int x, final int z) {
		return this.materialType[x][z];
	}

	public ColorMaterial getCellMaterial(final Vector2ic v) {
		return this.getCellMaterial(v.x(), v.y());
	}

	public ColorMaterial[][] getMaterialTypes() {
		return this.materialType;
	}

	public int getMinHeight() {
		return 0;
	}

	public int getMaxHeight() {
		return this.maxHeight;
	}

	@Override
	public String toString() {
		return "TerrainMesh@" + System.identityHashCode(this) + " [objectId=" + this.objectId + ", width=" + this.width + ", length="
				+ this.length + ", maxHeight=" + this.maxHeight + ", cellHeight=" + Arrays.toString(this.cellHeight) + ", materialType="
				+ Arrays.toString(this.materialType) + ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo + ", material="
				+ this.material + ", vertices=" + this.vertices + ", indices=" + this.indices + ", attribs=" + this.attribs
				+ ", vertexCount=" + this.vertexCount + ", indicesCount=" + this.indicesCount + ", boundingBox=" + this.boundingBox
				+ ", cleanable=" + this.cleanable + "]";
	}

}
