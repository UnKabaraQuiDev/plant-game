package lu.kbra.plant_game.engine.render;

import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;

public class SwayMeshComponent extends MeshComponent {

	public SwayMeshComponent(final SwayMesh swayMesh) {
		super(swayMesh);
	}

	public SwayMesh getSwayMesh() {
		return (SwayMesh) super.mesh;
	}

	public void setSwayMesh(final SwayMesh swayMesh) {
		super.mesh = swayMesh;
	}

	@Override
	public void setMesh(final Mesh mesh) {
		this.setSwayMesh((SwayMesh) mesh);
	}

}
