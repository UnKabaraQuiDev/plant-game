package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.engine.entity.ui.impl.TextureUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadLoadedMesh;
import lu.kbra.plant_game.engine.util.DataPath;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@DataPath("image:classpath:/icons/money-32.png")
public class MoneyUIObject extends TextureUIObject {

	public MoneyUIObject(String str, TexturedQuadLoadedMesh mesh) {
		super(str, mesh);
	}

	public MoneyUIObject(String str, TexturedQuadLoadedMesh mesh, Transform3D transform) {
		super(str, mesh, transform);
	}

}
