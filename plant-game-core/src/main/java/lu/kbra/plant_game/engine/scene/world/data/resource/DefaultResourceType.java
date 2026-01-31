package lu.kbra.plant_game.engine.scene.world.data.resource;

import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.EnergyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.MoneyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.WaterIconUIObject;

public enum DefaultResourceType implements ResourceType {

	WATER(WaterIconUIObject.class), ENERGY(EnergyIconUIObject.class), MONEY(MoneyIconUIObject.class);

	public static DefaultResourceType byName(final String name) {
		return valueOf(name.toUpperCase());
	}

	private Class<? extends TexturedQuadMeshUIObject> iconClass;

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

}
