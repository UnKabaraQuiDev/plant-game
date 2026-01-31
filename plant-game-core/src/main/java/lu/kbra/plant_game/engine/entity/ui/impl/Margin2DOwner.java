package lu.kbra.plant_game.engine.entity.ui.impl;

public interface Margin2DOwner {

	void setMarginX(float f);

	void setMarginZ(float f);

	float getMarginX();

	float getMarginZ();

	default float[] getMargins() {
		return new float[] { this.getMarginX(), this.getMarginZ() };
	}

}
