package lu.kbra.plant_game.engine.mesh;

import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.AttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class LoadedTexturedMesh extends OffsetLoadedMesh implements TexturedMesh {

	protected SingleTexture texture;

	public LoadedTexturedMesh(final String name, final Material material, final Vector3f origin, final SingleTexture texture, final Vec3fAttribArray vertices,
			final UIntAttribArray indices, final AttribArray... attribs) {
		super(name, material, origin, vertices, indices, attribs);
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
		return "LoadedTexturedMesh@" + System.identityHashCode(this) + " [texture=" + this.texture + ", name=" + this.name + ", vao="
				+ this.vao + ", vbo=" + this.vbo + ", material=" + this.material + ", vertices=" + this.vertices + ", indices="
				+ this.indices + ", attribs=" + this.attribs + ", vertexCount=" + this.vertexCount + ", indicesCount=" + this.indicesCount
				+ ", boundingBox=" + this.boundingBox + "]";
	}

}
