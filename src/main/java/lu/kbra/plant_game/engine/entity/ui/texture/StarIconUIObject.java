package lu.kbra.plant_game.engine.entity.ui.texture;

import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/star-128.png")
public class StarIconUIObject extends TextureUIObject {

	public StarIconUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public StarIconUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

}
