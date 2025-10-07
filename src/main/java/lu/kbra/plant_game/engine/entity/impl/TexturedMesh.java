package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public interface TexturedMesh extends Mesh {

	SingleTexture getTexture();

	void setTexture(SingleTexture texture);

}
