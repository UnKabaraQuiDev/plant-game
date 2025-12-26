package lu.kbra.plant_game.engine.scene.ui.overlay;

import lu.kbra.standalone.gameengine.objs.entity.Component;

public class MarginComponent extends Component implements MarginOwner {

	private float margin = 0;

	public MarginComponent() {
	}

	public MarginComponent(final float margin) {
		this.margin = margin;
	}

	@Override
	public float getMargin() {
		return this.margin;
	}

	@Override
	public void setMargin(final float m) {
		this.margin = m;
	}

}
