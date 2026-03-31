package lu.kbra.plant_game.plugin.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingCategory;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingDefinition;
import lu.kbra.plant_game.engine.scene.world.data.building.requirement.BuildingRequirement;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class BuildingRegistry extends PluginRegistry {

	public static final Map<Class<? extends GameObject>, String> BUILDING_NAMES = new ConcurrentHashMap<>();
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
		this.register(cate, new BuildingDefinition<>(clazz, this.getInternalName_(clazz), new HashMap<ResourceType, Integer>() {
			{
				this.put(DefaultResourceType.MONEY, price);
			}
		}, unlock, building, index));
	}

	protected final <T extends GameObject & PlaceableObject> String getInternalName_(final Class<T> clazz) {
		if (!GameObjectRegistry.DATA_PATH.containsKey(clazz)) {
			throw new IllegalArgumentException("Class: " + clazz + " not registered in " + GameObjectRegistry.class.getSimpleName());
		}
		return this.pluginDescriptor.getInternalName() + ":" + PCUtils.getFileName(GameObjectRegistry.DATA_PATH.get(clazz));
	}

	protected <T extends GameObject & PlaceableObject> void register(
			final BuildingCategory cate,
			final Class<T> clazz,
			final Map<ResourceType, Integer> prices,
			final List<BuildingRequirement> unlock,
			final List<BuildingRequirement> building,
			final int index) {
		this.register(cate, new BuildingDefinition<>(clazz, this.getInternalName_(clazz), prices, unlock, building, index));
	}

	protected void register(final BuildingCategory category, final BuildingDefinition<?> def) {
		BUILDING_DEFS.computeIfAbsent(category, c -> Collections.synchronizedList(new ArrayList<>()));
		BUILDING_DEFS.get(category).add(def);
		if (BUILDING_NAMES.containsKey(def.getClazz())) {
			throw new IllegalArgumentException(
					"Class: " + def.getClazz() + " was already registered under: " + BUILDING_NAMES.get(def.getClazz()));
		}
		BUILDING_NAMES.put(def.getClazz(), def.getInternalName());
	}

	public static String getInternalName(final Class<? extends GameObject> clazz) {
		if (!BUILDING_NAMES.containsKey(clazz)) {
			throw new NoSuchElementException("Class: " + clazz + " isn't registered.");
		}
		return BUILDING_NAMES.get(clazz);
	}

	@Override
	public int getPriority() {
		return 500;
	}

}
