package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
public class IndexedAnchoredProgrammaticTextUIObject extends AnchoredProgrammaticTextUIObject implements IndexOwner {

	protected int index;

	public IndexedAnchoredProgrammaticTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "IndexedAnchoredProgrammaticTextUIObject@" + System.identityHashCode(this) + " [index=" + this.index + ", objectAnchor="
				+ this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
