package lu.kbra.plant_game.engine.mesh;

import java.util.Arrays;

import org.joml.Vector3f;

import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedLoadedMesh extends OffsetMesh implements TexturedMesh {

	protected SingleTexture texture;

	public TexturedLoadedMesh(String name, Material material, Vector3f origin, SingleTexture texture, Vec3fAttribArray vertices,
			UIntAttribArray indices, AttribArray... attribs) {
		super(name, material, origin, vertices, indices, attribs);
		this.texture = texture;
	}

	@Override
	public SingleTexture getTexture() {
		return texture;
	}

	@Override
	public void setTexture(SingleTexture texture) {
		this.texture = texture;
	}

	@Override
	public String toString() {
		return "TexturedLoadedMesh [texture=" + texture + ", name=" + name + ", vao=" + vao + ", vbo=" + vbo + ", material=" + material
				+ ", vertices=" + vertices + ", indices=" + indices + ", attribs=" + Arrays.toString(attribs) + ", vertexCount="
				+ vertexCount + ", indicesCount=" + indicesCount + ", isValid()=" + isValid() + "]";
	}

}
