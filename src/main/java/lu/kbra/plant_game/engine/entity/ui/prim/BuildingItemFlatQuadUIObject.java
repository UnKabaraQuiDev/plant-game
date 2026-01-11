package lu.kbra.plant_game.engine.entity.ui.prim;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.ui.data.HoverState;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.AnimatedOnHover;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.overlay.OverlayUIScene;
import lu.kbra.plant_game.engine.scene.ui.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.plant_game.engine.scene.world.data.building.BuildingDeclaration;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;

public class BuildingItemFlatQuadUIObject extends IndexedFlatQuadUIObject
		implements AnimatedOnHover, AbsoluteTransformedBoundsOwner, UISceneParentAware {

	protected static final Vector3fc TARGET_SCALE = new Vector3f(1.05f);

	protected static final float Y_RANGE = 0.1f;

	protected boolean growing;
	protected float progress = 0f;

	protected BuildingDeclaration buildingDeclaration;

	public BuildingItemFlatQuadUIObject(final String str, final TexturedQuadMesh mesh) {
		super(str, mesh);
	}

	@Override
	public boolean hover(final WindowInputHandler input, final HoverState hoverState) {
		if (!this.hasTransform()) {
			return false;
		}
		final boolean grow = (hoverState == HoverState.ENTER || hoverState == HoverState.STAY);

		if (hoverState == HoverState.ENTER) {
			this.getUISceneParent().filter(OverlayUIScene.class::isInstance).map(OverlayUIScene.class::cast).ifPresent(scene -> {
				final BuildingInfoUIObjectGroup buildingInfo = scene.getBuildingInfo();
				buildingInfo.setActive(true);
				buildingInfo.setTarget(this);

				final Rectangle2D infoBounds = buildingInfo.getLocalTransformedBounds().getBounds2D();
				final Rectangle2D selfBounds = this.getAbsoluteTransformedBounds().getBounds2D();

				if (selfBounds.getCenterX() + infoBounds.getMaxX() > scene.getBounds().getMaxX()) {
					buildingInfo.setObjectAnchor(Anchor.BOTTOM_RIGHT);
				} else if (selfBounds.getCenterX() + infoBounds.getMinX() < scene.getBounds().getMinX()) {
					buildingInfo.setObjectAnchor(Anchor.BOTTOM_LEFT);
				} else {
					buildingInfo.setObjectAnchor(Anchor.BOTTOM_CENTER);
				}
			});
		} else if (hoverState == HoverState.LEAVE) {
			this.getUISceneParent().filter(OverlayUIScene.class::isInstance).map(OverlayUIScene.class::cast).ifPresent(scene -> {
				final BuildingInfoUIObjectGroup buildingInfo = scene.getBuildingInfo();
				if (buildingInfo.getTarget() != BuildingItemFlatQuadUIObject.this) {
					return;
				}
				buildingInfo.setActive(false);
				buildingInfo.setTarget(null);
			});
		}

		final Interpolator interpol = this.getInterpolator(grow);
		if (this.progress == 0 || this.progress == 1) {
		} else if (grow ^ this.growing && interpol.hasInverse()) {
			this.progress = interpol.inverse(this.progress);
		} else if (grow ^ this.growing && !interpol.hasInverse()) {
			this.progress = Interpolator.inverse(this.progress, interpol, 0.02f, 0.02f, this.progress);
//			GlobalLogger.warning("Interpolator: " + interpol + " has no inverse.");
		}
		this.growing = grow;

		this.compute(input.dTime(), grow);
		this.getTransform().translationSetZ(-interpol.evaluate(this.getGrowthProgress()) * Y_RANGE).update();
		this.getTransform().updateMatrix();

		return this.progress == 0;
	}

	@Override
	public Float getBounds() {
		if (this.progress > 0 && this.hasTransform()) {
			final Rectangle2D comp = super.getBounds();
			return new Rectangle2D.Float((float) comp.getX(),
					(float) comp.getY(),
					(float) comp.getWidth(),
					(float) comp.getHeight() - this.getTransform().getTranslation().z() / this.getTransform().getScale().z());
		}
		return super.getBounds();
	}

	@Override
	public float getGrowthRate(final boolean grow) {
		return grow ? 3f : 2f;
	}

	@Override
	public float getGrowthProgress() {
		return this.progress;
	}

	@Override
	public void setGrowthProgress(final float f) {
		this.progress = f;
	}

	@Override
	public Interpolator getInterpolator(final boolean grow) {
		return grow ? Interpolators.QUAD_OUT : Interpolators.BOUNCE_IN;
	}

	public boolean isGrowing() {
		return this.growing;
	}

	public BuildingDeclaration getBuildingDeclaration() {
		return this.buildingDeclaration;
	}

	public void setBuildingDeclaration(final BuildingDeclaration buildingDeclaration) {
		this.buildingDeclaration = buildingDeclaration;
	}

	@Override
	public String toString() {
		return "BuildingItemFlatQuadUIObject@" + System.identityHashCode(this) + " [growing=" + this.growing + ", progress=" + this.progress
				+ ", index=" + this.index + ", color=" + this.color + ", bounds=" + this.bounds + ", mesh=" + this.mesh + ", transform="
				+ this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
