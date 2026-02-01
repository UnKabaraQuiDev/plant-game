package lu.kbra.plant_game;

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

public abstract class GameObjectRegistry {

	public static final Map<Class<? extends GameObject>, List<InternalConstructorFunction<GameObject>>> GAME_OBJECT_CONSTRUCTORS;
	public static final Map<Class<? extends GameObject>, String> DATA_PATH;
	public static final Map<Class<? extends GameObject>, Integer> BUFFER_SIZE;
	public static final Map<Class<? extends GameObject>, TextureFilter> TEXTURE_FILTER;
	public static final Map<Class<? extends GameObject>, TextureWrap> TEXTURE_WRAP;
//	public static final Map<Class<? extends GameObject>, Integer> DEFAULT_PRICE;

	static {
		GAME_OBJECT_CONSTRUCTORS = new HashMap<>();
		DATA_PATH = new HashMap<>();
		BUFFER_SIZE = new HashMap<>();
		TEXTURE_FILTER = new HashMap<>();
		TEXTURE_WRAP = new HashMap<>();
//		DEFAULT_PRICE = new HashMap<>();
	}

	protected PluginDescriptor pluginDescriptor;

	public GameObjectRegistry(final PluginDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

	public abstract void init();

	public PluginDescriptor getPluginDescriptor() {
		return this.pluginDescriptor;
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

}
