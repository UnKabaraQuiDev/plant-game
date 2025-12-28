package lu.kbra.plant_game.engine.entity.go.mesh.terrain;

import org.joml.Vector2i;

import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;

public class TerrainMesh extends LoadedMesh {

	private final int objectId;
	private final int width;
	private final int length;
	private final int maxHeight;

	private final Integer[][] cellHeight;
	private final ColorMaterial[][] materialType;

	public TerrainMesh(
			final String name,
			final int objectId,
			final int width,
			final int length,
			final int maxHeight,
			final Integer[][] cellHeight,
			final ColorMaterial[][] materialType,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final AttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
		this.objectId = objectId;
		this.width = width;
		this.length = length;
		this.maxHeight = maxHeight;
		this.cellHeight = cellHeight;
		this.materialType = materialType;
	}

	public int getCellHeight(final int x, final int z) {
		if (this.isInBounds(x, z)) {
			return this.cellHeight[x][z];
		}

		return Integer.MIN_VALUE;
	}

	public int getCellHeight(final Vector2i rotated) {
		return this.getCellHeight(rotated.x, rotated.y);
	}

	public boolean isInBounds(final int x, final int z) {
		return x >= 0 && x < this.width && z >= 0 && z < this.length;
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

	public ColorMaterial[][] getMaterialTypes() {
		return this.materialType;
	}

	public int getMinHeight() {
		return 0;
	}

	public int getMaxHeight() {
		return this.maxHeight;
	}

}
