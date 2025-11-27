package lu.kbra.plant_game.engine.entity.go.mesh.terrain;

import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LineMesh;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.utils.gl.consts.BeginMode;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonDrawMode;
import lu.kbra.standalone.gameengine.utils.gl.consts.PolygonMode;

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
		return 2;
	}

	@Override
	public PolygonDrawMode getPolygonDrawMode() {
		return PolygonDrawMode.LINE;
	}

	@Override
	public PolygonMode getPolygonMode() {
		return PolygonMode.FRONT_AND_BACK;
	}

	@Override
	public BeginMode getBeginMode() {
		return BeginMode.LINES;
	}

}
