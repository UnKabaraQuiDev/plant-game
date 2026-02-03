package lu.kbra.plant_game.plugin.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class ResourceRegistry extends PluginRegistry {

	public static final String DEBUG_PROPERTY = ResourceRegistry.class.getSimpleName() + ".debug";
	public static boolean DEBUG = Boolean.getBoolean(DEBUG_PROPERTY);

	public static final Map<String, ResourceType> RESOURCE_TYPE_DEFS = new ConcurrentHashMap<>();

	public ResourceRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	protected void register(final Enum<? extends ResourceType> resourceType) {
		final String name = this.pluginDescriptor.getInternalName() + ":" + resourceType.name().toLowerCase();
		if (DEBUG) {
			GlobalLogger.info("Registring resource: " + name + " from " + this.pluginDescriptor);
		}
		RESOURCE_TYPE_DEFS.put(name, (ResourceType) resourceType);
	}

	@Deprecated
	protected void register(final String name, final ResourceType resourceType) {
		final String iname = this.pluginDescriptor.getInternalName() + ":" + name;
		if (DEBUG) {
			GlobalLogger.info("Registring resource: " + iname + " from " + this.pluginDescriptor);
		}
		RESOURCE_TYPE_DEFS.put(iname, resourceType);
	}

}
