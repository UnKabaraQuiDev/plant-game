package lu.kbra.plant_game.engine.entity.ui.text;

import lu.kbra.standalone.gameengine.objs.text.TextEmitter;

@Deprecated
public class PercentageSignedIntTextUIObject extends SignedIntegerTextUIObject {

	@Deprecated
	public PercentageSignedIntTextUIObject(final String str, final TextEmitter text) {
		super(str, text);

	}

	@Deprecated
	@Override
	public void postConstruct() {
		super.paddingLength--;
		super.postConstruct();
	}

	@Deprecated
	@Override
	public String buildText() {
		return super.buildText() + "%";
	}

}
