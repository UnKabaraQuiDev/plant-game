package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.entity.impl.ExtAnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;

public class ExtAnchoredTexturedQuadMeshUIObject extends TexturedQuadMeshUIObject implements ExtAnchorOwner {

	protected UIObject target;
	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public ExtAnchoredTexturedQuadMeshUIObject(final String str, final TexturedQuadMesh mesh) {
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
		return "ExtAnchoredTexturedQuadMeshUIObject@" + System.identityHashCode(this) + " [target=" + this.target + ", objectAnchor="
				+ this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", bounds=" + this.bounds + ", mesh=" + this.mesh
				+ ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
