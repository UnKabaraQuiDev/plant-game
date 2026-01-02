package lu.kbra.plant_game.engine.scene.ui.overlay;

import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;

public interface AnchorOwner {

	Anchor getObjectAnchor();

	void setObjectAnchor(Anchor a);

	Anchor getTargetAnchor();

	void setTargetAnchor(Anchor a);

	default boolean isAnchored() {
		return this.getTargetAnchor() != null && this.getObjectAnchor() != null;
	}

}
