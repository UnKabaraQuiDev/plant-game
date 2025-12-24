package lu.kbra.plant_game.engine.entity.ui.texture;

import lu.kbra.plant_game.engine.entity.ui.prim.QuadUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class TextureUIObject extends QuadUIObject {

	public TextureUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);

	}

	public TextureUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

}
