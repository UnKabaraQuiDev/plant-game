package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

public class PercentageSignedIntTextUIObject extends SignedIntegerTextUIObject {

	public PercentageSignedIntTextUIObject(final String str, final TextEmitter text) {
		super(str, text);

	}

	@Override
	public void init() {
		super.paddingLength--;
		super.init();
	}

	@Override
	public String buildText() {
		return super.buildText() + "%";
	}

}
