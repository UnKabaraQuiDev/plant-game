package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.geom.Rectangle2D;

import javax.swing.GroupLayout.Alignment;

import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TextureUIObject extends UIObject {

	private final Rectangle2D.Float bounds;

	public TextureUIObject(String str, TexturedQuadMesh mesh) {
		super(str, mesh);
		bounds = GameEngineUtils.toRectangleBounds(mesh.getSize(), Alignment.CENTER, Alignment.CENTER);
	}

	public TextureUIObject(String str, TexturedQuadMesh mesh, Transform3D transform) {
		super(str, mesh, transform);
		bounds = GameEngineUtils.toRectangleBounds(mesh.getSize(), Alignment.CENTER, Alignment.CENTER);
	}

	@Override
	public Rectangle2D.Float getBounds() {
		return bounds;
	}

}
