package lu.kbra.plant_game.vanilla.entity.go.obj_inst.grass;

import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/grass-small.json")
public class InstanceSmallGrassObject extends InstanceSwayGameObject {

	public InstanceSmallGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

}
