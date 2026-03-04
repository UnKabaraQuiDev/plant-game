package lu.kbra.plant_game.engine.entity.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import lu.kbra.plant_game.engine.util.InternalConstructorFunction;

/**
 * Context key passed down during resolution.
 * <p>
 * It resolves annotation properties from the smallest to the largest scope:
 * parameter -> constructor -> class.
 */
public final class ResolutionKey {

	private final Class<?> targetClass;
	private final InternalConstructorFunction<?> ctor;
	private final int paramIndex;

	public ResolutionKey(final Class<?> targetClass, final InternalConstructorFunction<?> ctor, final int paramIndex) {
		this.targetClass = targetClass;
		this.ctor = ctor;
		this.paramIndex = paramIndex;
	}

	public Class<?> targetClass() {
		return this.targetClass;
	}

	public int paramIndex() {
		return this.paramIndex;
	}

	public Constructor<?> constructor() {
		return this.ctor.getConstructor();
	}

	public <T extends Annotation> T getProperty(final Class<T> annotationType) {
		// 1) parameter scope
		if (this.paramIndex >= 0) {
			final Parameter[] params = this.constructor().getParameters();
			if (this.paramIndex < params.length) {
				final T a = params[this.paramIndex].getAnnotation(annotationType);
				if (a != null) {
					return a;
				}
			}
		}
		// 2) constructor scope
		final T b = this.constructor().getAnnotation(annotationType);
		if (b != null) {
			return b;
		}
		// 3) class scope
		return this.targetClass.getAnnotation(annotationType);
	}
}
