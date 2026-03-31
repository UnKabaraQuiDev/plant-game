package lu.kbra.plant_game.base.data;

import lu.kbra.plant_game.engine.scene.world.data.building.BuildingCategory;
import lu.kbra.plant_game.generated.ColorMaterial;

public enum DefaultBuildingCategories implements BuildingCategory {

	ENERGY(ColorMaterial.YELLOW, 10),
	WATER(ColorMaterial.CYAN, 20);

	protected final String localizationKey;
	protected final ColorMaterial accentColor;
	protected final int index;

	DefaultBuildingCategories(final ColorMaterial mainColor, final int index) {
		this.localizationKey = BUILDING_CATEGORY_KEY + this.name().toLowerCase();
		this.accentColor = mainColor;
		this.index = index;
	}

	@Override
	public String getLocalizationKey() {
		return this.localizationKey;
	}

	@Override
	public ColorMaterial getAccentColor() {
		return this.accentColor;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

}
