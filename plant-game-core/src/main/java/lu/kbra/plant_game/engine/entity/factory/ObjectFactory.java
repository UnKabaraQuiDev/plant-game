package lu.kbra.plant_game.engine.entity.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lu.kbra.plant_game.engine.entity.go.GameObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.annotation.Qualifier;
import lu.kbra.plant_game.plugin.registry.GameObjectRegistry;
import lu.kbra.plant_game.plugin.registry.UIObjectRegistry;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;

/**
 * Entry point for DI-style object creation.
 */
public final class ObjectFactory {

	private static Resolver RESOLVER;

	public static void setResolver(final Resolver resolver) {
		RESOLVER = Objects.requireNonNull(resolver);
	}

	public static Resolver resolver() {
		if (RESOLVER == null) {
			throw new IllegalStateException("ObjectFactory resolver not configured.");
		}
		return RESOLVER;
	}

	public static <T> CreatingTaskFuture<T> create(final Class<T> clazz) {
		return new CreatingTaskFuture<>(resolver().context().loader(), clazz, resolver());
	}

	public static final class CreatingTaskFuture<T> extends TaskFuture<Void, T> {

		private final Class<T> clazz;
		private final Resolver resolver;
		private final Map<OverrideKey, Object> overrides = new HashMap<>();

		public CreatingTaskFuture(final Dispatcher dispatcher, final Class<T> clazz, final Resolver resolver) {
			super(dispatcher);
			this.clazz = Objects.requireNonNull(clazz);
			this.resolver = Objects.requireNonNull(resolver);
			super.task = (v) -> this.build();
		}

		public CreatingTaskFuture<T> with(final Class<?> type, final Object value) {
			return this.with(type, "", value);
		}

		public CreatingTaskFuture<T> with(final Class<?> type, final String qualifier, final Object value) {
			this.overrides.put(new OverrideKey(type, qualifier), value);
			return this;
		}

		private T build() {
			// resolve via registry constructors
			final InternalConstructorFunction<?> icf = this.pickConstructor();
			final Class<?>[] params = icf.getParams();
			final Object[] args = new Object[params.length];

			for (int i = 0; i < params.length; i++) {
				final Class<?> pType = params[i];
				final ResolutionKey rKey = new ResolutionKey(this.clazz, icf, i);
				args[i] = this.resolveArgument(pType, rKey);
			}

			@SuppressWarnings("unchecked")
			final T instance = (T) icf.apply(args);
			return instance;
		}

		private Object resolveArgument(final Class<?> pType, final ResolutionKey rKey) {
			// allow common runtime values
			final Qualifier q = rKey.getProperty(Qualifier.class);
			final String qualifier = q == null ? "" : q.value();

			final Object override = this.overrides.get(new OverrideKey(pType, qualifier));
			if (override != null) {
				return override;
			}

			// default name for String if nothing provided
			if (pType == String.class) {
				return this.clazz.getSimpleName() + "#" + System.nanoTime();
			}

			// provider resolution (blocking join)
			final DIKey key = new DIKey(pType, qualifier, "");
			final TaskFuture<?, ?> fut = this.resolver.resolveFuture(key, rKey);
			return fut.push().join();
		}

		private InternalConstructorFunction<?> pickConstructor() {
			if (GameObject.class.isAssignableFrom(this.clazz)) {
				return GameObjectRegistry.GAME_OBJECT_CONSTRUCTORS.get(this.clazz).stream()
						.max((a, b) -> Integer.compare(a.getParams().length, b.getParams().length)).orElseThrow();
			}
			if (UIObject.class.isAssignableFrom(this.clazz)) {
				return UIObjectRegistry.UI_OBJECT_CONSTRUCTORS.get(this.clazz).stream()
						.max((a, b) -> Integer.compare(a.getParams().length, b.getParams().length)).orElseThrow();
			}
			throw new UnsupportedOperationException("No registry for: " + this.clazz.getName());
		}
	}

	private record OverrideKey(Class<?> type, String qualifier) {
		OverrideKey {
			qualifier = qualifier == null ? "" : qualifier;
		}
	}
}
