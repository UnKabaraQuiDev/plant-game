package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.entity.ui.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;

public class AnchoredFlatQuadUIObject extends FlatQuadUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredFlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor objectAnchor) {
		this.objectAnchor = objectAnchor;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor targetAnchor) {
		this.targetAnchor = targetAnchor;
	}

	@Override
	public String toString() {
		return "AnchoredFlatQuadUIObject [objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", color="
				+ this.color + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
