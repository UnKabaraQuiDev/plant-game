package lu.kbra.plant_game;

import org.joml.Vector2f;

import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedQuadMesh extends QuadMesh implements TexturedMesh {

	private SingleTexture texture;

	public TexturedQuadMesh(String name, SingleTexture txt, Vector2f size) {
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
