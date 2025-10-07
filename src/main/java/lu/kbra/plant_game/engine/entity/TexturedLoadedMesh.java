package lu.kbra.plant_game.engine.entity;

import lu.kbra.plant_game.engine.entity.impl.TexturedMesh;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class TexturedLoadedMesh extends LoadedMesh implements TexturedMesh {

	protected SingleTexture texture;

	public TexturedLoadedMesh(String name, Material material, SingleTexture texture, Vec3fAttribArray vertices,
			UIntAttribArray indices, AttribArray... attribs) {
		super(name, material, vertices, indices, attribs);
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

}
