package lu.kbra.plant_game.engine.entity.go.obj.flower.round;

import lu.kbra.plant_game.engine.entity.go.impl.SwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/flower-round-medium.json")
public class MediumRoundFlowerObject extends SwayGameObject {

	public MediumRoundFlowerObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
