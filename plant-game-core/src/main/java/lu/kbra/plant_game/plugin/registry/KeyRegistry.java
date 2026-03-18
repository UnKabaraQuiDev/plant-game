package lu.kbra.plant_game.plugin.registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class KeyRegistry extends PluginRegistry {

	public static final List<KeyOption> KEYS = new ArrayList<>();
	public static final Map<KeyOption, String> KEYS_NAMES = new HashMap<>();
	public static final Map<String, KeyOption> KEYS_DEFS = new HashMap<>();

	public KeyRegistry(PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	protected void register(KeyOption k) {
		final String name = getInternalName_(k);
		KEYS.add(k);
		KEYS_NAMES.put(k, name);
		KEYS_DEFS.put(name, k);
	}

	@Override
	public void postInit() {
		KEYS.sort(Comparator.comparing((KeyOption k) -> KEYS_NAMES.get(k).split(":")[0]).thenComparingInt(KeyOption::getIndex));
	}

	private String getInternalName_(KeyOption k) {
		return pluginDescriptor.getInternalName() + ":" + k.name().toLowerCase();
	}

	public static String getInternalName(KeyOption k) {
		return KEYS_NAMES.get(k);
	}

	public static <T extends KeyOption> T getKeyOption(String k) {
		return (T) KEYS_DEFS.get(k);
	}

}
