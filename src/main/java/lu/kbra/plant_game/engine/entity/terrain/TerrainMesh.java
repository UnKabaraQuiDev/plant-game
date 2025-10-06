package lu.kbra.plant_game.engine.entity.terrain;

import lu.kbra.plant_game.engine.scene.WorldGenerator.TerrainMaterialType;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class TerrainMesh extends Mesh {

	private int width, length;

	private Integer[][] cellHeight;
	private TerrainMaterialType[][] materialType;

	public TerrainMesh(String name, int width, int length, Integer[][] cellHeight, TerrainMaterialType[][] materialType,
			Vec3fAttribArray vertices, UIntAttribArray indices, AttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
		this.width = width;
		this.length = length;
		this.cellHeight = cellHeight;
		this.materialType = materialType;
	}

	public int getCellHeight(int x, int z) {
		return cellHeight[x][z];
	}

	public Integer[][] getCellHeights() {
		return cellHeight;
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
