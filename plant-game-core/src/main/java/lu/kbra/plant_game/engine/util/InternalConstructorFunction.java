package lu.kbra.plant_game.engine.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Internal wrapper around a public constructor.
 * <p>
 * The constructor reference is kept so runtime systems can inspect
 * parameter/constructor/class annotations at runtime.
 */
public final class InternalConstructorFunction<T> implements Function<Object[], T> {

	private final Constructor<?> constructor;
	private final Class<?>[] params;
	private final Function<Object[], T> delegate;

	public InternalConstructorFunction(final Constructor<?> constructor, final Class<?>[] params, final Function<Object[], T> delegate) {
		this.constructor = constructor;
		this.params = params;
		this.delegate = delegate;
	}

	@Override
	public T apply(final Object[] t) {
		return this.delegate.apply(t);
	}

	public Class<?>[] getParams() {
		return this.params;
	}

	public Constructor<?> getConstructor() {
		return this.constructor;
	}

	public Function<Object[], T> getDelegate() {
		return this.delegate;
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

	@Override
	public String toString() {
		return "InternalConstructorFunction [constructor=" + this.constructor + ", params=" + Arrays.toString(this.params) + ", delegate="
				+ this.delegate + "]";
	}

}