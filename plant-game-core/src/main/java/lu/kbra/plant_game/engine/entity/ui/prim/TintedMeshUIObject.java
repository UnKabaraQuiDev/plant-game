package lu.kbra.plant_game.engine.entity.ui.prim;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.engine.entity.impl.TintOwner;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class TintedMeshUIObject extends MeshUIObject implements TintOwner {

	protected Vector4f tint;

	public TintedMeshUIObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public Vector4f getTint() {
		return this.tint;
	}

	@Override
	public void setTint(final Vector4fc tint) {
		if (this.tint == null) {
			this.tint = new Vector4f(tint);
		} else {
			this.tint.set(tint);
		}
	}

	@Override
	public String toString() {
		return "TintedMeshUIObject@" + System.identityHashCode(this) + " [tint=" + this.tint + ", bounds=" + this.bounds + ", mesh="
				+ this.mesh + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
