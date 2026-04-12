package lu.kbra.plant_game.engine.entity.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.entity.impl.TransformedBoundsOwner;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;

public interface UIObject extends SceneEntity, Transform3DOwner, TransformedBoundsOwner, ParentAwareNode {

	Shape SQUARE_1_UNIT = new Rectangle2D.Float(-0.5f, -0.5f, 1f, 1f);

}
