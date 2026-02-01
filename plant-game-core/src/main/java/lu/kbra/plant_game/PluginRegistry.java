package lu.kbra.plant_game;

import lu.kbra.plant_game.plugin.PluginDescriptor;

public class PluginRegistry {

	protected final PluginDescriptor pluginDescriptor;

	public PluginRegistry(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

}
