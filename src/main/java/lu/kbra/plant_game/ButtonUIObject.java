package lu.kbra.plant_game;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.impl.UIObject;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/icons/water-32.png")
public class ButtonUIObject extends UIObject {

	public ButtonUIObject(String str, Mesh mesh) {
		super(str, mesh);
	}

	public ButtonUIObject(String str, Mesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

	@Override
	public Shape getBounds() {
		return new Rectangle2D.Float(-0.25f, -0.25f, 0.5f, 0.5f);
	}

}
