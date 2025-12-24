package lu.kbra.plant_game.engine.entity.ui.texture;

import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/water-128.png")
public class WaterIconUIObject extends TextureUIObject {

	public WaterIconUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public WaterIconUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

}
