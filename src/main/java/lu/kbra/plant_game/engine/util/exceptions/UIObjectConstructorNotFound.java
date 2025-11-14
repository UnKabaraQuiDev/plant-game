package lu.kbra.plant_game.engine.util.exceptions;

import java.util.Arrays;
import java.util.stream.Collectors;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;

public class UIObjectConstructorNotFound extends RuntimeException {

	private final Class<? extends UIObject> clazz;
	private final Object[] args;

	public UIObjectConstructorNotFound(Class<? extends UIObject> clazz, Object[] args) {
		super("Matching constructor for UIObject not found: " + clazz.getName() + " ("
				+ Arrays.stream(args).map(c -> c.getClass().getSimpleName()).collect(Collectors.joining(", ")) + ")");
		this.clazz = clazz;
		this.args = args;
	}

}
