package lu.kbra.plant_game.engine.scene.world.modal;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.impl.PlaceableObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;

public class InspectBuildingModal implements Modal {

	public static final Vector4fc GREEN = new Vector4f(0.2f, 0.8f, 0.2f, 1);
	public static final Vector4fc RED = new Vector4f(1f, 0.2f, 0.2f, 1);

	protected ActiveModalController parent;

	protected WorldLevelScene worldScene;
	protected UIScene uiScene;

	protected Runnable closeHook;

	protected PlaceableObject attachedObject;
	protected InspectBuildingUIObjectGroup popup;

	public InspectBuildingModal(final WorldLevelScene worldScene) {
		this.worldScene = worldScene;
	}

	@Override
	public void start() {
		this.uiScene = PGLogic.INSTANCE.getUiScene();
		this.popup = this.uiScene.getEntity(InspectBuildingUIObjectGroup.NAME);
		if (this.popup == null) {
			this.popup = new InspectBuildingUIObjectGroup();
//			this.popup.init().then();
			this.uiScene.add(this.popup);
		}
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		if (this.hasAttachedObject() && !frameState.uiSceneCaughtMouseInput
				&& inputHandler.isMouseButtonPressedOnce(DefaultKeyOption.PLACE)) {
			this.parent.stopModal();
		}
	}

	private void runCloseHook() {
		if (this.closeHook != null) {
			this.closeHook.run();
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

	}

	@Override
	public void render(final float dTime) {
	}

	@Override
	public void cancel() {
		if (!this.hasAttachedObject()) {
			return;
		}
	}

	@Override
	public void stop() {
		this.attachedObject = null;
		this.closeHook = null;
		this.popup.setActive(false);
		this.popup = null;
		this.uiScene = null;
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

	public void setCancelHook(final Runnable cancelHook) {
		this.closeHook = cancelHook;
	}

}
