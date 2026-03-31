package lu.kbra.plant_game.base.scene.menu.main;

import lu.kbra.plant_game.engine.mesh.TexturedMesh;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

@Deprecated
public class TexturedLoadedTriangleStripMesh extends LoadedTriangleStripMesh implements TexturedMesh {

	@Deprecated
	protected SingleTexture texture;

	@Deprecated
	public TexturedLoadedTriangleStripMesh(
			final String name,
			final Material material,
			final SingleTexture texture,
			final Vec3fAttribArray vertices,
			final JavaAttribArray... attribs) {
		super(name, material, vertices, attribs);
		this.texture = texture;
	}

	@Deprecated
	@Override
	public SingleTexture getTexture() {
		return this.texture;
	}

	@Deprecated
	@Override
	public void setTexture(final SingleTexture texture) {
		this.texture = texture;
	}

	@Deprecated
	@Override
	public String toString() {
		return "TexturedLoadedTriangleStripMesh@" + System.identityHashCode(this) + " [texture=" + this.texture + ", name=" + this.name
				+ ", vao=" + this.vao + ", vbo=" + this.vbo + ", material=" + this.material + ", vertices=" + this.vertices + ", attribs="
				+ this.attribs + ", vertexCount=" + this.vertexCount + ", boundingBox=" + this.boundingBox + "]";
	}

}
