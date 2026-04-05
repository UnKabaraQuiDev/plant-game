package lu.kbra.plant_game.base.entity.go.obj_inst;

import lu.kbra.plant_game.engine.entity.go.obj_inst.GrowingInstanceGameObject;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;

public abstract class InstanceGrassObject extends GrowingInstanceGameObject {

	public InstanceGrassObject(final String str, final InstanceEmitter ie) {
		super(str, ie);
	}

	@Override
	public String getGrownName() {
		return "grass";
	}

	@DataPath("classpath:/models/grass-small.json")
	public static class InstanceSmallGrassObject extends InstanceGrassObject {

		public InstanceSmallGrassObject(final String str, final InstanceEmitter ie) {
			super(str, ie);
		}

		@Override
		public int getSize() {
			return SMALL_SIZE;
		}

	}

	@DataPath("classpath:/models/grass-medium.json")
	public static class InstanceMediumGrassObject extends InstanceGrassObject {

		public InstanceMediumGrassObject(final String str, final InstanceEmitter ie) {
			super(str, ie);
		}

		@Override
		public int getSize() {
			return MEDIUM_SIZE;
		}

	}

	@DataPath("classpath:/models/grass-large.json")
	public static class InstanceLargeGrassObject extends InstanceGrassObject {

		public InstanceLargeGrassObject(final String str, final InstanceEmitter ie) {
			super(str, ie);
		}

		@Override
		public int getSize() {
			return LARGE_SIZE;
		}

	}

}
