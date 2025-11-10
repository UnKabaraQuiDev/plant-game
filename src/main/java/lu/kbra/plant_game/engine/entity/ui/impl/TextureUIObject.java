package lu.kbra.plant_game.engine.entity.ui.impl;

import lu.kbra.plant_game.engine.mesh.TexturedQuadLoadedMesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TextureUIObject extends QuadUIObject {

	public TextureUIObject(String str, TexturedQuadLoadedMesh mesh) {
		super(str, mesh);

	}

	public TextureUIObject(String str, TexturedQuadLoadedMesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

}
