package lu.kbra.plant_game.engine.util;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.AnimatedMeshLoader.AnimationData;
import lu.kbra.plant_game.engine.mesh.AnimatedTexturedLoadedMesh;
import lu.kbra.plant_game.engine.mesh.OffsetMesh;
import lu.kbra.plant_game.engine.mesh.TexturedLoadedMesh;
import lu.kbra.standalone.gameengine.geom.utils.ObjLoader;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class AdvObjLoader {

	public static OffsetMesh loadMesh(String name, Material material, String path, Vector3f origin) {
		return ObjLoader.loadMesh(name, material, path,
				(t) -> new OffsetMesh(t.name(), null, origin, t.vertices(), t.indices(), t.attribs()));
	}

	public static AnimatedLoadedMesh loadMesh(String name, Material material, String path, Vector3f origin,
			AnimationData animationData) {
		return ObjLoader.loadMesh(name, material, path, (t) -> new AnimatedLoadedMesh(t.name(), null, origin,
				animationData, t.vertices(), t.indices(), t.attribs()));
	}

	public static TexturedLoadedMesh loadMesh(String name, Material material, String path, Vector3f origin,
			SingleTexture texture) {
		return ObjLoader.loadMesh(name, material, path,
				(t) -> new TexturedLoadedMesh(t.name(), null, origin, texture, t.vertices(), t.indices(), t.attribs()));
	}

	public static AnimatedTexturedLoadedMesh loadMesh(String name, Material material, String path, Vector3f origin,
			SingleTexture texture, AnimationData animationData) {
		return ObjLoader.loadMesh(name, material, path, (t) -> new AnimatedTexturedLoadedMesh(t.name(), null, origin,
				texture, animationData, t.vertices(), t.indices(), t.attribs()));
	}

}
