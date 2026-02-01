package lu.kbra.plant_game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectNotFound;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public abstract class UIObjectRegistry {

	public static final Map<Class<? extends UIObject>, List<InternalConstructorFunction<UIObject>>> UI_OBJECT_CONSTRUCTORS;
	public static final Map<Class<? extends UIObject>, String> DATA_PATH;
	public static final Map<Class<? extends UIObject>, Integer> BUFFER_SIZE;
	public static final Map<Class<? extends UIObject>, TextureFilter> TEXTURE_FILTER;
	public static final Map<Class<? extends UIObject>, TextureWrap> TEXTURE_WRAP;

	static {
		UI_OBJECT_CONSTRUCTORS = new HashMap<>();
		DATA_PATH = new HashMap<>();
		BUFFER_SIZE = new HashMap<>();
		TEXTURE_FILTER = new HashMap<>();
		TEXTURE_WRAP = new HashMap<>();
	}
	protected PluginDescriptor pluginDescriptor;

	public UIObjectRegistry(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	public abstract void init();

	@SuppressWarnings("unchecked")
	public static <T extends UIObject> T create(final Class<T> clazz, final Object... args) {
		return (T) get(clazz, args).apply(args);
	}

	public static <T extends UIObject> InternalConstructorFunction<UIObject> get(final Class<T> clazz, final Object... args) {
		if (!UI_OBJECT_CONSTRUCTORS.containsKey(clazz)) {
			throw new UIObjectNotFound(clazz, args);
		}
		final Optional<InternalConstructorFunction<UIObject>> bestConstructor = UI_OBJECT_CONSTRUCTORS.get(clazz)
				.parallelStream()
				.filter((v) -> v.matches(args))
				.findFirst();
		if (bestConstructor.isPresent()) {
			return bestConstructor.get();
		}
		throw new UIObjectConstructorNotFound(clazz, args);
	}
}
