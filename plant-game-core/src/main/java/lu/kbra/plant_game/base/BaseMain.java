package lu.kbra.plant_game.base;

import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.plant_game.plugin.PluginMain;
import lu.kbra.plant_game.plugin.PluginManager;

public class BaseMain extends PluginMain {

	public BaseMain(final PluginManager pluginManager, final PluginDescriptor pluginDescriptor) {
		super(pluginManager, pluginDescriptor);
	}

	@Override
	public void onLoad() {
		System.err.println("Base plugin loaded 👍");
	}

	@Override
	public void onEnable() {
		System.err.println("Base plugin enabled 👍");
//		PGLogic.INSTANCE.getCompositor().enableFilter(new VignetteShaderConfiguration());
	}

	@Override
	public void onDisable() {
		System.err.println("Base plugin disabled 👍");
	}

}
