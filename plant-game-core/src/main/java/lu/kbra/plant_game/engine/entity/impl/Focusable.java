package lu.kbra.plant_game.engine.entity.impl;

public interface Focusable {

	boolean hasFocus();

	default void giveFocus() {
		setFocused(true);
	}

	default void removeFocus() {
		setFocused(false);
	}

	void setFocused(boolean focused);

}
