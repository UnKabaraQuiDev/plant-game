package lu.kbra.plant_game;

import lu.kbra.plant_game.engine.entity.impl.UIObject;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/models/plane.json")
public class IconUIObject extends UIObject {

	public IconUIObject(String str, Mesh mesh) {
		super(str, mesh);
	}

	public IconUIObject(String str, Mesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

}
