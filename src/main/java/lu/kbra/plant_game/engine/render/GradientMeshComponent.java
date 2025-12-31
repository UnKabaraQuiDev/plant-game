package lu.kbra.plant_game.engine.render;

import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;

public class GradientMeshComponent extends MeshComponent {

	public GradientMeshComponent(final GradientMesh gradientMesh) {
		super(gradientMesh);
	}

	public GradientMesh getGradientMesh() {
		return (GradientMesh) super.mesh;
	}

	public void setGradientMesh(final GradientMesh gradientMesh) {
		super.mesh = gradientMesh;
	}

	@Override
	public void setMesh(final Mesh mesh) {
		this.setGradientMesh((GradientMesh) mesh);
	}

}
