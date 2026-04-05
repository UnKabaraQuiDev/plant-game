package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import lu.kbra.plant_game.base.entity.go.obj_inst.round.GrownObject;
import lu.kbra.plant_game.base.entity.go_inst.champi.SizeOwner;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/grass-small.json")
public class InstanceSmallGrassObject extends GrowingInstanceGameObject implements SizeOwner, GrownObject {

	public InstanceSmallGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	@Override
	public int getSize() {
		return SMALL_SIZE;
	}

	@Override
	public String getGrownName() {
		return "grass";
	}

}
