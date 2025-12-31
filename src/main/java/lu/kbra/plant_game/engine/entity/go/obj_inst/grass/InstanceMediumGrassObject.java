package lu.kbra.plant_game.engine.entity.go.obj_inst.grass;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/grass-medium.json")
public class InstanceMediumGrassObject extends InstanceSwayGameObject {

	public InstanceMediumGrassObject(final String str, final SwayInstanceEmitter ie) {
		super(str, ie);
	}

}
