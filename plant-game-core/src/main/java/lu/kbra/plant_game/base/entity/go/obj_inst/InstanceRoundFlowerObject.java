package lu.kbra.plant_game.base.entity.go.obj_inst;

import lu.kbra.plant_game.engine.entity.go.obj_inst.GrowingInstanceGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

public abstract class InstanceRoundFlowerObject extends GrowingInstanceGameObject {

	public InstanceRoundFlowerObject(final String str, final InstanceEmitter instanceEmitter) {
		super(str, instanceEmitter);
	}

	@Override
	public String getGrownName() {
		return "round-flower";
	}

	@DataPath("classpath:/models/flower-round-small.json")
	public static class InstanceSmallRoundFlowerObject extends InstanceRoundFlowerObject {

		public InstanceSmallRoundFlowerObject(final String str, final InstanceEmitter instanceEmitter) {
			super(str, instanceEmitter);
		}

		@Override
		public int getSize() {
			return SMALL_SIZE;
		}

	}

	@DataPath("classpath:/models/flower-round-medium.json")
	public static class InstanceMediumRoundFlowerObject extends InstanceRoundFlowerObject {

		public InstanceMediumRoundFlowerObject(final String str, final InstanceEmitter instanceEmitter) {
			super(str, instanceEmitter);
		}

		@Override
		public int getSize() {
			return MEDIUM_SIZE;
		}

	}

	@DataPath("classpath:/models/flower-round-large.json")
	public static class InstanceLargeRoundFlowerObject extends InstanceRoundFlowerObject {

		public InstanceLargeRoundFlowerObject(final String str, final InstanceEmitter instanceEmitter) {
			super(str, instanceEmitter);
		}

		@Override
		public int getSize() {
			return LARGE_SIZE;
		}

	}

}
