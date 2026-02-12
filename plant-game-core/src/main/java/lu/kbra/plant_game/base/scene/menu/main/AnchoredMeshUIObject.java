package lu.kbra.plant_game.base.scene.menu.main;

import lu.kbra.plant_game.engine.entity.ui.MeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.standalone.gameengine.geom.Mesh;

public class AnchoredMeshUIObject extends MeshUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredMeshUIObject(final String str, final Mesh mesh) {
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
		return "AnchoredMeshUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active="
				+ this.active + ", name=" + this.name + "]";
	}

}
