package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.entity.impl.IndexOwner;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class IndexedProgrammaticTextUIObject extends ProgrammaticTextUIObject implements IndexOwner {

	protected int index;

	public IndexedProgrammaticTextUIObject(final String str, final TextEmitter text) {
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
		return "IndexedProgrammaticTextUIObject@" + System.identityHashCode(this) + " [index=" + this.index + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
