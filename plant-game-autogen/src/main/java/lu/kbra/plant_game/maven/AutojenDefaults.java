package lu.kbra.plant_game.maven;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface AutojenDefaults {

	default Enum<?> getEnumValue(Annotation annotation, String method) {
		try {
			final Class<?> clazz = annotation.getClass();
			final Method meth = clazz.getMethod(method);
			return (Enum<?>) meth.invoke(annotation);
		} catch (IllegalAccessException | NoSuchMethodException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	default int getIntValue(Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (int) meth.invoke(annotation);
	}

	default String getStringValue(Annotation annotation)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?> clazz = annotation.getClass();
		final Method meth = clazz.getMethod("value");
		return (String) meth.invoke(annotation);
	}

}
