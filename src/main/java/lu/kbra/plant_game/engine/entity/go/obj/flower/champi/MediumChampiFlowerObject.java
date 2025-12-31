package lu.kbra.plant_game.engine.entity.go.obj.flower.champi;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/champi-medium.json")
public class MediumChampiFlowerObject extends SwayGameObject {

	public MediumChampiFlowerObject(final String str, final SwayMesh mesh) {
		super(str, mesh);
	}

}
