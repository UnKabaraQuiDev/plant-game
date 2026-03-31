package lu.kbra.plant_game.base.entity.go.obj_inst.grass;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

@DataPath("classpath:/models/grass-medium.json")
public class InstanceMediumGrassObject extends GrowingInstanceGameObject {

	public InstanceMediumGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

}
