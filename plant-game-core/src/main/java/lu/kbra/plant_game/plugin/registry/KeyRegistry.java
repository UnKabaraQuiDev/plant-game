package lu.kbra.plant_game.plugin.registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lu.kbra.plant_game.engine.data.locale.Localizable;
import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.plugin.PluginDescriptor;

public abstract class KeyRegistry extends PluginRegistry {

	public static final List<KeyOption> KEYS = new ArrayList<>();
	public static final Map<KeyOption, PluginDescriptor> KEYS_PLUGINS = new HashMap<>();
	public static final Map<KeyOption, String> KEYS_NAMES = new HashMap<>();
	public static final Map<String, KeyOption> KEYS_DEFS = new HashMap<>();

	public KeyRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	protected void register(final KeyOption k) {
		final String name = this.getInternalName_(k);
		KEYS.add(k);
		KEYS_PLUGINS.put(k, this.pluginDescriptor);
		KEYS_NAMES.put(k, name);
		KEYS_DEFS.put(name, k);
	}

	@Override
	public void postConstruct() {
		KEYS.sort(Comparator.comparing((final KeyOption k) -> KEYS_PLUGINS.get(k).getInternalName()).thenComparingInt(KeyOption::getIndex));
	}

	private String getInternalName_(final KeyOption k) {
		return this.pluginDescriptor.getInternalName() + ":" + k.name().toLowerCase();
	}

	public static String getInternalName(final KeyOption k) {
		return KEYS_NAMES.get(k);
	}

	public static <T extends KeyOption> T getKeyOption(final String k) {
		return (T) KEYS_DEFS.get(k);
	}

	public static PluginDescriptor getPluginDescriptor(final KeyOption k) {
		return KEYS_PLUGINS.get(k);
	}

	public static String getLocalizationKey(final KeyOption keyOption) {
		return KeyOption.LOCALIZATION_KEY + getInternalName(keyOption).replaceAll("[:/ ]", ".");
	}

	public static Localizable getLocalization(final KeyOption keyOption) {
		return Localizable.of(getLocalizationKey(keyOption));
	}

	@Override
	public int getPriority() {
		return 100;
	}

}
