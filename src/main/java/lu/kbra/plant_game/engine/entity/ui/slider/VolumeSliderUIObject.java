package lu.kbra.plant_game.engine.entity.ui.slider;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("localization:slider.volume")
public class VolumeSliderUIObject extends SliderUIObject {

	public VolumeSliderUIObject(final String str, final TextEmitter text) {
		super(str, text, 0, 100, 100, 10);
	}

	public VolumeSliderUIObject(final String str, final TextEmitter text, final Transform3D transform) {
		super(str, text, transform, 0, 100, 100, 10);
	}

	public VolumeSliderUIObject(
			final String str,
			final TextEmitter text,
			final float min,
			final float max,
			final float value,
			final int divisors) {
		super(str, text, min, max, value, divisors);
	}

	public VolumeSliderUIObject(
			final String str,
			final TextEmitter text,
			final Transform3D transform,
			final float min,
			final float max,
			final float value,
			final int divisors) {
		super(str, text, transform, min, max, value, divisors);
	}

}
