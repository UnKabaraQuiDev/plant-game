package lu.kbra.plant_game.engine.render;

import lu.kbra.standalone.gameengine.objs.entity.Component;

public class GradientMeshComponent extends Component {

	protected GradientMesh gradientMesh;

	public GradientMeshComponent(GradientMesh gradientMesh) {
		this.gradientMesh = gradientMesh;
	}

	public GradientMesh getGradientMesh() {
		return gradientMesh;
	}

	public void setGradientMesh(GradientQuadMesh gradientMesh) {
		this.gradientMesh = gradientMesh;
	}

}
