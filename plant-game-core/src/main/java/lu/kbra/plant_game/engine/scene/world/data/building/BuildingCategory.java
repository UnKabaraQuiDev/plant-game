package lu.kbra.plant_game.engine.scene.world.data.building;

import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.generated.ColorMaterial;

public interface BuildingCategory extends IndexOwner, Localizable {

	String BUILDING_CATEGORY_KEY = "building.category.";

	ColorMaterial getAccentColor();

}
