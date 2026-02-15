package lu.kbra.plant_game.base.scene.world;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.base.entity.go.obj.energy.WaterWheelObject;
import lu.kbra.plant_game.base.entity.go.obj.water.WaterTowerObject;
import lu.kbra.plant_game.base.scene.menu.main.MainMenuUIScene;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.go.factory.GameObjectFactory;
import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.world.GameData;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

public class MainMenuWorldScene extends WorldLevelScene {

	public static float TRANSITION_DURATION = MainMenuUIScene.TRANSITION_DURATION; // seconds

	protected final float[] rotationSpeeds = {
			(float) Math.PI * 2 / 20,
			(float) Math.PI * 2 / 20,
			(float) Math.PI * 2 / 20,
			(float) Math.PI * 2 / 20,
			(float) Math.PI * 2 / 20 };

	private final Transform3DOwner[] targets = { null, null, null, null, null };
	private final Vector3fc[] offsets = {
			new Vector3f(0, 0, 17),
			new Vector3f(0, 0, 0),
			new Vector3f(-2, 3, -4),
			new Vector3f(),
			new Vector3f() };
	private final Vector3fc[] cameraOffsets = {
			new Vector3f(50, 45, 0),
			new Vector3f(),
			new Vector3f(-8, 0, 0),
			new Vector3f(),
			new Vector3f() };

	private int currentIndex = 0;
	private int targetIndex = 0;

	private float progress = 1f;

	private final Vector3fc axis = GameEngine.Y_POS;

	private final Vector3f transitionStart = new Vector3f();
	private final Vector3f transitionEnd = new Vector3f();
	private final Vector3f currentCenter = new Vector3f();

	private final Vector3f offsetStart = new Vector3f();
	private final Vector3f offsetEnd = new Vector3f();
	private final Vector3f currentOffset = new Vector3f();

	private final Vector3f cameraOffsetStart = new Vector3f();
	private final Vector3f cameraOffsetEnd = new Vector3f();
	private final Vector3f currentCameraOffset = new Vector3f();

	public MainMenuWorldScene(final CacheManager parentCache) {
		super("mainMenuWorld", parentCache);
	}

	public void startTransition(final int target) {
		if (this.progress != 0f && this.progress != 1f) {
			return;
		}

		this.targetIndex = target;

		if (this.targets[this.currentIndex] != null) {
			this.transitionStart.set(this.targets[this.currentIndex].getTransform().getTranslation());
			if (this.targets[this.currentIndex].getTransform() instanceof Transform3DPivot tp) {
				this.transitionStart.add(tp.getRotationPivot());
			}
		}
		if (this.targets[this.targetIndex] != null) {
			this.transitionEnd.set(this.targets[this.targetIndex].getTransform().getTranslation());
			if (this.targets[this.targetIndex].getTransform() instanceof Transform3DPivot tp) {
				this.transitionEnd.add(tp.getRotationPivot());
			}
		}

		this.offsetStart.set(this.offsets[this.currentIndex]);
		this.offsetEnd.set(this.offsets[this.targetIndex]);

		this.cameraOffsetStart.set(this.cameraOffsets[this.currentIndex]);
		this.cameraOffsetEnd.set(this.cameraOffsets[this.targetIndex]);

		this.progress = 0f;
	}

	@Override
	public ObjectTriggerLatch<WorldLevelScene> init(
			final Dispatcher workers,
			final Dispatcher render,
			final GameData gameData,
			final IntPointer worldProgress) {
		GameObjectFactory.create(WaterTowerObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, -100, 0))))
				.add(this)
				.postInit(i -> this.targets[MainMenuUIScene.OPTIONS] = i)
				.push();

		GameObjectFactory.create(WaterWheelObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 100, 0))))
				.add(this)
				.postInit(i -> this.targets[MainMenuUIScene.PLAY] = i)
				.push();

		return super.init(workers, render, gameData, worldProgress).thenOther(c -> {
			this.targets[MainMenuUIScene.MAIN] = c.getTerrain();
//			this.startTransition(MainMenuUIScene.MAIN);
			this.currentOffset.set(this.offsets[MainMenuUIScene.MAIN]);
			this.currentCenter.set(this.targets[MainMenuUIScene.MAIN].getTransform().getTranslation())
					.add(((Transform3DPivot) this.targets[MainMenuUIScene.MAIN].getTransform()).getRotationPivot())
					.add(this.currentOffset);
			this.currentCameraOffset.set(this.cameraOffsets[MainMenuUIScene.MAIN]);
		});
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
	}

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final DeferredCompositor compositor,
			final Dispatcher workers,
			final Dispatcher renderDispatcher) {

		super.update(inputHandler, compositor, workers, renderDispatcher);

		final float dTime = inputHandler.dTime();

		if (this.targetIndex != this.currentIndex) {
			this.progress = org.joml.Math.clamp(0f, 1f, this.progress + dTime / TRANSITION_DURATION);
			if (PCUtils.compare(this.progress, 1f, 1e-9)) {
				this.currentOffset.set(this.offsetEnd);
				this.currentCenter.set(this.transitionEnd).add(this.currentOffset);
				this.currentCameraOffset.set(this.cameraOffsetEnd);
				this.currentIndex = this.targetIndex;
				this.progress = 1f;
			} else {
				final float t = Interpolators.BACK_IN_OUT.evaluate(this.progress);
				this.currentOffset.set(this.offsetStart).lerp(this.offsetEnd, t);
				this.currentCenter.set(this.transitionStart).lerp(this.transitionEnd, t).add(this.currentOffset);
				this.currentCameraOffset.set(this.cameraOffsetStart).lerp(this.cameraOffsetEnd, t);
			}
		}
		/*
		 * else { this.currentCenter.set(this.targets[this.currentIndex].getTransform().getTranslation()); }
		 */

		this.applyRotation(dTime, this.targetIndex);
		this.applyRotation(dTime, this.currentIndex);

		this.camera.positionSet(this.currentCenter).positionAdd(this.currentCameraOffset);
		this.camera.lookAt(this.camera.getPosition(), this.currentCenter, this.axis);
		this.camera.updateMatrix();
	}

	private void applyRotation(final float dTime, final int idx) {
		final Transform3DOwner obj = this.targets[idx];
		if (obj == null || !obj.hasTransform()) {
			return;
		}

		obj.getTransform().rotationAdd(0, this.rotationSpeeds[idx] * dTime, 0).updateMatrix();
	}

}
