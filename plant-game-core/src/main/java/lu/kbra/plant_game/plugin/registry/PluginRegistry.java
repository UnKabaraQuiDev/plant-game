package lu.kbra.plant_game.plugin.registry;

import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class PluginRegistry implements Registry {

	protected final PluginDescriptor pluginDescriptor;

	public PluginRegistry(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	@Override
	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

}
