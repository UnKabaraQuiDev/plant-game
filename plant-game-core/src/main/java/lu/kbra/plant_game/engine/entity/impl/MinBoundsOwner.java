package lu.kbra.plant_game.engine.entity.impl;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public interface MinBoundsOwner extends TransformedBoundsOwner {

	Rectangle2D getMinBounds();

	default Shape getTransformedMinBounds() {
		return this.getTransformedBounds(this.getMinBounds());
	}

}
