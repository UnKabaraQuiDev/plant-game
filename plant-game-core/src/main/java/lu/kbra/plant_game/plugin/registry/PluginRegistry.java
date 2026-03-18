package lu.kbra.plant_game.plugin.registry;

import lu.kbra.plant_game.engine.entity.impl.NeedsPostConstruct;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class PluginRegistry implements Registry, NeedsPostConstruct {

	protected final PluginDescriptor pluginDescriptor;

	public PluginRegistry(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	@Override
	public void postConstruct() {
	}

	@Override
	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

}
