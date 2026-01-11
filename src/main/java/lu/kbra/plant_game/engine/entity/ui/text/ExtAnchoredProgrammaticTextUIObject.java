package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.ExtAnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
public class ExtAnchoredProgrammaticTextUIObject extends AnchoredProgrammaticTextUIObject implements ExtAnchorOwner, NeedsUpdate {

	protected UIObject target;

	public ExtAnchoredProgrammaticTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
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
	public void update(final WindowInputHandler input) {
		ExtAnchorOwner.super.applyAnchor();
	}

	@Override
	public String toString() {
		return "ExtAnchoredProgrammaticTextUIObject [target=" + this.target + ", objectAnchor=" + this.objectAnchor + ", targetAnchor="
				+ this.targetAnchor + ", key=" + this.key + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
