package lu.kbra.plant_game.engine.entity.ui;

import java.awt.Shape;

import lu.kbra.plant_game.engine.entity.impl.UIObject;
import lu.kbra.plant_game.engine.entity.impl.WindowInputHandler;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("classpath:/icons/money-32.png")
public class MoneyUIObject extends UIObject {

	public MoneyUIObject(String str, Mesh mesh) {
		super(str, mesh);
	}

	public MoneyUIObject(String str, Mesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

	@Override
	public void hover(WindowInputHandler input, float dTime) {
	}

	@Override
	public void click(WindowInputHandler input, float dTime) {
	}

	@Override
	public Shape getBounds() {
		return UIObject.SQUARE_1_UNIT;
	}

}
