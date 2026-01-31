package lu.kbra.plant_game.engine.mesh;

import java.util.Arrays;

import org.joml.Vector2fc;

import lu.kbra.standalone.gameengine.geom.QuadLoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class LoadedTexturedQuadMesh extends QuadLoadedMesh implements TexturedQuadMesh {

	protected SingleTexture texture;

	public LoadedTexturedQuadMesh(final String name, final Material material, final Vector2fc size, final SingleTexture texture) {
		super(name, material, size);
		this.texture = texture;
	}

	@Override
	public SingleTexture getTexture() {
		return this.texture;
	}

	@Override
	public void setTexture(final SingleTexture texture) {
		this.texture = texture;
	}

	@Override
	public boolean isValid() {
		return super.isValid() && this.texture.isValid();
	}

	@Override
	public String toString() {
		return "LoadedTexturedQuadMesh [texture=" + this.texture + ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo
				+ ", material=" + this.material + ", vertices=" + this.vertices + ", indices=" + this.indices + ", attribs="
				+ Arrays.toString(this.attribs) + ", vertexCount=" + this.vertexCount + ", indicesCount=" + this.indicesCount
				+ ", isValid()=" + this.isValid() + "]";
	}

}
