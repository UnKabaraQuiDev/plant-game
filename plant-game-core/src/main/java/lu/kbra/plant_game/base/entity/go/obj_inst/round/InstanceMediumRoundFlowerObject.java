package lu.kbra.plant_game.base.entity.go.obj_inst.round;

import lu.kbra.plant_game.base.entity.go.obj_inst.grass.GrowingInstanceGameObject;
import lu.kbra.plant_game.base.entity.go_inst.champi.SizeOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/flower-round-medium.json")
public class InstanceMediumRoundFlowerObject extends GrowingInstanceGameObject implements SizeOwner, GrownObject {

	public InstanceMediumRoundFlowerObject(final String str, final InstanceEmitter instanceEmitter) {
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
