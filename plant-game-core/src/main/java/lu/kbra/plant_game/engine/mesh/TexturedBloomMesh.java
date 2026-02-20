package lu.kbra.plant_game.engine.mesh;

import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public interface TexturedBloomMesh extends TexturedMesh {

	default SingleTexture getDiffuseTexture() {
		return this.getTexture();
	}

	default void setDiffuseTexture(final SingleTexture texture) {
		this.setTexture(texture);
	}

	SingleTexture getBloomTexture();

	void setBloomTexture(SingleTexture texture);

	void setBloomStrength(final float bloomStrength);

	float getBloomStrength();

}
