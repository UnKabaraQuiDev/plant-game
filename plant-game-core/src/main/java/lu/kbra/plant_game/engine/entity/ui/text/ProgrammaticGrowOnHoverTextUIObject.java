package lu.kbra.plant_game.engine.entity.ui.text;

import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.impl.GrowOnHover;
import lu.kbra.plant_game.engine.entity.ui.data.Scale2dDir;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@DataPath("localization:string-placeholder")
public class ProgrammaticGrowOnHoverTextUIObject extends ProgrammaticTextUIObject implements GrowOnHover {

	protected Scale2dDir dir;
	protected float progress;

	public ProgrammaticGrowOnHoverTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public Vector3fc getTargetScale(final boolean grow) {
		return grow ? switch (this.dir) {
		case HORIZONTAL -> HORIZONTAL_GROWTH_SCALE;
		case VERTICAL -> VERTICAL_GROWTH_SCALE;
		case BOTH -> BOTH_GROWTH_SCALE;
		} : GameEngine.IDENTITY_VECTOR3F;
	}

	@Override
	public float getGrowthRate(final boolean grow) {
		return 0.5f;
	}

	public Scale2dDir getDir() {
		return this.dir;
	}

	public void setDir(final Scale2dDir dir) {
		this.dir = dir;
	}

	@Override
	public float getGrowthProgress() {
		return this.progress;
	}

	@Override
	public void setGrowthProgress(final float f) {
		this.progress = f;
	}

}
