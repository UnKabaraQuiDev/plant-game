package lu.kbra.plant_game.engine.scene.ui;

public class UniformMarginFlowLayout extends MarginFlowLayout {

	public UniformMarginFlowLayout(final boolean vertical, final float gap, final float margin, final byte flags) {
		super(vertical, gap, margin, flags);
	}

	public void setMargin(final float margin) {
		this.marginBottom = this.marginTop = this.marginLeft = this.marginRight = margin;
	}

}
