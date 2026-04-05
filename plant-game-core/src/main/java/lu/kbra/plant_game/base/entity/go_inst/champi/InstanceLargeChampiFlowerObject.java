package lu.kbra.plant_game.base.entity.go_inst.champi;

import lu.kbra.plant_game.base.entity.go.obj_inst.round.GrownObject;
import lu.kbra.plant_game.engine.entity.go.InstanceSwayGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/champi-large.json")
public class InstanceLargeChampiFlowerObject extends InstanceSwayGameObject implements SizeOwner, GrownObject {

	public InstanceLargeChampiFlowerObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	@Override
	public int getSize() {
		return LARGE_SIZE;
	}

	@Override
	public String getGrownName() {
		return "champi-flower";
	}

}
