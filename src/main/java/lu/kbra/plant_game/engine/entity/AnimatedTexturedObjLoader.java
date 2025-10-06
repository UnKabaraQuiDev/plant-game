package lu.kbra.plant_game.engine.entity;

import lu.kbra.plant_game.engine.entity.AnimatedObjectLoader.AnimationData;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.utils.ObjLoader;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class AnimatedTexturedObjLoader {

	public static Mesh loadMesh(String name, Material material, String path, SingleTexture texture, AnimationData animData) {
		return ObjLoader.loadMesh(name, material, path,
				(t) -> new AnimatedTexturedMesh(t.name(), texture, animData, t.vertices(), t.indices(), t.attribs()));
	}

}
