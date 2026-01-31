package lu.kbra.plant_game.vanilla.entity.go_inst.champi;

import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/champi-medium.json")
public class InstanceMediumChampiFlowerObject extends InstanceSwayGameObject {

	public InstanceMediumChampiFlowerObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

}
