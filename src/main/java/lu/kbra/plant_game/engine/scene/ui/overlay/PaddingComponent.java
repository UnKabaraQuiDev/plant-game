package lu.kbra.plant_game.engine.scene.ui.overlay;

import lu.kbra.standalone.gameengine.objs.entity.Component;

public class PaddingComponent extends Component implements PaddingOwner {

	private float padding = 0;

	public PaddingComponent() {
	}

	public PaddingComponent(final float padding) {
		this.padding = padding;
	}

	@Override
	public float getPadding() {
		return this.padding;
	}

	@Override
	public void setPadding(final float p) {
		this.padding = p;
	}

}
