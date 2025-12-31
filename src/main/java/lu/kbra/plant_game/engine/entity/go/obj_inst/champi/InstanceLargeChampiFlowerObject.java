package lu.kbra.plant_game.engine.entity.go.obj_inst.champi;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/champi-large.json")
public class InstanceLargeChampiFlowerObject extends InstanceSwayGameObject {

	public InstanceLargeChampiFlowerObject(final String str, final SwayInstanceEmitter ie) {
		super(str, ie);
	}

}
