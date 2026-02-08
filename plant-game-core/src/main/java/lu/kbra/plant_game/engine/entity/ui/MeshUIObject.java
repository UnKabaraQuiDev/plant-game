package lu.kbra.plant_game.engine.entity.ui;

import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;

public class MeshUIObject extends UIObject implements MeshOwner {

	protected Rectangle2D.Float bounds;

	protected Mesh mesh;

	public MeshUIObject(final String str, final Mesh mesh) {
		super(str);
		this.setMesh(mesh);
	}

	protected void recomputeBounds() {
		this.bounds = this.mesh.getBoundingBox().project(GeoPlane.XZ);
	}

	@Override
	public Mesh getMesh() {
		return this.mesh;
	}

	@Override
	public void setMesh(final Mesh m) {
		this.mesh = m;
		this.recomputeBounds();
	}

	@Override
	public Rectangle2D.Float getBounds() {
		return this.bounds;
	}

	@Override
	public String toString() {
		return "MeshUIObject@" + System.identityHashCode(this) + " [bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
