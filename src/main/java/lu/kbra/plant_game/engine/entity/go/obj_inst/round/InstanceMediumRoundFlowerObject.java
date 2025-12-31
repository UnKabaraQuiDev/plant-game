package lu.kbra.plant_game.engine.entity.go.obj_inst.round;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/flower-round-medium.json")
public class InstanceMediumRoundFlowerObject extends InstanceSwayGameObject {

	public InstanceMediumRoundFlowerObject(final String str, final SwayInstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

}
