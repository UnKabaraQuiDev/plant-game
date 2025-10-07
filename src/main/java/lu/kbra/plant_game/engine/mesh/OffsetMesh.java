package lu.kbra.plant_game.engine.mesh;

import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class OffsetMesh extends LoadedMesh {

	private Vector3f origin;

	public OffsetMesh(String name, Material material, Vector3f origin, Vec3fAttribArray vertices, UIntAttribArray indices,
			AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
		this.origin = origin;
	}

	public Vector3f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3f origin) {
		this.origin = origin;
	}

}
