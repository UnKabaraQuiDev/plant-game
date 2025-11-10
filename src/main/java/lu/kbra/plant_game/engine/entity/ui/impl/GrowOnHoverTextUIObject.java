package lu.kbra.plant_game.engine.entity.ui.impl;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.ui.HoverState;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class GrowOnHoverTextUIObject extends TextUIObject implements GrowOnHover {

	public static final Vector3f HORIZONTAL_GROWTH_SCALE = new Vector3f(1.1f, 1, 1);

	private boolean hovered;

	public GrowOnHoverTextUIObject(String str, TextEmitter text) {
		super(str, text);
	}

	public GrowOnHoverTextUIObject(String str, TextEmitter text, Transform3D transform) {
		super(str, text, transform);
	}

	@Override
	public void hover(WindowInputHandler input, float dTime, HoverState hoverState) {
		hovered = (hoverState == HoverState.ENTER || hoverState == HoverState.STAY);
	}

	@Override
	public boolean isHovered() {
		return hovered;
	}

	@Override
	public Vector3f getTargetScale(boolean grow) {
		return grow ? HORIZONTAL_GROWTH_SCALE : GameEngine.IDENTITY_VECTOR3F;
	}

	@Override
	public float getGrowthRate(boolean grow) {
		return grow ? 1.5f : 3f;
	}

}
