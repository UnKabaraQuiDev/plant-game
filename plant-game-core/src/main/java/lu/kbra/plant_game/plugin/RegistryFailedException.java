package lu.kbra.plant_game.plugin;

public class RegistryFailedException extends PluginLoadException {

	public RegistryFailedException(final PluginDescriptor plugin, final Throwable e) {
		super(plugin, e);
	}

	public RegistryFailedException(final PluginDescriptor plugin, final String msg, final Throwable e) {
		super(plugin, msg, e);
	}

}
