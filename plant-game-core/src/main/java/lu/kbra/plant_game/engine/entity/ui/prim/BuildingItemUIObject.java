package lu.kbra.plant_game.engine.entity.ui.prim;

import java.awt.geom.Rectangle2D;
import java.io.File;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;
import lu.pcy113.pclib.pointer.ObjectPointer;

import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.engine.entity.impl.TintOwner;
import lu.kbra.plant_game.engine.entity.ui.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.ProgrammaticTexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.data.HoverState;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.AnimatedOnHover;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.vanilla.scene.overlay.OverlayUIScene;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BuildingItemUIObject extends OffsetUIObjectGroup // ProgrammaticTexturedQuadMeshUIObject
		implements AnimatedOnHover, AbsoluteTransformedBoundsOwner, UISceneParentAware, TintOwner, IndexOwner {

	protected static final Vector3fc TARGET_SCALE = new Vector3f(1.05f);

	protected static final float Y_RANGE = 0.1f;

	protected boolean growing;
	protected float progress = 0f;
	protected int index;

	protected BuildingDefinition<?> buildingDefinition;

	protected ObjectPointer<FlatQuadUIObject> background = new ObjectPointer<>();

	public BuildingItemUIObject(final BuildingDefinition<?> buildingInfo) {
		super("building-item-" + buildingInfo.getInternalName());
		this.buildingDefinition = buildingInfo;
	}

	public ObjectTriggerLatch<? extends BuildingItemUIObject> init() {
		final ObjectTriggerLatch<? extends BuildingItemUIObject> latch = new ObjectTriggerLatch<>(2, this);

		UIObjectFactory
				.createManual(ProgrammaticTexturedQuadMeshUIObject.class,
						"image:" + new File(Consts.ICONS_BAKES_RES_DIR,
								this.buildingDefinition.getInternalName().replace('.', '/') + ".png").getPath())
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0.2f, 0))))
				.add(this)
				.latch(latch)
				.push();

		UIObjectFactory.create(FlatQuadUIObject.class)
				.set(i -> i.setColorMaterial(ColorMaterial.BLACK))
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0.1f, 0))))
				.get(this.background)
				.add(this)
				.latch(latch)
				.push();

		return latch;
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
				buildingInfo.setBuildingDefinition(this.getBuildingDefinition());

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
			this.getUISceneParent()
					.filter(OverlayUIScene.class::isInstance)
					.map(OverlayUIScene.class::cast)
					.map(OverlayUIScene::getBuildingInfo)
					.filter(buildingInfo -> buildingInfo.getTarget() == BuildingItemUIObject.this)
					.ifPresent(buildingInfo -> {
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

	public void updateTintStatus(final WorldLevelScene world) {
		if (this.buildingDefinition.canBuild(world)) {
			this.setColorMaterial(ColorMaterial.GREEN);
		} else if (this.buildingDefinition.isUnlocked(world)) {
			this.setColorMaterial(ColorMaterial.ORANGE);
		} else {
			this.setColorMaterial(ColorMaterial.GRAY);
		}
	}

//	@Override
//	public Rectangle2D.Float getBounds() {
//		if (this.progress > 0 && this.hasTransform()) {
//			final Rectangle2D comp = super.getBounds().getBounds2D();
//			return new Rectangle2D.Float((float) comp.getX(),
//					(float) comp.getY(),
//					(float) comp.getWidth(),
//					(float) comp.getHeight() - this.getTransform().getTranslation().z() / this.getTransform().getScale().z());
//		}
//		return super.getBounds();
//	}

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

	public BuildingDefinition<?> getBuildingDefinition() {
		return this.buildingDefinition;
	}

	public void setBuildingDefinition(final BuildingDefinition<?> BuildingDefinition) {
		this.buildingDefinition = BuildingDefinition;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	@Override
	public Vector4fc getTint() {
		return this.background.get().getTint();
	}

	@Override
	public void setTint(final Vector4fc tint) {
		this.background.ifSet(o -> o.setTint(tint));
	}

	@Override
	public String toString() {
		return "BuildingItemUIObject@" + System.identityHashCode(this) + " [growing=" + this.growing + ", progress=" + this.progress
				+ ", index=" + this.index + ", buildingDefinition=" + this.buildingDefinition + ", background=" + this.background
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
