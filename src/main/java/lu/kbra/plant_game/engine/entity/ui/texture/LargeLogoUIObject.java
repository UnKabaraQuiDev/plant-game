package lu.kbra.plant_game.engine.entity.ui.texture;

import lu.kbra.plant_game.engine.entity.ui.impl.TextureUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadLoadedMesh;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/large_logo-256.png")
@TextureOption(textureFilter = TextureFilter.LINEAR)
public class LargeLogoUIObject extends TextureUIObject {

	public LargeLogoUIObject(String str, TexturedQuadLoadedMesh mesh) {
		super(str, mesh);
	}

	public LargeLogoUIObject(String str, TexturedQuadLoadedMesh mesh, Transform3D transform) {
		super(str, mesh, transform);
		System.err.println(str + " " + mesh + " " + transform);
	}

}
