package lu.kbra.plant_game.plugin.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lu.kbra.pclib.PCUtils;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.annotation.Named;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public abstract class GameObjectRegistry extends PluginRegistry {

	/* PLACEABLE OBJECTS ONLY */
	public static final Map<Class<? extends GameObject>, String> GAME_OBJECT_BY_NAME = new HashMap<>();
	public static final Map<String, Class<? extends GameObject>> GAME_OBJECT_NAMES = new HashMap<>();

	public static final Map<String, Class<? extends GameObject>> GAME_OBJECT_CLASSES = new HashMap<>();
	public static final Map<Class<? extends GameObject>, List<InternalConstructorFunction<GameObject>>> GAME_OBJECT_CONSTRUCTORS = new HashMap<>();
	public static final Map<Class<? extends GameObject>, String> DATA_PATH = new HashMap<>();
	public static final Map<Class<? extends GameObject>, Integer> BUFFER_SIZE = new HashMap<>();
	public static final Map<Class<? extends GameObject>, TextureFilter> TEXTURE_FILTER = new HashMap<>();
	public static final Map<Class<? extends GameObject>, TextureWrap> TEXTURE_WRAP = new HashMap<>();

	public GameObjectRegistry(final PluginDescriptor pluginDescriptor) {
		super(pluginDescriptor);
	}

	@Override
	public void postConstruct() {
		GAME_OBJECT_CONSTRUCTORS.forEach((k, v) -> {
			GAME_OBJECT_CLASSES.put(k.getName(), k);

//			if (!PlaceableObject.class.isAssignableFrom(k)) {
//				return;
//			}
			final String internalName = this.computeInternalName((Class) k);
			if (internalName != null) {
				GAME_OBJECT_BY_NAME.put(k, internalName);
				GAME_OBJECT_NAMES.put(internalName, k);
			}
		});
//		DATA_PATH.forEach((k, v) -> GAME_OBJECT_CLASSES.put(k.getName(), k));
	}

	protected final <T extends GameObject> String computeInternalName(final Class<T> clazz) {
		if (clazz.isAnnotationPresent(Named.class)) {
			return this.pluginDescriptor.getInternalName() + ":" + clazz.getAnnotation(Named.class).value().toLowerCase();
		}
		if (!GameObjectRegistry.DATA_PATH.containsKey(clazz)) {
			return null;
		}
		return this.pluginDescriptor.getInternalName() + ":" + PCUtils.getFileName(GameObjectRegistry.DATA_PATH.get(clazz));
	}

	public static <T extends GameObject> Class<T> findClass(final String name) {
		return (Class<T>) GAME_OBJECT_CLASSES.get(name);
	}

	@SuppressWarnings("unchecked")
	public static <T extends GameObject> T create(final Class<T> clazz, final Object... args) {
		return (T) get(clazz, args).apply(args);
	}

	public static <T extends GameObject> InternalConstructorFunction<GameObject> get(final Class<T> clazz, final Object... args) {
		if (!GAME_OBJECT_CONSTRUCTORS.containsKey(clazz)) {
			throw new GameObjectNotFound(clazz, args);
		}
		final Optional<InternalConstructorFunction<GameObject>> bestConstructor = GAME_OBJECT_CONSTRUCTORS.get(clazz)
				.parallelStream()
				.filter((v) -> v.matches(args))
				.findFirst();
		if (bestConstructor.isPresent()) {
			return bestConstructor.get();
		}
		throw new GameObjectConstructorNotFound(clazz, args);
	}

	public static <T extends GameObject> String getInternalName(final Class<T> clazz) {
		return GAME_OBJECT_BY_NAME.get(clazz);
	}

	public static <T extends GameObject> Class<T> findInternalName(final String name) {
		return (Class<T>) GAME_OBJECT_NAMES.get(name);
	}

	@Override
	public int getPriority() {
		return 400;
	}

}
