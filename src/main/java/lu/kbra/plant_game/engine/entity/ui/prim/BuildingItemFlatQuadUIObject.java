package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.entity.ui.impl.HoverState;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsHover;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;

public class BuildingItemFlatQuadUIObject extends IndexedFlatQuadUIObject implements NeedsHover {

	public BuildingItemFlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public boolean hover(final WindowInputHandler input, final HoverState hoverState) {
		return false;
	}

	@Override
	public String toString() {
		return "BuildingItemFlatQuadUIObject@" + System.identityHashCode(this) + " [index=" + this.index + ", color=" + this.color
				+ ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform=" + this.transform + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
