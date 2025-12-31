package lu.kbra.plant_game.engine.entity.go.obj_inst.champi;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/champi-small.json")
public class InstanceSmallChampiFlowerObject extends InstanceSwayGameObject {

	public InstanceSmallChampiFlowerObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

}
