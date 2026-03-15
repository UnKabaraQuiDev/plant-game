package lu.kbra.plant_game;

import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.generated.ColorMaterial;

public interface BuildingCategory extends IndexOwner, Localizable/* , Comparable<BuildingCategory> */ {

	String BUILDING_CATEGORY_KEY = "building.category.";

	ColorMaterial getAccentColor();

//	@Override
//	default int compareTo(final BuildingCategory o) {
//		return Integer.compare(this.getIndex(), o.getIndex());
//	}

}
