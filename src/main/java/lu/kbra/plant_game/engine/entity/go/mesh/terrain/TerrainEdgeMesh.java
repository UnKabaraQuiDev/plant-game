package lu.kbra.plant_game.engine.entity.go.mesh.terrain;

import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LineMesh;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;

public class TerrainEdgeMesh extends LoadedMesh implements LineMesh {

	public static final int BUFFER_GROW_SIZE = 16;

	public TerrainEdgeMesh(
			final String name,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final AttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
	}

	@Override
	public float getLineWidth() {
		return 2.5f;
	}

}
