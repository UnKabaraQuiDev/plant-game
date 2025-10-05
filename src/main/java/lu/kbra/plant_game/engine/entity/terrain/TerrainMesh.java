package lu.kbra.plant_game.engine.entity.terrain;

import lu.kbra.plant_game.engine.scene.WorldGenerator.TerrainMaterialType;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class TerrainMesh extends Mesh {

	private Integer[][] cellHeight;
	private TerrainMaterialType[][] materialType;

	public TerrainMesh(String name, Integer[][] cellHeight, TerrainMaterialType[][] materialType, Vec3fAttribArray vertices,
			UIntAttribArray indices, AttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
		this.cellHeight = cellHeight;
		this.materialType = materialType;
	}

	public int getCellHeight(int x, int z) {
		return cellHeight[x][z];
	}

	public Integer[][] getCellHeight() {
		return cellHeight;
	}

	public TerrainMaterialType getCellMaterial(int x, int z) {
		return materialType[x][z];
	}

	public TerrainMaterialType[][] getMaterialType() {
		return materialType;
	}

}
