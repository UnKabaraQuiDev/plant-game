package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.pcy113.pclib.impl.TriConsumer;

import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.scene.Scene;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class DelegatingTextUIObject extends TextUIObject implements NeedsHover, NeedsClick {

	private final TriConsumer<WindowInputHandler, Float, DelegatingTextUIObject> hoverDelegate;
	private final TriConsumer<WindowInputHandler, Float, DelegatingTextUIObject> clickDelegate;

	public DelegatingTextUIObject(String str, TextEmitter text,
			TriConsumer<WindowInputHandler, Float, DelegatingTextUIObject> hoverDelegate,
			TriConsumer<WindowInputHandler, Float, DelegatingTextUIObject> clickDelegate) {
		super(str, text);
		this.hoverDelegate = hoverDelegate;
		this.clickDelegate = clickDelegate;
	}

	public DelegatingTextUIObject(String str, TextEmitter text, Transform3D transform,
			TriConsumer<WindowInputHandler, Float, DelegatingTextUIObject> hoverDelegate,
			TriConsumer<WindowInputHandler, Float, DelegatingTextUIObject> clickDelegate) {
		super(str, text, transform);
		this.hoverDelegate = hoverDelegate;
		this.clickDelegate = clickDelegate;
	}

	@Override
	public void hover(WindowInputHandler input, float dTime, HoverState hoverState, Scene scene) {
		if (hoverDelegate != null) {
			hoverDelegate.accept(input, dTime, this);
		}
	}

	@Override
	public void click(WindowInputHandler input, float dTime, Scene scene) {
		if (clickDelegate != null) {
			clickDelegate.accept(input, dTime, this);
		}
	}

}
