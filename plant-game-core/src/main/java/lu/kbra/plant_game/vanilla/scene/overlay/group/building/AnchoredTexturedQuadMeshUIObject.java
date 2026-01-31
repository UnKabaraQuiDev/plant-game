package lu.kbra.plant_game.vanilla.scene.overlay.group.building;

import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;

public class AnchoredTexturedQuadMeshUIObject extends TexturedQuadMeshUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredTexturedQuadMeshUIObject(final String str, final TexturedQuadMesh mesh) {
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
		return "AnchoredTexturedQuadMeshUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor
				+ ", targetAnchor=" + this.targetAnchor + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
