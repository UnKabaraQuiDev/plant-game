package lu.kbra.plant_game.engine.entity.ui.prim;

import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class MeshUIObject extends UIObject {

	protected Rectangle2D.Float bounds;

	public MeshUIObject(final String str, final Mesh mesh) {
		this(str, mesh, null);
	}

	public MeshUIObject(final String str, final Mesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
		if (mesh != null) {
			this.bounds = mesh.getBoundingBox().project(GeoPlane.XZ);
		}
	}

	@Override
	public Rectangle2D.Float getBounds() {
		return this.bounds;
	}

}
