package lu.kbra.plant_game.base.reg;

import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;
import lu.kbra.plant_game.plugin.registry.KeyRegistry;

public class BaseKeyRegistry extends KeyRegistry {

	public BaseKeyRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void register() throws RegistryFailedException {
		for (DefaultKeyOption dko : DefaultKeyOption.values()) {
			this.register(dko);
		}
	}

}
