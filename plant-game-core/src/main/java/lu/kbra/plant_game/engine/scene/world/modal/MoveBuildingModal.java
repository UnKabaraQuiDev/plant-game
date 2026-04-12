package lu.kbra.plant_game.engine.scene.world.modal;

import java.util.Optional;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.obj.terrain.TerrainGameObject;
import lu.kbra.plant_game.engine.entity.impl.PlaceableObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class MoveBuildingModal implements Modal {

	public static final Vector4fc GREEN = new Vector4f(0.2f, 0.8f, 0.2f, 1);
	public static final Vector4fc RED = new Vector4f(1f, 0.2f, 0.2f, 1);

	protected ActiveModalController parent;

	protected DeferredCompositor compositor;
	protected WorldLevelScene worldScene;

	protected Runnable placeHook;
	protected Runnable cancelHook;
	protected Runnable errorHook;

	protected PlaceableObject attachedObject;

	protected Vector2i source;
	protected Direction sourceRotation;

	protected Vector2i currentPos;
	protected Direction targetRotation;

	protected boolean placeDown;

	public MoveBuildingModal(final WorldLevelScene worldScene, final DeferredCompositor compositor) {
		this.worldScene = worldScene;
		this.compositor = compositor;
	}

	@Override
	public void start() {
		if (!this.attachedObject.hasTransform()) {
			this.attachedObject.setTransform(new Transform3D());
			this.targetRotation = Direction.NONE;
		} else {
			final TerrainGameObject terrain = this.worldScene.getTerrain();
			final Vector2i candidateSource = terrain.getCellPosition(this.attachedObject.getTransform().getTranslation());
			if (terrain.getMesh().isInBounds(candidateSource)) {
				this.source = candidateSource;
			}
			this.sourceRotation = this.targetRotation = this.attachedObject.getRotation();
		}

		this.worldScene.getTerrain().getMoveHighlightObject().setActive(true);
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		if (this.hasAttachedObject() && !frameState.uiSceneCaughtKeyboardInput) {
			if (inputHandler.isKeyPressedOrRepeat(DefaultKeyOption.TURN_CW)) {
				this.targetRotation = this.targetRotation.getClockwise();
			}
			if (inputHandler.isKeyPressedOrRepeat(DefaultKeyOption.TURN_CCW)) {
				this.targetRotation = this.targetRotation.getCounterClockwise();
			}
		}
		if (!frameState.uiSceneCaughtMouseInput) {
			if (inputHandler.isMouseButtonPressedOnce(DefaultKeyOption.PLACE)) {
				if (this.worldScene.isPlaceable(this.attachedObject, this.currentPos, this.targetRotation)) {
					this.placeDown = true;
				} else {
					this.placeDown = false;
					this.runErrorHook();
				}
			} else if (inputHandler.isMouseButtonPressedOnce(DefaultKeyOption.CANCEL)) {
				this.runCancelHook();
				this.parent.cancelModal();
			}
		}
	}

	private void runCancelHook() {
		if (this.cancelHook != null) {
			this.cancelHook.run();
		}
	}

	private void runPlaceHook() {
		if (this.placeHook != null) {
			this.placeHook.run();
		}
	}

	private void runErrorHook() {
		if (this.errorHook != null) {
			this.errorHook.run();
		}
	}

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final DeferredCompositor compositor,
			final Dispatcher workers,
			final Dispatcher renderDispatcher) {
		if (!this.hasAttachedObject()) {
			return;
		}

		final Vector2f mousePos = inputHandler.getMousePosition();
		this.currentPos = this.worldScene.getTerrain()
				.pickTerrainCell(this.worldScene.getCamera(),
						mousePos,
						this.compositor.getWindow().getWidth(),
						this.compositor.getWindow().getHeight());
		if (this.currentPos != null) {
			this.worldScene.getTerrain()
					.getMoveHighlightObject()
					.getTransform()
					.translationSet(this.worldScene.getTerrain().getTilePosition(this.currentPos))
					.translationSub(this.worldScene.getTerrain().getTransform().getTranslation())
					.updateMatrix();
			this.attachedObject.placeDown(this.worldScene.getTerrain(), this.currentPos, this.targetRotation);
			if (this.worldScene.isPlaceable(this.attachedObject, this.currentPos, this.targetRotation)) {
				this.compositor.addOutline(this.attachedObject, GREEN);
			} else {
				this.compositor.addOutline(this.attachedObject, RED);
			}
			if (this.placeDown) {
				this.attachedObject.confirmPlaceDown(this.worldScene
						.getTerrain(), this.source, this.sourceRotation, this.currentPos, this.targetRotation);
				this.worldScene.getTerrain()
						.place(Optional.ofNullable(this.source), Optional.ofNullable(this.sourceRotation), this.attachedObject);
				this.runPlaceHook();
				this.parent.stopModal();
			}
		} else {
			this.compositor.addOutline(this.attachedObject, RED);
		}
	}

	@Override
	public void render(final float dTime) {
	}

	@Override
	public void cancel() {
		if (!this.hasAttachedObject()) {
			return;
		}

		if (this.source == null) {
			this.worldScene.remove(this.attachedObject);
			// ref. lost, memory will cleanup the mesh if needed
			this.attachedObject.setActive(false);
		} else {
			this.attachedObject.placeDown(this.worldScene.getTerrain(), this.source, this.sourceRotation);
		}
	}

	@Override
	public void stop() {
		if (this.hasAttachedObject()) {
			this.compositor.removeOutline(this.attachedObject);
		}
		this.attachedObject = null;
		this.source = null;
		this.sourceRotation = null;
		this.targetRotation = null;
		this.currentPos = null;
		this.errorHook = null;
		this.cancelHook = null;
		this.placeHook = null;
		this.placeDown = false;
		this.worldScene.getTerrain().getMoveHighlightObject().setActive(false);
	}

	public boolean hasAttachedObject() {
		return this.attachedObject != null;
	}

	public PlaceableObject getAttachedObject() {
		return this.attachedObject;
	}

	public void setAttachedObject(final PlaceableObject attachedObject) {
		if (this.attachedObject != null) {
			throw new IllegalStateException("Modal still active.");
		}
		if (attachedObject == null) {
			this.cancel();
		}
		this.attachedObject = attachedObject;
	}

	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		if (!(e instanceof ActiveModalController amc)) {
			throw new IllegalArgumentException(e.getClass().getSimpleName() + " is not a ModalsOwner.");
		}
		this.parent = amc;
	}

	@Override
	public ParentAwareComponent getParent() {
		return this.parent;
	}

	public void setPlaceHook(final Runnable run) {
		this.placeHook = run;
	}

	public void setCancelHook(final Runnable cancelHook) {
		this.cancelHook = cancelHook;
	}

	public void setErrorHook(final Runnable errorHook) {
		this.errorHook = errorHook;
	}

}
