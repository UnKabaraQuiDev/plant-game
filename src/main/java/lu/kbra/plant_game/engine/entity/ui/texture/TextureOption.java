package lu.kbra.plant_game.engine.entity.ui.texture;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface TextureOption {

	TextureFilter textureFilter() default TextureFilter.NEAREST;

	TextureWrap textureWrap() default TextureWrap.REPEAT;

}
