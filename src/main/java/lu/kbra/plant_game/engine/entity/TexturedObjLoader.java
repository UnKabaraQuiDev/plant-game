package lu.kbra.plant_game.engine.entity;

import lu.kbra.plant_game.engine.entity.impl.TexturedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.utils.ObjLoader;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedObjLoader {

	public static Mesh loadMesh(String name, Material material, String path, SingleTexture texture) {
		return ObjLoader.loadMesh(name, material, path, (t) -> new TexturedMesh(t.name(), texture, t.vertices(), t.indices(), t.attribs()));
	}

}
