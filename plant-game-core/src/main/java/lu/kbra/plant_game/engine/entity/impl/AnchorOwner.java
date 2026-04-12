package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;

public interface AnchorOwner {

	Anchor getObjectAnchor();

	void setObjectAnchor(Anchor a);

	Anchor getTargetAnchor();

	void setTargetAnchor(Anchor a);

	default void setAnchors(final Anchor obj, final Anchor tar) {
		this.setObjectAnchor(obj);
		this.setTargetAnchor(tar);
	}

	default boolean isAnchored() {
		return this.getTargetAnchor() != null && this.getObjectAnchor() != null;
	}

}
