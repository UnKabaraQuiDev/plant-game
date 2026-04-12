package lu.kbra.plant_game.base.data;

import lu.kbra.plant_game.engine.entity.ui.icon.EnergyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.MoneyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.WaterIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;

public enum DefaultResourceType implements ResourceType {

	WATER(WaterIconUIObject.class),
	ENERGY(EnergyIconUIObject.class),
	MONEY(MoneyIconUIObject.class);

	public static DefaultResourceType byName(final String name) {
		return valueOf(name.toUpperCase());
	}

	protected final Class<? extends TexturedQuadMeshUIObject> iconClass;

	private DefaultResourceType(final Class<? extends TexturedQuadMeshUIObject> iconClass) {
		this.iconClass = iconClass;
	}

	@Override
	public Class<? extends TexturedQuadMeshUIObject> getIconClass() {
		return this.iconClass;
	}

	@Override
	public String getName() {
		return this.name().toLowerCase();
	}

	@Override
	public String getLocalizationKey() {
		return LOCALIZATION_KEY + "base." + this.name().toLowerCase();
	}

}
