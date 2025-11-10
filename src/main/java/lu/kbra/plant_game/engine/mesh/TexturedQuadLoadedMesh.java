package lu.kbra.plant_game.engine.mesh;

import org.joml.Vector2f;

import lu.kbra.standalone.gameengine.geom.LoadedQuadMesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedQuadLoadedMesh extends LoadedQuadMesh implements TexturedMesh {

	private SingleTexture texture;

	public TexturedQuadLoadedMesh(String name, SingleTexture txt, Vector2f size) {
		super(name, null, size);
		this.texture = txt;
	}

	@Override
	public SingleTexture getTexture() {
		return texture;
	}

	@Override
	public void setTexture(SingleTexture texture) {
		this.texture = texture;
	}

}
