package lu.kbra.plant_game.engine.entity.ui.texture;

import lu.kbra.plant_game.engine.entity.ui.impl.TextureOption;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;

@DataPath("image:classpath:/icons/large_logo-256.png")
@TextureOption(textureFilter = TextureFilter.LINEAR)
public class LargeLogoUIObject extends TexturedQuadMeshUIObject {

	public LargeLogoUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

}
