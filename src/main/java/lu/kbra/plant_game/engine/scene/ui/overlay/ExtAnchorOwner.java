package lu.kbra.plant_game.engine.scene.ui.overlay;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;

public interface ExtAnchorOwner extends AnchorOwner {

	UIObject getTarget();

	void setTarget(UIObject target);

	default void setTarget(final UIObject target, final Anchor obj, final Anchor tar) {
		this.setTarget(target);
		this.setObjectAnchor(obj);
		this.setTargetAnchor(tar);
	}

}
