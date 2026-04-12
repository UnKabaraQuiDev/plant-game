package lu.kbra.plant_game.plugin.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingCategory;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingDefinition;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class BuildingRegistry extends PluginRegistry {

	public static final Map<BuildingCategory, List<BuildingDefinition<?>>> BUILDING_DEFS = new ConcurrentHashMap<>();

	public BuildingRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	protected <T extends GameObject & PlaceableObject> void register(
			final BuildingCategory cate,
			final Class<T> clazz,
			final int price,
			final List<BuildingRequirement> unlock,
			final List<BuildingRequirement> building,
			final int index) {
		this.register(cate,
				new BuildingDefinition<>(clazz, GameObjectRegistry.getInternalName(clazz), new HashMap<ResourceType, Integer>() {
					{
						this.put(DefaultResourceType.MONEY, price);
					}
				}, unlock, building, index));
	}

	protected <T extends GameObject & PlaceableObject> void register(
			final BuildingCategory cate,
			final Class<T> clazz,
			final Map<ResourceType, Integer> prices,
			final List<BuildingRequirement> unlock,
			final List<BuildingRequirement> building,
			final int index) {
		this.register(cate, new BuildingDefinition<>(clazz, GameObjectRegistry.getInternalName(clazz), prices, unlock, building, index));
	}

	protected void register(final BuildingCategory category, final BuildingDefinition<?> def) {
		BUILDING_DEFS.computeIfAbsent(category, c -> Collections.synchronizedList(new ArrayList<>()));
		BUILDING_DEFS.get(category).add(def);
	}

	@Override
	public int getPriority() {
		return 500;
	}

}
