package lu.kbra.plant_game.engine.entity.go.obj.terrain;

import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.go.impl.GameObject;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TerrainHighlightObject extends GameObject {

	public TerrainHighlightObject(
			final String str,
			final Mesh mesh,
			final Transform3D transform,
			final Vector3ic objectId,
			final short materialId) {
		super(str, mesh, transform, objectId, materialId);
	}

	public TerrainHighlightObject(final String str, final Mesh mesh, final Transform3D transform, final Vector3ic objectId) {
		super(str, mesh, transform, objectId);
	}

	public TerrainHighlightObject(final String str, final Mesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

	public TerrainHighlightObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
