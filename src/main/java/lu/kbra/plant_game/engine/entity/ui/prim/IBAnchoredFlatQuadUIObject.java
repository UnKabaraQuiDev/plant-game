package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.entity.ui.group.IgnoreBounds;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;

public class IBAnchoredFlatQuadUIObject extends AnchoredFlatQuadUIObject implements IgnoreBounds {

	public IBAnchoredFlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public String toString() {
		return "IBAnchoredFlatQuadUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", color=" + this.color + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
