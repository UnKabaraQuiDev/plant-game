package lu.kbra.plant_game.engine.mesh;

import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public interface TexturedMesh extends Mesh {

	SingleTexture getTexture();

	void setTexture(SingleTexture texture);

}
