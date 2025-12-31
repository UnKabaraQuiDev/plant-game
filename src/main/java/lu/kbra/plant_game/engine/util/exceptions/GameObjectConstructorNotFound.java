package lu.kbra.plant_game.engine.util.exceptions;

import java.util.Arrays;
import java.util.stream.Collectors;

import lu.kbra.plant_game.engine.entity.go.GameObject;

public class GameObjectConstructorNotFound extends GameObjectException {

	private final Class<? extends GameObject> clazz;
	private final Object[] args;

	public GameObjectConstructorNotFound(final Class<? extends GameObject> clazz, final Object[] args) {
		super("Matching constructor for GameObject not found: " + clazz.getName() + " ("
				+ Arrays.stream(args).map(c -> c.getClass().getSimpleName()).collect(Collectors.joining(", ")) + ")");
		this.clazz = clazz;
		this.args = args;
	}

}
