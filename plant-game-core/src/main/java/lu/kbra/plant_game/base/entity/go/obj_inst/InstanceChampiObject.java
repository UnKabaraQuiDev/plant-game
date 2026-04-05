package lu.kbra.plant_game.base.entity.go.obj_inst;

import lu.kbra.plant_game.engine.entity.go.obj_inst.GrowingInstanceGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

public abstract class InstanceChampiObject extends GrowingInstanceGameObject {

	public InstanceChampiObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

	@Override
	public String getGrownName() {
		return "round-flower";
	}

	@DataPath("classpath:/models/champi-small.json")
	public static class InstanceSmallChampiObject extends InstanceChampiObject {

		public InstanceSmallChampiObject(final String str, final InstanceEmitter instanceEmitter) {
			super(str, instanceEmitter);
		}

		@Override
		public int getSize() {
			return SMALL_SIZE;
		}

	}

	@DataPath("classpath:/models/champi-medium.json")
	public static class InstanceMediumChampiObject extends InstanceChampiObject {

		public InstanceMediumChampiObject(final String str, final InstanceEmitter instanceEmitter) {
			super(str, instanceEmitter);
		}

		@Override
		public int getSize() {
			return MEDIUM_SIZE;
		}

	}

	@DataPath("classpath:/models/champi-large.json")
	public static class InstanceLargeChampiObject extends InstanceChampiObject {

		public InstanceLargeChampiObject(final String str, final InstanceEmitter instanceEmitter) {
			super(str, instanceEmitter);
		}

		@Override
		public int getSize() {
			return LARGE_SIZE;
		}

	}

}