package lu.kbra.plant_game.engine.entity.go.obj_inst.round;

import lu.kbra.plant_game.engine.entity.go.impl.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.entity.go.impl.SwayInstanceEmitter;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("classpath:/models/flower-round-small.json")
public class InstanceSmallRoundFlowerObject extends InstanceSwayGameObject {

	public InstanceSmallRoundFlowerObject(final String str, final SwayInstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

}
