package lu.kbra.plant_game.engine.entity.ui.prim;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.logger.GlobalLogger;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.scene.overlay.OverlayUIScene;
import lu.kbra.plant_game.base.scene.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.plant_game.engine.entity.go.GenericGameObject;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.entity.impl.TintOwner;
import lu.kbra.plant_game.engine.entity.ui.ProgrammaticTexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.data.GradientDirection;
import lu.kbra.plant_game.engine.entity.ui.data.HoverState;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.gradient.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.AnimatedOnHover;
import lu.kbra.plant_game.engine.entity.ui.impl.IndexOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsClick;
import lu.kbra.plant_game.engine.entity.ui.impl.UISceneParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.world.GameData;
import lu.kbra.plant_game.engine.scene.world.MoveBuildingModal;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.utils.gl.consts.Consts;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BuildingItemUIObjectGroup extends OffsetUIObjectGroup
		implements AnimatedOnHover, AbsoluteTransformedBoundsOwner, UISceneParentAware, TintOwner, IndexOwner, NeedsClick {

	protected static final float Y_RANGE = 0.1f;
	protected static final float ALPHA = 0.8f;

	protected boolean growing;
	protected float progress = 0f;
	protected int index;

	protected BuildingDefinition<?> buildingDefinition;

	protected ObjectPointer<GradientQuadUIObject> background = new ObjectPointer<>();

	public BuildingItemUIObjectGroup(final BuildingDefinition<?> buildingInfo) {
		super("building-item-" + buildingInfo.getInternalName());
		this.buildingDefinition = buildingInfo;
	}

	public ObjectTriggerLatch<? extends BuildingItemUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends BuildingItemUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		UIObjectFactory
				.createManual(ProgrammaticTexturedQuadMeshUIObject.class,
						"image:classpath:" + Consts.ICONS_BAKES_DIR + this.buildingDefinition.getInternalPath() + ".png")
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0.2f, 0))))
				.add(this)
				.latch(latch)
				.push();

		UIObjectFactory.create(GradientQuadUIObject.class)
				.set(i -> i.setDirection(GradientDirection.UV_Y))
				.set(i -> i.setTint(new Vector4f(ColorMaterial.BLACK.getColor()).mul(1, 1, 1, ALPHA)))
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0.1f, 0))))
				.get(this.background)
				.add(this)
				.latch(latch)
				.push();

		return latch;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void click(final WindowInputHandler input) {
		if (this.buildingDefinition == null) {
			GlobalLogger.warning(this.getClass().getSimpleName() + " (" + this.getId() + ") has no building definition.");
			return;
		}

		final GameData gameData = PGLogic.INSTANCE.getGameData();
		final WorldLevelScene worldScene = PGLogic.INSTANCE.getWorldScene();

		if (!this.buildingDefinition.isUnlocked(gameData, worldScene)) {
			GlobalLogger.warning(this.buildingDefinition.getInternalName() + " not unlocked.");
			return;
		}

		if (!this.buildingDefinition.canBuild(gameData, worldScene)) {
			GlobalLogger.warning(this.buildingDefinition.getInternalName() + " unable to build.");
			return;
		}

		final MoveBuildingModal modal = worldScene.getModal(MoveBuildingModal.class);
		if (worldScene.hasActiveModal()) {
			return;
		}

		GameObjectFactory.create(this.buildingDefinition.getClazz())
				.set(i -> i.setTransform(new Transform3D()))
				.add(worldScene)
				.then(PGLogic.INSTANCE.WORKERS, (Consumer) (c) -> {
					if (!(c instanceof GenericGameObject go && c instanceof PlaceableObject po)) {
						return;
					}
					modal.setAttachedObject(po);
					modal.setPlaceHook(() -> gameData.buyBuilding(this.buildingDefinition));
					modal.setCancelHook(() -> worldScene.remove(go));
					worldScene.startModal(modal);
				})
				.push();
	}

	@Override
	public boolean isAnimated() {
		return this.hasTransform();
	}

	@Override
	public void animate(final float t, final boolean isHovered) {
		this.getTransform().translationSetZ(-t * Y_RANGE).update();
		this.getTransform().updateMatrix();
	}

	@Override
	public boolean hover(final WindowInputHandler input, final HoverState hoverState) {
		final boolean grow = (hoverState == HoverState.ENTER || hoverState == HoverState.STAY);

		if (hoverState == HoverState.ENTER || hoverState == HoverState.STAY) {
			this.getUISceneParent().filter(OverlayUIScene.class::isInstance).map(OverlayUIScene.class::cast).ifPresent(scene -> {
				final BuildingInfoUIObjectGroup buildingInfo = scene.getBuildingInfo();
				buildingInfo.setActive(true);
				buildingInfo.setTarget(this);

				if (buildingInfo.setBuildingDefinition(this.getBuildingDefinition())) {
					final Rectangle2D infoBounds = buildingInfo.getLocalTransformedBounds().getBounds2D();
					final Rectangle2D selfBounds = this.getAbsoluteTransformedBounds().getBounds2D();

					if (selfBounds.getCenterX() + infoBounds.getMaxX() > scene.getBounds().getMaxX()) {
						buildingInfo.setObjectAnchor(Anchor.BOTTOM_RIGHT);
					} else if (selfBounds.getCenterX() + infoBounds.getMinX() < scene.getBounds().getMinX()) {
						buildingInfo.setObjectAnchor(Anchor.BOTTOM_LEFT);
					} else {
						buildingInfo.setObjectAnchor(Anchor.BOTTOM_CENTER);
					}
				}
			});
		} else if (hoverState == HoverState.LEAVE) {
			this.getUISceneParent()
					.filter(OverlayUIScene.class::isInstance)
					.map(OverlayUIScene.class::cast)
					.map(OverlayUIScene::getBuildingInfo)
					.filter(buildingInfo -> buildingInfo.getTarget() == BuildingItemUIObjectGroup.this)
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
		}
		this.growing = grow;

		final boolean ret = AnimatedOnHover.super.hover(input, hoverState);

		return ret;
	}

	@Override
	public Shape getBounds() {
		if (this.progress > 0 && this.hasTransform()) {
			final Rectangle2D comp = super.getBounds().getBounds2D();
			return new Rectangle2D.Float((float) comp.getX(),
					(float) comp.getY(),
					(float) comp.getWidth(),
					(float) comp.getHeight() - this.getTransform().getTranslation().z() / this.getTransform().getScale().z());
		}
		return super.getBounds();
	}

	public void updateTintStatus(final GameData gameData, final WorldLevelScene world) {
		this.setColorMaterial(this.buildingDefinition.getColorMaterial(gameData, world));
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
	public void setColorMaterial(final ColorMaterial cm) {
		this.background.ifSet(o -> o.setTint(new Vector4f(cm.getColor()).mul(1, 1, 1, ALPHA)));
	}

	@Override
	public String toString() {
		return "BuildingItemUIObject@" + System.identityHashCode(this) + " [growing=" + this.growing + ", progress=" + this.progress
				+ ", index=" + this.index + ", buildingDefinition=" + this.buildingDefinition + ", background=" + this.background
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
