package lu.kbra.plant_game.engine.entity.ui.btn;

import lu.kbra.plant_game.engine.entity.impl.AnimatedOnHover;
import lu.kbra.plant_game.engine.entity.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.geo.GeoAxis;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DShear;

public abstract class MainMenuItemTextUIObject extends TextUIObject implements NeedsClick, AnimatedOnHover, IndexOwner, UISceneParentAware {

	protected static final float SHEAR_Y_FACTOR = 0;
	protected static final float SHEAR_X_FACTOR = -0.5f;
	protected static final float SCALE_FACTOR = 0.2f;

	protected float growthProgress = 0;

	public MainMenuItemTextUIObject(final String str, final TextEmitter text) {
		super(str, text);
		super.setTransform(new Transform3DShear());
	}

	@Override
	public void animate(final float t, final boolean isHovered) {
		if (this.hasTransform()) {
			if (this.getTransform() instanceof Transform3DShear t3Ds) {
				t3Ds.shearSet(GeoAxis.X, GeoAxis.Z, t * SHEAR_Y_FACTOR);
				t3Ds.shearSet(GeoAxis.Z, GeoAxis.X, t * SHEAR_X_FACTOR);
			}
			this.getTransform().scaleSet(1 + t * SCALE_FACTOR);
			this.getTransform().update();
		}
		this.getFirstParentMatching(LayoutOwner.class).ifPresent(LayoutOwner::doLayout);
	}

	@Override
	public boolean isAnimated() {
		return this.hasTransform();
	}

	@Override
	public float getGrowthRate(final boolean grow) {
		return 5;
	}

	@Override
	public float getGrowthProgress() {
		return this.growthProgress;
	}

	@Override
	public void setGrowthProgress(final float growthProgress) {
		this.growthProgress = growthProgress;
	}

	@Override
	public String toString() {
		return "MainMenuITemTextUIObject@" + System.identityHashCode(this) + " [growthProgress=" + this.growthProgress + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
