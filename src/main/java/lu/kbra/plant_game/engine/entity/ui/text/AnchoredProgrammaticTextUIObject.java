package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.overlay.AnchorOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
public class AnchoredProgrammaticTextUIObject extends ProgrammaticTextUIObject implements AnchorOwner {

	protected Anchor objectAnchor;
	protected Anchor targetAnchor;

	public AnchoredProgrammaticTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
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
		return "AnchoredProgrammaticTextUIObject [objectAnchor=" + this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", key="
				+ this.key + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
