package lu.kbra.plant_game.engine.mesh;

import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class LoadedTexturedBloomMesh extends LoadedTexturedMesh implements TexturedBloomMesh {

	protected SingleTexture bloomTexture;
	protected float bloomStrength;

	public LoadedTexturedBloomMesh(
			final String name,
			final Material material,
			final Vector3f origin,
			final SingleTexture texture,
			final SingleTexture bloomTexture,
			final float bloomStrength,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final JavaAttribArray... attribs) {
		super(name, material, origin, texture, vertices, indices, attribs);
		this.bloomTexture = bloomTexture;
		this.bloomStrength = bloomStrength;
	}

	@Override
	public SingleTexture getBloomTexture() {
		return this.bloomTexture;
	}

	@Override
	public void setBloomTexture(final SingleTexture texture) {
		this.bloomTexture = texture;
	}

	@Override
	public float getBloomStrength() {
		return this.bloomStrength;
	}

	@Override
	public void setBloomStrength(final float bloomStrength) {
		this.bloomStrength = bloomStrength;
	}

	@Override
	public String toString() {
		return "LoadedTexturedBloomMesh@" + System.identityHashCode(this) + " [bloomTexture=" + this.bloomTexture + ", bloomStrength="
				+ this.bloomStrength + ", texture=" + this.texture + ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo
				+ ", material=" + this.material + ", vertices=" + this.vertices + ", indices=" + this.indices + ", attribs=" + this.attribs
				+ ", vertexCount=" + this.vertexCount + ", indicesCount=" + this.indicesCount + ", boundingBox=" + this.boundingBox
				+ ", cleanable=" + this.cleanable + "]";
	}

}
