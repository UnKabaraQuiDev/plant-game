package lu.kbra.plant_game.engine.util;

import java.util.function.Function;

public final class InternalConstructorFunction<T> implements Function<Object[], T> {

	private final Class<?>[] params;
	private final BuildingOption[] options;
	private final Function<Object[], T> delegate;

	public InternalConstructorFunction(final Class<?>[] params, final BuildingOption[] options, final Function<Object[], T> delegate) {
		this.params = params;
		this.options = options;
		this.delegate = delegate;
	}

	@Override
	public T apply(final Object[] args) {
		return this.delegate.apply(args);
	}

	public Class<?>[] getParams() {
		return this.params;
	}

	public BuildingOption[] getOptions() {
		return this.options;
	}

	public boolean matches(final Object[] objs) {
		if (this.params.length != objs.length) {
			return false;
		}

		for (int i = 0; i < this.params.length; i++) {
			final Class<?> expected = wrap(this.params[i]);
			final Class<?> actual = objs[i] == null ? null : wrap(objs[i].getClass());

			if (actual == null || !expected.isAssignableFrom(actual)) {
				return false;
			}
		}
		return true;
	}

	private static Class<?> wrap(final Class<?> c) {
		if (!c.isPrimitive()) {
			return c;
		}
		if (c == int.class) {
			return Integer.class;
		}
		if (c == boolean.class) {
			return Boolean.class;
		}
		if (c == long.class) {
			return Long.class;
		}
		if (c == float.class) {
			return Float.class;
		}
		if (c == double.class) {
			return Double.class;
		}
		if (c == char.class) {
			return Character.class;
		}
		if (c == byte.class) {
			return Byte.class;
		}
		if (c == short.class) {
			return Short.class;
		}
		return c;
	}
}
