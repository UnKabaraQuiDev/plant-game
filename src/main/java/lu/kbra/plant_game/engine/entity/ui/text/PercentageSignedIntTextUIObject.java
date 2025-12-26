package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("")
public class PercentageSignedIntTextUIObject extends SignedIntegerTextUIObject {

	public PercentageSignedIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final ColorMaterial neutralColor,
			final ColorMaterial negativeColor,
			final ColorMaterial positiveColor,
			final Transform3D transform) {
		super(str, text, key, neutralColor, negativeColor, positiveColor, transform);
		super.paddingLength--;
	}

	public PercentageSignedIntTextUIObject(
			final String str,
			final TextEmitter text,
			final String key,
			final ColorMaterial neutralColor,
			final ColorMaterial negativeColor,
			final ColorMaterial positiveColor) {
		super(str, text, key, neutralColor, negativeColor, positiveColor);
		super.paddingLength--;
	}

	@Override
	public String buildText() {
		return super.buildText() + "%";
	}

}
