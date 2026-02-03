package lu.kbra.plant_game;

import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class PluginRegistry implements lu.kbra.plant_game.plugin.Registry {

	protected final PluginDescriptor pluginDescriptor;

	public PluginRegistry(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	@Override
	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

}
