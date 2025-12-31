package lu.kbra.plant_game.engine.entity.go.obj.grass;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/grass-medium.json")
public class MediumGrassObject extends SwayGameObject {

	public MediumGrassObject(final String str, final SwayMesh mesh) {
		super(str, mesh);
	}

}
