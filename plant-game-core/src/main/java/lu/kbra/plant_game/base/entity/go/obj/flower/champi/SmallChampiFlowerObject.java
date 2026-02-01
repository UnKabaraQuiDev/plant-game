package lu.kbra.plant_game.base.entity.go.obj.flower.champi;

import lu.kbra.plant_game.engine.entity.go.SwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/champi-small.json")
public class SmallChampiFlowerObject extends SwayGameObject {

	public SmallChampiFlowerObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
