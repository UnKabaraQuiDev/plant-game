package lu.kbra.plant_game.engine.entity.go.obj.grass;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/grass-small.json")
public class SmallGrassObject extends SwayGameObject {

	public SmallGrassObject(final String str, final SwayMesh mesh) {
		super(str, mesh);
	}

}
