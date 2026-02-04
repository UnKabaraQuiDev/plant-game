package lu.kbra.plant_game.plugin.exception;

import java.util.Objects;

import lu.kbra.plant_game.plugin.PluginDescriptor;

public class PluginLoadException extends Exception {

	protected PluginDescriptor plugin;

	public PluginLoadException(final PluginDescriptor plugin, final Throwable e) {
		super(Objects.toString(plugin), e);
		this.plugin = plugin;
	}

	public PluginLoadException(final PluginDescriptor plugin, final String msg, final Throwable e) {
		super(Objects.toString(plugin) + ": " + msg, e);
		this.plugin = plugin;
	}

}
