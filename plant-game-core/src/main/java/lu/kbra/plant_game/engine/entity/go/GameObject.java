package lu.kbra.plant_game.engine.entity.go;

import lu.kbra.plant_game.engine.entity.impl.ObjectIdOwner;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;

public interface GameObject extends SceneEntity, Transform3DOwner, ObjectIdOwner {

}
