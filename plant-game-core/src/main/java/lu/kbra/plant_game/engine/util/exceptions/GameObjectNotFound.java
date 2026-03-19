package lu.kbra.plant_game.engine.util.exceptions;

import lu.kbra.plant_game.engine.entity.go.GameObject;

public class GameObjectNotFound extends GameObjectException {

	private final Class<? extends GameObject> clazz;
	private final Object[] args;

	public GameObjectNotFound(final Class<? extends GameObject> clazz, final Object[] args) {
		super("GameObject not found for name: " + clazz.getName());
		this.clazz = clazz;
		this.args = args;
	}

}
