package lu.kbra.plant_game.engine.entity.ui.prim;

import java.awt.geom.Rectangle2D;

import javax.swing.GroupLayout.Alignment;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class QuadUIObject extends UIObject {

	protected Rectangle2D.Float bounds;

	public QuadUIObject(String str, QuadMesh mesh) {
		this(str, mesh, null);
	}

	public QuadUIObject(String str, QuadMesh mesh, Transform3D transform) {
		super(str, mesh, transform);
		if (mesh != null) {
			bounds = GameEngineUtils.toRectangleBounds(mesh.getSize(), Alignment.CENTER, Alignment.CENTER);
		}
	}

	@Override
	public Rectangle2D.Float getBounds() {
		return bounds;
	}

}
