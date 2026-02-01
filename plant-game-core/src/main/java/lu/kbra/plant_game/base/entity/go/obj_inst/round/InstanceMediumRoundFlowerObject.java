package lu.kbra.plant_game.base.entity.go.obj_inst.round;

import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/flower-round-medium.json")
public class InstanceMediumRoundFlowerObject extends InstanceSwayGameObject {

	public InstanceMediumRoundFlowerObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

}
