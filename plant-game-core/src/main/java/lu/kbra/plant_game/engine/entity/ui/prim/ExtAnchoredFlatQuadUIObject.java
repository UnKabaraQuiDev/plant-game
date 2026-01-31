package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.ExtAnchorOwner;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;

public class ExtAnchoredFlatQuadUIObject extends AnchoredFlatQuadUIObject implements ExtAnchorOwner {

	protected UIObject target;

	public ExtAnchoredFlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public UIObject getTarget() {
		return this.target;
	}

	@Override
	public void setTarget(final UIObject target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "ExtAnchoredFlatQuadUIObject [target=" + this.target + ", objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", color=" + this.color + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
