package lu.kbra.plant_game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BuildingDefinitionRegistry {

	public static final Map<BuildingCategory, List<BuildingDefinition<?>>> BUILDING_DEFS = new ConcurrentHashMap<>();

	public static void register(final BuildingCategory category, final BuildingDefinition<?> def) {
		BUILDING_DEFS.computeIfAbsent(category, c -> Collections.synchronizedList(new ArrayList<>()));
		BUILDING_DEFS.get(category).add(def);
	}

	public static String getInternalObjectName(final Class<?> clazz) {
		return clazz.getName();
	}

}
