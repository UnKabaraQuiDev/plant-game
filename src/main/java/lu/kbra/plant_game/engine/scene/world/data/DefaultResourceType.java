package lu.kbra.plant_game.engine.scene.world.data;

public enum DefaultResourceType implements ResourceType {

	WATER, ENERGY, MONEY;

	public DefaultResourceType byName(final String name) {
		return valueOf(name.toUpperCase());
	}

	@Override
	public String getName() {
		return this.name().toLowerCase();
	}

}
