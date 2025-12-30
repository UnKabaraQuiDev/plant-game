package lu.kbra.plant_game.engine.mesh;

import java.util.Arrays;

import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedSwayLoadedMesh extends SwayLoadedMesh implements TexturedMesh {

	protected SingleTexture texture;

	public TexturedSwayLoadedMesh(
			final String name,
			final Material material,
			final float deformRatio,
			final float speedRatio,
			final SingleTexture texture,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final AttribArray... attribs) {
		super(name, material, deformRatio, speedRatio, vertices, indices, attribs);
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
	public String toString() {
		return "TexturedSwayLoadedMesh [texture=" + this.texture + ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo
				+ ", material=" + this.material + ", vertices=" + this.vertices + ", indices=" + this.indices + ", attribs="
				+ Arrays.toString(this.attribs) + ", vertexCount=" + this.vertexCount + ", indicesCount=" + this.indicesCount
				+ ", boundingBox=" + this.boundingBox + "]";
	}

}
