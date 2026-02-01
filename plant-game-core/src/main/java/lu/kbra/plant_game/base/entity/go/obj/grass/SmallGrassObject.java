package lu.kbra.plant_game.base.entity.go.obj.grass;

import lu.kbra.plant_game.engine.entity.go.SwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/grass-small.json")
public class SmallGrassObject extends SwayGameObject {

	public SmallGrassObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
