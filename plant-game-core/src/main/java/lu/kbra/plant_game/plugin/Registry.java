package lu.kbra.plant_game.plugin;

public interface Registry {

	PluginDescriptor getPluginDescriptor();

	void init() throws RegistryFailedException;

}
