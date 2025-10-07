package lu.kbra.plant_game.engine.entity.water;

import lu.kbra.plant_game.engine.entity.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;

public class AnimatedMeshComponent extends MeshComponent {

	public AnimatedMeshComponent(AnimatedMesh mesh) {
		super((Mesh) mesh);
	}

	@Override
	public AnimatedMesh getMesh() {
		return (AnimatedMesh) super.getMesh();
	}

	@Override
	public void setMesh(Mesh mesh) {
		super.setMesh((AnimatedMesh) mesh);
	}

}
