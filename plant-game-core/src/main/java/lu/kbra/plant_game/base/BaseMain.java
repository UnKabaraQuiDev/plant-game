package lu.kbra.plant_game.base;

import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.PluginMain;

public class BaseMain extends PluginMain {

	public BaseMain(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void onLoad() {
		System.err.println("Base plugin loaded 👍");
	}

	@Override
	public void onEnable() {
		System.err.println("Base plugin enabled 👍");
	}

	@Override
	public void onDisable() {
		System.err.println("Base plugin disabled 👍");
	}

}
