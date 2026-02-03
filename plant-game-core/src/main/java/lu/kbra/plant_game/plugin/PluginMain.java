package lu.kbra.plant_game.plugin;

public abstract class PluginMain {

	protected final PluginManager pluginManager;
	protected final PluginDescriptor pluginDescriptor;

	public PluginMain(final PluginManager pluginManager, final PluginDescriptor pluginDescriptor) {
		this.pluginManager = pluginManager;
		this.pluginDescriptor = pluginDescriptor;
	}

	public abstract void onLoad();

	public abstract void onEnable();

	public abstract void onDisable();

	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

	@Override
	public String toString() {
		return "PluginMain@" + System.identityHashCode(this) + " [pluginManager=" + this.pluginManager + ", pluginDescriptor="
				+ this.pluginDescriptor + "]";
	}

}
