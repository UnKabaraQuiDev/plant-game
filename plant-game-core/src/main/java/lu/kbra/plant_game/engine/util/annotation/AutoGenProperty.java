package lu.kbra.plant_game.engine.util.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Meta-annotation.
 * <p>
 * An annotation marked with {@link AutoGenProperty} is treated as a property
 * that can be resolved by the object creation / DI system.
 */
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface AutoGenProperty {
}
