package lu.kbra.plant_game.engine.entity.go.factory;

import static lu.kbra.plant_game.plugin.registry.GameObjectRegistry.BUFFER_SIZE;
import static lu.kbra.plant_game.plugin.registry.GameObjectRegistry.DATA_PATH;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import lu.kbra.plant_game.engine.entity.factory.ObjectFactory;
import lu.kbra.plant_game.engine.entity.factory.ResolveContext;
import lu.kbra.plant_game.engine.entity.factory.Resolver;
import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.impl.InstanceEmitterOwner;
import lu.kbra.plant_game.engine.loader.StaticInstanceLoader;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.geom.instance.InstanceEmitter;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.transform.Transform;

public class GameObjectFactory {

	public static final int DEFAULT_BUFFER_SIZE = 12;

	public static GameObjectFactory INSTANCE;

	private final CacheManager cache;
	private final Dispatcher loader;
	private final Dispatcher render;

	public GameObjectFactory(final CacheManager cache, final Dispatcher loader, final Dispatcher render) {
		this.cache = cache;
		this.loader = loader;
		this.render = render;
		// configure DI resolver
		ObjectFactory.setResolver(new Resolver(new ResolveContext(cache, loader, render)));
	}

	public <T extends GameObject & InstanceEmitterOwner> GOCreatingTaskFuture<T> createInstances_(final Class<T> clazz,
			final IntFunction<Transform> transforms, final OptionalInt bufferSize, final Optional<String> name,
			final Supplier<AttribArray>... attribs) {

		return StaticInstanceLoader.getFuture(this.cache, name.orElse(clazz.getSimpleName()), DATA_PATH.get(clazz),
				bufferSize.orElseGet(() -> {
					if (!BUFFER_SIZE.containsKey(clazz)) {
						throw new IllegalArgumentException(
								"Class: " + clazz.getName() + " defines no default buffer size.");
					}
					return BUFFER_SIZE.get(clazz);
				}), transforms, attribs, this.loader, this.render)
				.then(this.loader,
						(Function<InstanceEmitter, T>) emitter -> ObjectFactory.create(clazz)
								.with(InstanceEmitter.class, emitter).push().join())
				.then(new GOCreatingTaskFuture<>(this.loader, clazz));
	}

	public static <T extends GameObject> GOCreatingTaskFuture<T> create(final Class<T> clazz) {
		return ObjectFactory.create(clazz).then(new GOCreatingTaskFuture<>(INSTANCE.loader, clazz));
	}

	@SafeVarargs
	public static <T extends GameObject & InstanceEmitterOwner> GOCreatingTaskFuture<T> createInstances(
			final Class<T> clazz, final IntFunction<Transform> transforms, final OptionalInt bufferSize,
			final Optional<String> name, final Supplier<AttribArray>... attribs) {
		return INSTANCE.createInstances_(clazz, transforms, bufferSize, name, attribs);
	}

	// manual creation can be done through
	// ObjectFactory.create(clazz).with(Mesh.class, mesh)

}
