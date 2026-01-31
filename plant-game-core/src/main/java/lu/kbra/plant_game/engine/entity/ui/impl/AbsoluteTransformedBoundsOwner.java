package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.Shape;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;

public interface AbsoluteTransformedBoundsOwner extends TransformedBoundsOwner, ParentAwareNode, AbsoluteTransform3DOwner {

	default Shape getAbsoluteTransformedBounds() {
		return this.getTransformedBounds(this.getAbsoluteTransform());
	}

	static <T extends Transform3DOwner & BoundsOwner & ParentAwareNode> Shape getAbsoluteTransformedBounds(final T entity) {
		return TransformedBoundsOwner.getTransformedBounds(entity.getBounds(), AbsoluteTransform3DOwner.getAbsoluteTransform(entity));
	}

}
