package lu.kbra.plant_game.plugin.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.GameObjectNotFound;
import lu.kbra.plant_game.plugin.PluginDescriptor;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

public abstract class GameObjectRegistry extends PluginRegistry {

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
		System.err.println("construtcors");
		GAME_OBJECT_CONSTRUCTORS.keySet().forEach(System.out::println);
		GAME_OBJECT_CONSTRUCTORS.forEach((k, v) -> GAME_OBJECT_CLASSES.put(k.getName(), k));
		// some GameObjects may not have a constructor
		DATA_PATH.forEach((k, v) -> GAME_OBJECT_CLASSES.put(k.getName(), k));
	}

	public static <T extends GameObject> Class<T> getClass(final String name) {
		System.err.println("classes");
		GAME_OBJECT_CLASSES.keySet().forEach(System.err::println);
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
		final Optional<InternalConstructorFunction<GameObject>> bestConstructor = GAME_OBJECT_CONSTRUCTORS
				.get(clazz)
				.parallelStream()
				.filter((v) -> v.matches(args))
				.findFirst();
		if (bestConstructor.isPresent()) {
			return bestConstructor.get();
		}
		throw new GameObjectConstructorNotFound(clazz, args);
	}

	@Override
	public int getPriority() {
		return 400;
	}

}
