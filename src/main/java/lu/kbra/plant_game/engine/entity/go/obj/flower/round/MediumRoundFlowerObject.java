package lu.kbra.plant_game.engine.entity.go.obj.flower.round;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.render.SwayMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/flower-round-medium.json")
public class MediumRoundFlowerObject extends SwayGameObject {

	public MediumRoundFlowerObject(final String str, final SwayMesh mesh) {
		super(str, mesh);
	}

}
