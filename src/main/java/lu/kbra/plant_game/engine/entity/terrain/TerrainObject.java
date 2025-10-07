package lu.kbra.plant_game.engine.entity.terrain;

import lu.kbra.plant_game.engine.entity.impl.GameObject;
import lu.kbra.plant_game.engine.mesh.AttributeLocation;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TerrainObject extends GameObject {

	public TerrainObject(String str, TerrainMesh mesh) {
		super(str, mesh, new Transform3D(), false);
		setEntityMaterialId(false);
		setObjectIdLocation(AttributeLocation.MESH);
	}

	@Override
	public TerrainMesh getMesh() {
		return (TerrainMesh) super.getMesh();
	}

}
