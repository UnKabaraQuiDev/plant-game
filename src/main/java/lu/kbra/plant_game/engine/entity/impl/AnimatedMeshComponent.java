package lu.kbra.plant_game.engine.entity.impl;

import lu.kbra.plant_game.engine.mesh.AnimatedMesh;
import lu.kbra.standalone.gameengine.objs.entity.Component;

public class AnimatedMeshComponent extends Component {

	protected AnimatedMesh AnimatedMesh;

	public AnimatedMeshComponent(AnimatedMesh AnimatedMesh) {
		this.AnimatedMesh = AnimatedMesh;
	}

	public AnimatedMesh getAnimatedMesh() {
		return AnimatedMesh;
	}

	public void setAnimatedMesh(AnimatedMesh AnimatedMesh) {
		this.AnimatedMesh = AnimatedMesh;
	}

}
