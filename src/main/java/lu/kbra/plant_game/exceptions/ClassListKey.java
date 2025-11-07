package lu.kbra.plant_game.exceptions;

import java.util.Arrays;

public class ClassListKey {

	private final Class<?>[] classes;

	public ClassListKey(Class<?>... classes) {
		this.classes = classes.clone();
	}

	public Class<?>[] getClasses() {
		return classes.clone();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClassListKey other)) {
			return false;
		}
		if (classes.length != other.classes.length) {
			return false;
		}

		for (int i = 0; i < classes.length; i++) {
			if (!classes[i].isAssignableFrom(other.classes[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(Arrays.stream(classes).map(Class::hashCode).toArray());
	}

}
