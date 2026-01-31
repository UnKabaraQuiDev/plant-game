package lu.kbra.plant_game.engine.entity.impl;

import org.joml.Vector4fc;

import lu.kbra.plant_game.generated.ColorMaterial;

public interface TintOwner extends ColorMaterialOwner {

	Vector4fc getTint();

	void setTint(Vector4fc tint);

	@Override
	default void setColorMaterial(final ColorMaterial tint) {
		if (tint == null) {
			this.setTint(ColorMaterial.BLACK.getColor());
			return;
		}

		this.setTint(tint.getColor());
	}

}
