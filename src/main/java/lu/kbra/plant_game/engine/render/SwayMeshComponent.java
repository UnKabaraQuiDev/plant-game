package lu.kbra.plant_game.engine.render;

import lu.kbra.standalone.gameengine.objs.entity.Component;

public class SwayMeshComponent extends Component {

	protected SwayMesh swayMesh;

	public SwayMeshComponent(final SwayMesh swayMesh) {
		this.swayMesh = swayMesh;
	}

	public SwayMesh getSwayMesh() {
		return this.swayMesh;
	}

	public void setSwayMesh(final SwayMesh swayMesh) {
		this.swayMesh = swayMesh;
	}

}
