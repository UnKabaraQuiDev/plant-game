package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import org.joml.Vector3i;

import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.plant_game.engine.entity.go.mesh.terrain.TerrainEdgeMesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TerrainEdgeObject extends GameObject {

	public TerrainEdgeObject(
			final String str,
			final TerrainEdgeMesh mesh,
			final Transform3D transform,
			final Vector3i objectId,
			final short materialId) {
		super(str, mesh, transform, objectId, materialId);
	}

	public TerrainEdgeObject(final String str, final TerrainEdgeMesh mesh, final Transform3D transform, final Vector3i objectId) {
		super(str, mesh, transform, objectId);
	}

	public TerrainEdgeObject(final String str, final TerrainEdgeMesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

	public TerrainEdgeObject(final String str, final TerrainEdgeMesh mesh) {
		super(str, mesh);
	}

}
