package lu.kbra.plant_game.engine.entity.ui.gradient;

import lu.kbra.plant_game.engine.entity.impl.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.standalone.gameengine.geom.QuadMesh;

public class AnchoredGradientQuadUIObject extends GradientQuadUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredGradientQuadUIObject(final String str, final QuadMesh mesh) {
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
		return "AnchoredGradientQuadUIObject@" + System.identityHashCode(this) + " [objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", tint=" + this.tint + ", startColor=" + this.startColor + ", endColor=" + this.endColor
				+ ", gradientDirection=" + this.gradientDirection + ", gradientRange=" + this.gradientRange + ", bounds=" + this.bounds
				+ ", mesh=" + this.mesh + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
