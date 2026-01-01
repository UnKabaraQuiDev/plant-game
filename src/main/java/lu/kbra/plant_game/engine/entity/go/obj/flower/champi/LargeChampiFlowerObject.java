package lu.kbra.plant_game.engine.entity.go.obj.flower.champi;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/champi-large.json")
public class LargeChampiFlowerObject extends SwayGameObject {

	public LargeChampiFlowerObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
