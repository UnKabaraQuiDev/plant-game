package lu.kbra.plant_game.base.reg;

import lu.kbra.plant_game.ResourceRegistry;
import lu.kbra.plant_game.engine.scene.world.data.resource.DefaultResourceType;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public class BaseResourceRegistry extends ResourceRegistry {

	public BaseResourceRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void init() {
		super.register(DefaultResourceType.ENERGY);
		super.register(DefaultResourceType.WATER);
		super.register(DefaultResourceType.MONEY);
	}

}
