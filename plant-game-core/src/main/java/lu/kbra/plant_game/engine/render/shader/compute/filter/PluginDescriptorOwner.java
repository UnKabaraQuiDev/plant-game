package lu.kbra.plant_game.engine.render.shader.compute.filter;

import lu.kbra.plant_game.plugin.PluginDescriptor;

public interface PluginDescriptorOwner {

	PluginDescriptor getPluginDescriptor();

	void setPluginDescriptor(PluginDescriptor pd);

}
