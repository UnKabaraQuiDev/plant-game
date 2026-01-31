package lu.kbra.plant_game.engine.scene.world;

import org.joml.Vector4fc;

import lu.kbra.plant_game.generated.ColorMaterial;

public enum LevelState {

	NOT_STARTED(ColorMaterial.GRAY), STARTED(ColorMaterial.YELLOW), LOST(ColorMaterial.RED), WON(ColorMaterial.LIGHT_GREEN);

	private final Vector4fc color;

	private LevelState(final Vector4fc color) {
		this.color = color;
	}

	private LevelState(final ColorMaterial color) {
		this.color = color.getColor();
	}

	public Vector4fc getColor() {
		return this.color;
	}

}
