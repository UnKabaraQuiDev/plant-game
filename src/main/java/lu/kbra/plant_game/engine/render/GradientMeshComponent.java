package lu.kbra.plant_game.engine.render;

import lu.kbra.standalone.gameengine.objs.entity.Component;

public class GradientMeshComponent extends Component {

	protected GradientMesh gradientMesh;

	public GradientMeshComponent(final GradientMesh gradientMesh) {
		this.gradientMesh = gradientMesh;
	}

	public GradientMesh getGradientMesh() {
		return this.gradientMesh;
	}

	public void setGradientMesh(final GradientMesh gradientMesh) {
		this.gradientMesh = gradientMesh;
	}

}
