package lu.kbra.plant_game;

import lu.kbra.plant_game.engine.data.locale.LocalizationService;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.generated.ColorMaterial;

public class BuildingCategory implements IndexOwner {

	public static final String LK_BUILDING_CATEGORY = "building.category.";

	protected final String localizationKey;
	protected final ColorMaterial accentColor;
	protected final int index;

	public BuildingCategory(final String localizationName, final ColorMaterial mainColor, final int index) {
		this.localizationKey = LK_BUILDING_CATEGORY + localizationName;
		this.accentColor = mainColor;
		this.index = index;
	}

	public String getLocalizationKey() {
		return this.localizationKey;
	}

	public ColorMaterial getAccentColor() {
		return this.accentColor;
	}

	public String getLocalizedName() {
		return LocalizationService.get(this.getLocalizationKey());
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public String toString() {
		return "BuildingCategory@" + System.identityHashCode(this) + " [localizationKey=" + this.localizationKey + ", accentColor="
				+ this.accentColor + ", index=" + this.index + "]";
	}

}
