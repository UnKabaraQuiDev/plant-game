package lu.kbra.plant_game.engine.entity.ui.texture;

import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;

@DataPath("image:classpath:/icons/water-128.png")
public class WaterIconUIObject extends TextureUIObject {

	public WaterIconUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

}
