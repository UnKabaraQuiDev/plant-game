package lu.kbra.plant_game.engine.entity.ui.text;

import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.ui.impl.GrowOnHover;
import lu.kbra.plant_game.engine.entity.ui.impl.HoverState;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class GrowOnHoverTextUIObject extends TextUIObject implements GrowOnHover {

	private boolean hovered;
	private Scale2dDir dir;

	public GrowOnHoverTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
	}

	@Override
	public void hover(final WindowInputHandler input, final HoverState hoverState) {
		this.hovered = (hoverState == HoverState.ENTER || hoverState == HoverState.STAY);
	}

	@Override
	public boolean isHovered() {
		return this.hovered;
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
		return grow ? 1.5f : 3f;
	}

	public Scale2dDir getDir() {
		return this.dir;
	}

	public void setDir(final Scale2dDir dir) {
		this.dir = dir;
	}

}
