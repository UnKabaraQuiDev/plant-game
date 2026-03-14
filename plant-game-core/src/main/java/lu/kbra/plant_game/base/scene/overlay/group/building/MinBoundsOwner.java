package lu.kbra.plant_game.base.scene.overlay.group.building;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.ui.impl.TransformedBoundsOwner;

public interface MinBoundsOwner extends TransformedBoundsOwner {

	Rectangle2D getMinBounds();

	default Shape getTransformedMinBounds() {
		return this.getTransformedBounds(this.getMinBounds());
	}

}
