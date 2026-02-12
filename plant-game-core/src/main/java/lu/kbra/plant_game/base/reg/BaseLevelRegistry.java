package lu.kbra.plant_game.base.reg;

import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;
import lu.kbra.plant_game.plugin.registry.LevelRegistry;

public class BaseLevelRegistry extends LevelRegistry {

	public BaseLevelRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void init() throws RegistryFailedException {
		this.register(0, "level0");
		this.register(10, "level1");
		this.register(20, "level2");
	}

}
