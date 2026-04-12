package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.entity.impl.IndexOwner;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;

public class IndexedFlatQuadUIObject extends FlatQuadUIObject implements IndexOwner {

	protected int index;

	public IndexedFlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
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
		return "IndexedFlatQuadUIObject [index=" + this.index + ", color=" + this.color + ", bounds=" + this.bounds + ", mesh=" + this.mesh
				+ ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
