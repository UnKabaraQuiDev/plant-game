package lu.kbra.plant_game.engine.entity.go;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import lu.kbra.plant_game.engine.entity.impl.ObjectIdOwner;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.objs.entity.SceneEntity;

public interface GameObject extends SceneEntity, Transform3DOwner, ObjectIdOwner {

	int MESH_ATTRIB_MATERIAL_ID_ID = 3;
	int MESH_ATTRIB_OBJECT_ID_ID = 4;
	String MESH_ATTRIB_MATERIAL_ID_NAME = "materialId";
	String MESH_ATTRIB_OBJECT_ID_NAME = "objectId";

	static Vector3ic getRandomObjectId() {
		return new Vector3i((int) System.nanoTime(), (int) System.nanoTime() % 20056, (int) System.nanoTime() % 255);
	}

}
