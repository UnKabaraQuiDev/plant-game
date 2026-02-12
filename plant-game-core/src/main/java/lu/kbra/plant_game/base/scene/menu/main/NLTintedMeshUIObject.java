package lu.kbra.plant_game.base.scene.menu.main;

import lu.kbra.plant_game.engine.scene.ui.layout.NoLayout;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class NLTintedMeshUIObject extends TintedMeshUIObject implements NoLayout {

	public NLTintedMeshUIObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

	@Override
	public String toString() {
		return "NLTintedMeshUIObject@" + System.identityHashCode(this) + " [tint=" + this.tint + ", bounds=" + this.bounds + ", mesh="
				+ this.mesh + ", transform=" + this.transform + ", parent=" + this.parent + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
