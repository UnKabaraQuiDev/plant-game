package lu.kbra.plant_game.engine.entity.ui;

import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;

public class ProgrammaticTexturedQuadMeshUIObject extends TexturedQuadMeshUIObject implements ProgrammaticUIObject, IndexOwner {

	protected int index;

	public ProgrammaticTexturedQuadMeshUIObject(String str, TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "ProgrammaticTexturedQuadMeshUIObject [bounds=" + bounds + ", mesh=" + mesh + ", transform=" + transform + ", active="
				+ active + ", name=" + name + "]";
	}

}
