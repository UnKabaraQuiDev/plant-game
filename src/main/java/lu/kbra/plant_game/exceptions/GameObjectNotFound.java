package lu.kbra.plant_game.exceptions;

import java.util.Arrays;

import lu.kbra.plant_game.engine.entity.impl.GameObject;

public class GameObjectNotFound extends RuntimeException {

	private final Class<? extends GameObject> clazz;
	private final Object[] args;

	public GameObjectNotFound(Class<? extends GameObject> clazz, Object[] args) {
		super("GameObject not found for name: " + clazz.getName());
		this.clazz = clazz;
		this.args = args;
	}

	@Override
	public String toString() {
		return "GameObjectNotFound [clazz=" + clazz + ", args=" + Arrays.toString(args) + "]";
	}

}
