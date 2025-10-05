package lu.kbra.plant_game.engine.entity;

import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TerrainObject extends GameObject {

	public TerrainObject(String str, TerrainMesh mesh) {
		super(str, mesh, new Transform3D(), false);
		setCompositeMaterialId(true);
		setCompositeObjectId(true);
	}

	@Override
	public TerrainMesh getMesh() {
		return (TerrainMesh) super.getMesh();
	}
	
}
