package lu.kbra.plant_game.engine.entity.ui.gradient;

import lu.kbra.plant_game.engine.entity.impl.ExtAnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.standalone.gameengine.geom.QuadMesh;

public class ExtAnchoredGradientQuadUIObject extends AnchoredGradientQuadUIObject implements ExtAnchorOwner {

	protected UIObject target;

	public ExtAnchoredGradientQuadUIObject(final String str, final QuadMesh mesh) {
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

}
