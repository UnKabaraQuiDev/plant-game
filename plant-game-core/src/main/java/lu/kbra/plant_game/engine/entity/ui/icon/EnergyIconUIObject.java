package lu.kbra.plant_game.engine.entity.ui.icon;

import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("image:classpath:/icons/energy-128.png")
public class EnergyIconUIObject extends TexturedQuadMeshUIObject {

	public EnergyIconUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

}
