package lu.kbra.plant_game.engine.mesh;

import org.joml.Vector2f;

import lu.kbra.standalone.gameengine.geom.QuadLoadedMesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedQuadLoadedMesh extends QuadLoadedMesh implements TexturedQuadMesh {

	private SingleTexture texture;

	public TexturedQuadLoadedMesh(final String name, final SingleTexture txt, final Vector2f size) {
		super(name, null, size);
		this.texture = txt;
	}

	@Override
	public SingleTexture getTexture() {
		return this.texture;
	}

	@Override
	public void setTexture(final SingleTexture texture) {
		this.texture = texture;
	}

}
