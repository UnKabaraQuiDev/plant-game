package lu.kbra.plant_game.plugin;

public abstract class PluginMain {

	protected final PluginDescriptor pluginDescriptor;

	public PluginMain(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	public abstract void onLoad();

	public abstract void onEnable();

	public abstract void onDisable();

	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + System.identityHashCode(this) + " [pluginDescriptor=" + this.pluginDescriptor + "]";
	}

}
