package lu.kbra.plant_game.vanilla.entity.go.obj.flower.champi;

import lu.kbra.plant_game.engine.entity.go.SwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/champi-medium.json")
public class MediumChampiFlowerObject extends SwayGameObject {

	public MediumChampiFlowerObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
