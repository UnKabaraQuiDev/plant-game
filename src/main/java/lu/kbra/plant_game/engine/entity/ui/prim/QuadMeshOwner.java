package lu.kbra.plant_game.engine.entity.ui.prim;

import lu.kbra.plant_game.engine.entity.impl.MeshOwner;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;

public interface QuadMeshOwner extends MeshOwner {

	void setQuadMesh(QuadMesh m);

	QuadMesh getQuadMesh();

	@Override
	default void setMesh(final Mesh m) {
		this.setQuadMesh((QuadMesh) m);
	}

	@Override
	default Mesh getMesh() {
		return this.getQuadMesh();
	}

}
