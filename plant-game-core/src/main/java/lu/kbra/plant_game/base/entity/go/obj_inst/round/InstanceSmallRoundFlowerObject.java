package lu.kbra.plant_game.base.entity.go.obj_inst.round;

import lu.kbra.plant_game.base.entity.go_inst.champi.SizeOwner;
import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/flower-round-small.json")
public class InstanceSmallRoundFlowerObject extends InstanceSwayGameObject implements SizeOwner, GrownObject {

	public InstanceSmallRoundFlowerObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

	@Override
	public int getSize() {
		return SMALL_SIZE;
	}

	@Override
	public String getGrownName() {
		return "round-flower";
	}

}
