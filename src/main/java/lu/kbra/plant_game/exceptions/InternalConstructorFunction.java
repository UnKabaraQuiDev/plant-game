package lu.kbra.plant_game.exceptions;

import java.util.function.Function;

public class InternalConstructorFunction<T> implements Function<Object[], T> {

	private final Class<?>[] params;
	private final Function<Object[], T> delegate;

	public InternalConstructorFunction(Class<?>[] params, Function<Object[], T> delegate) {
		this.params = params;
		this.delegate = delegate;
	}

	@Override
	public T apply(Object[] t) {
		return delegate.apply(t);
	}

	public Class<?>[] getParams() {
		return params;
	}

	public Function<Object[], T> getDelegate() {
		return delegate;
	}

	public boolean matches(Object[] objs) {
		if (this.params.length != objs.length) {
			return false;
		}
		for (int i = 0; i < params.length; i++) {
			if (!params[i].isAssignableFrom(objs[i].getClass())) {
				return false;
			}
		}
		return true;
	}

}