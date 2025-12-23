package lu.kbra.plant_game.engine.entity.ui.texture;

import lu.kbra.plant_game.engine.entity.ui.impl.TextureOption;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.util.annotation.DataPath;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/large_logo-256.png")
@TextureOption(textureFilter = TextureFilter.LINEAR)
public class LargeLogoUIObject extends TextureUIObject {

	public LargeLogoUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	public LargeLogoUIObject(final String str, final TexturedQuadMesh mesh, final Transform3D transform) {
		super(str, mesh, transform);
	}

}
