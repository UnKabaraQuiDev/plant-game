package lu.kbra.plant_game.engine.util.annotation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Identifies a specific dependency when multiple instances of the same type
 * exist.
 */
@Documented
@Retention(RUNTIME)
@Inherited
@AutoGenProperty
@Target({ TYPE, CONSTRUCTOR, PARAMETER })
public @interface Qualifier {

	String value();

}
