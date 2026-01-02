package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;

public interface TexturedQuadMeshOwner extends QuadMeshOwner {

	void setTexturedQuadMesh(TexturedQuadMesh m);

	TexturedQuadMesh getTexturedQuadMesh();

	@Override
	default void setQuadMesh(final QuadMesh m) {
		this.setTexturedQuadMesh((TexturedQuadMesh) m);
	}

	@Override
	default QuadMesh getQuadMesh() {
		return this.getTexturedQuadMesh();
	}

}
