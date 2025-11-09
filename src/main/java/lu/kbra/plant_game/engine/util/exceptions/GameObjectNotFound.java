package lu.kbra.plant_game.engine.util.exceptions;

import lu.kbra.plant_game.engine.entity.go.impl.GameObject;

public class GameObjectNotFound extends RuntimeException {

	private final Class<? extends GameObject> clazz;
	private final Object[] args;

	public GameObjectNotFound(Class<? extends GameObject> clazz, Object[] args) {
		super("GameObject not found for name: " + clazz.getName());
		this.clazz = clazz;
		this.args = args;
	}

}
