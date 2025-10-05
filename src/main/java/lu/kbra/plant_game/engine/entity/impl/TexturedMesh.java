package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedMesh extends Mesh {

	private SingleTexture texture;

	public TexturedMesh(String name, SingleTexture texture, Vec3fAttribArray vertices, UIntAttribArray indices, AttribArray... attribs) {
		super(name, null, vertices, indices, attribs);
		this.texture = texture;
	}

	public SingleTexture getTexture() {
		return texture;
	}

	public void setTexture(SingleTexture texture) {
		this.texture = texture;
	}

}
