package lu.kbra.plant_game.engine.entity.go.mesh.pipe;

import lu.kbra.plant_game.engine.entity.go.mesh.path.LineLoadedMesh;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;

public class PipeMesh extends LineLoadedMesh {

	public PipeMesh(
			final String name,
			final int objectId,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final JavaAttribArray... attribs) {
		super(name, objectId, vertices, indices, attribs);
	}

}
