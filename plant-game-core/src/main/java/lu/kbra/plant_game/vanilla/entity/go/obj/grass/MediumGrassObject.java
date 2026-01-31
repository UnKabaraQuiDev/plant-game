package lu.kbra.plant_game.vanilla.entity.go.obj.grass;

import lu.kbra.plant_game.engine.entity.go.SwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/grass-medium.json")
public class MediumGrassObject extends SwayGameObject {

	public MediumGrassObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
