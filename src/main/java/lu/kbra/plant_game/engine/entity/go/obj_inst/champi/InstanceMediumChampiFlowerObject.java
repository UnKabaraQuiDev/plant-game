package lu.kbra.plant_game.engine.entity.go.obj_inst.champi;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/champi-medium.json")
public class InstanceMediumChampiFlowerObject extends InstanceSwayGameObject {

	public InstanceMediumChampiFlowerObject(final String str, final SwayInstanceEmitter ie) {
		super(str, ie);
	}

}
