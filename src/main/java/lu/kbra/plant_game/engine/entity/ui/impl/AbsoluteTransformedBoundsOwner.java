package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.Shape;

import lu.kbra.standalone.gameengine.objs.entity.ParentAware;

public interface AbsoluteTransformedBoundsOwner extends TransformedBoundsOwner, ParentAware, AbsoluteTransform3DOwner {

	default Shape getTransformedWorld() {
		return this.getTransformedBounds(this.getAbsoluteTransform());
	}

}
