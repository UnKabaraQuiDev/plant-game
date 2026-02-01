package lu.kbra.plant_game.base.entity.go.obj.flower.round;

import lu.kbra.plant_game.engine.entity.go.SwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;

@DataPath("classpath:/models/flower-round-small.json")
public class SmallRoundFlowerObject extends SwayGameObject {

	public SmallRoundFlowerObject(final String str, final Mesh mesh) {
		super(str, mesh);
	}

}
