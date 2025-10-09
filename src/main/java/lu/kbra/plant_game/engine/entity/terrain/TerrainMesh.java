package lu.kbra.plant_game.engine.entity.terrain;

import lu.kbra.plant_game.engine.scene.WorldGenerator.TerrainMaterialType;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;

public class TerrainMesh extends LoadedMesh {

	private int objectId, width, length;

	private Integer[][] cellHeight;
	private TerrainMaterialType[][] materialType;

	public TerrainMesh(String name, int objectId, int width, int length, Integer[][] cellHeight,
			TerrainMaterialType[][] materialType, Vec3fAttribArray vertices, UIntAttribArray indices,
			AttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
		this.objectId = objectId;
		this.width = width;
		this.length = length;
		this.cellHeight = cellHeight;
		this.materialType = materialType;
	}

	public int getCellHeight(int x, int z) {
		if (isInBounds(x, z))
			return cellHeight[x][z];

		return Integer.MIN_VALUE;
	}

	public boolean isInBounds(int x, int z) {
		return x >= 0 && x < width && z >= 0 && z < length;
	}

	public Integer[][] getCellHeights() {
		return cellHeight;
	}

	public int getObjectId() {
		return objectId;
	}

	public int getWidth() {
		return width;
	}

	public int getLength() {
		return length;
	}

	public TerrainMaterialType getCellMaterial(int x, int z) {
		return materialType[x][z];
	}

	public TerrainMaterialType[][] getMaterialTypes() {
		return materialType;
	}

}
