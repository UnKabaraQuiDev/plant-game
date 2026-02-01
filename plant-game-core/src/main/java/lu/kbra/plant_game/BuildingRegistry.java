package lu.kbra.plant_game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.DefaultResourceType;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class BuildingRegistry extends PluginRegistry {

	public static final Map<BuildingCategory, List<BuildingDefinition<?>>> BUILDING_DEFS = new ConcurrentHashMap<>();

	public BuildingRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	public abstract void init();

	protected <T extends GameObject & PlaceableObject> void register(
			final BuildingCategory cate,
			final Class<T> clazz,
			final int price,
			final List<BuildingRequirement> unlock,
			final List<BuildingRequirement> building,
			final int index) {
		this.register(cate, new BuildingDefinition<>(clazz, this.getInternalName(clazz), new HashMap<ResourceType, Integer>() {
			{
				this.put(DefaultResourceType.MONEY, price);
			}
		}, unlock, building, index));
	}

	protected <T extends GameObject & PlaceableObject> String getInternalName(final Class<T> clazz) {
		if (!GameObjectRegistry.DATA_PATH.containsKey(clazz)) {
			throw new IllegalArgumentException("Class: " + clazz + " not registered in " + GameObjectRegistry.class.getSimpleName());
		}
		System.err.println(GameObjectRegistry.DATA_PATH.get(clazz));
		return this.pluginDescriptor.getInternalName() + ":" + PCUtils.getFileName(GameObjectRegistry.DATA_PATH.get(clazz));
	}

	protected <T extends GameObject & PlaceableObject> void register(
			final BuildingCategory cate,
			final Class<T> clazz,
			final Map<ResourceType, Integer> prices,
			final List<BuildingRequirement> unlock,
			final List<BuildingRequirement> building,
			final int index) {
		this.register(cate, new BuildingDefinition<>(clazz, this.getInternalName(clazz), prices, unlock, building, index));
	}

	protected void register(final BuildingCategory category, final BuildingDefinition<?> def) {
		BUILDING_DEFS.computeIfAbsent(category, c -> Collections.synchronizedList(new ArrayList<>()));
		BUILDING_DEFS.get(category).add(def);
	}

	public static String getInternalObjectName(final Class<?> clazz) {
		return clazz.getName();
	}

}
