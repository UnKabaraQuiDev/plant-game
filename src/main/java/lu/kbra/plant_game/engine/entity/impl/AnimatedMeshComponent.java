package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.entity.components.MeshComponent;

public class AnimatedMeshComponent extends MeshComponent {

	public AnimatedMeshComponent(final AnimatedMesh animatedMesh) {
		super(animatedMesh);
	}

	public AnimatedMesh getAnimatedMesh() {
		return (AnimatedMesh) super.mesh;
	}

	public void setAnimatedMesh(final AnimatedMesh animatedMesh) {
		super.mesh = animatedMesh;
	}

	@Override
	public void setMesh(final Mesh mesh) {
		this.setAnimatedMesh((AnimatedMesh) mesh);
	}

}
