package lu.kbra.plant_game.engine.entity.impl;

import java.awt.Shape;

public interface BoundsOwner {

	Shape getBounds();

	default boolean hasBounds() {
		return this.getBounds() != null;
	}

	default boolean areBoundsValid() {
		return this.hasBounds() && this.getBounds().getBounds2D().getWidth() > 0 && this.getBounds().getBounds2D().getHeight() > 0;
	}

}
