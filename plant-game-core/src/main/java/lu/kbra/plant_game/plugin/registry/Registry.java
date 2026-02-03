package lu.kbra.plant_game.plugin.registry;

import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.exception.RegistryFailedException;

public interface Registry {

	PluginDescriptor getPluginDescriptor();

	void init() throws RegistryFailedException;

}
