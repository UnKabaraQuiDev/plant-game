package lu.kbra.plant_game.engine.scene.ui.overlay;

import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.standalone.gameengine.objs.entity.Component;

public class AnchorComponent extends Component implements AnchorOwner {

	private Anchor objectAnchor;
	private Anchor targetAnchor;

	public AnchorComponent() {
		this(Anchor.CENTER_CENTER, Anchor.CENTER_CENTER);
	}

	public AnchorComponent(final Anchor objectAnchor, final Anchor targetAnchor) {
		this.objectAnchor = objectAnchor;
		this.targetAnchor = targetAnchor;
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor a) {
		this.objectAnchor = a;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor a) {
		this.targetAnchor = a;
	}

}
