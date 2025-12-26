package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("")
public class PercentageIntTextUIObject extends IntegerTextUIObject {

	public PercentageIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final ColorMaterial mat,
			final Transform3D transform) {
		super(str, text, key, mat, transform);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(final String str, final TextEmitter text, final String key, final ColorMaterial mat) {
		super(str, text, key, mat);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final boolean paddingZero,
			final int padLength,
			final ColorMaterial mat,
			final Transform3D transform) {
		super(str, text, key, value, forceSign, padding, paddingZero, padLength, mat, transform);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final boolean paddingZero,
			final int padLength,
			final ColorMaterial mat) {
		super(str, text, key, value, forceSign, padding, paddingZero, padLength, mat);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final int padLength,
			final Transform3D transform) {
		super(str, text, key, value, forceSign, padding, padLength, transform);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final boolean padding,
			final int padLength) {
		super(str, text, key, value, forceSign, padding, padLength);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final boolean forceSign,
			final Transform3D transform) {
		super(str, text, key, value, forceSign, transform);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(final String str, final TextEmitter text, final String key, final int value, final boolean forceSign) {
		super(str, text, key, value, forceSign);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final int value,
			final Transform3D transform) {
		super(str, text, key, value, transform);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(final String str, final TextEmitter text, final String key, final int value) {
		super(str, text, key, value);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(final String str, final TextEmitter text, final String key, final Transform3D transform) {
		super(str, text, key, transform);
		super.paddingLength--;
	}

	public PercentageIntTextUIObject(final String str, final TextEmitter text, final String key) {
		super(str, text, key);
		super.paddingLength--;
	}

	@Override
	public String buildText() {
		return super.buildText() + "%";
	}

}
