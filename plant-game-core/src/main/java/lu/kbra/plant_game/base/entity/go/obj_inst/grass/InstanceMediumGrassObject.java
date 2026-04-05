package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import lu.kbra.plant_game.base.entity.go.obj_inst.round.GrownObject;
import lu.kbra.plant_game.base.entity.go_inst.champi.SizeOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/grass-medium.json")
public class InstanceMediumGrassObject extends GrowingInstanceGameObject implements SizeOwner, GrownObject {

	public InstanceMediumGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	@Override
	public int getSize() {
		return MEDIUM_SIZE;
	}

	@Override
	public String getGrownName() {
		return "grass";
	}

}
