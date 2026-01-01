package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.plant_game.engine.mesh.ColorMaterialOwner;
import lu.kbra.plant_game.generated.ColorMaterial;

public interface MaterialIdOwner extends ColorMaterialOwner {

	boolean isEntityMaterialId();

	short getMaterialId();

	void setIsEntityMaterialId(boolean ie);

	void setMaterialId(short ie);

	@Override
	default void setColorMaterial(final ColorMaterial cm) {
		if (cm == null) {
			this.setMaterialId(ColorMaterial.BLACK.getId());
		}
		this.setMaterialId(cm.getId());
	}

}
