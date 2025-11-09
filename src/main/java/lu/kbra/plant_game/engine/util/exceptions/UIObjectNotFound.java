package lu.kbra.plant_game.engine.util.exceptions;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;

public class UIObjectNotFound extends RuntimeException {

	private final Class<? extends UIObject> clazz;
	private final Object[] args;

	public UIObjectNotFound(Class<? extends UIObject> clazz, Object[] args) {
		super("UIObject not found for name: " + clazz.getName());
		this.clazz = clazz;
		this.args = args;
	}

}
