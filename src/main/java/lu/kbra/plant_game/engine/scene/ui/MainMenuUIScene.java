package lu.kbra.plant_game.engine.scene.ui;

import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LargeLogoUIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class MainMenuUIScene extends UIScene {

	public static final int MAIN = 0;
	public static final int PLAY = 1;
	public static final int OPTIONS = 2;
	public static final int QUIT = 3;

	protected int currentGroup = 0;
	protected int targetGroup = 0;
	protected float progress = 0;

	protected Vector3f[] restPositions = { new Vector3f(), null, new Vector3f(2, 0, 0), null };

	protected OffsetUIObjectGroup mainMenuGroup = new OffsetUIObjectGroup("main", new Transform3D());
	protected OffsetUIObjectGroup optionsMenuGroup = new OffsetUIObjectGroup("option",
			new Transform3D(new Vector3f(restPositions[OPTIONS])));

	protected OffsetUIObjectGroup[] groups = new OffsetUIObjectGroup[] { mainMenuGroup, null, optionsMenuGroup, null };

	protected CursorUIObject cursor;

	protected GradientQuadUIObject greenGradient;
	protected GradientQuadUIObject blueGradient;

	public MainMenuUIScene(CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(Dispatcher workers, Dispatcher renderDispatcher) {
		super.addEntities(mainMenuGroup, optionsMenuGroup);

		final TextData uiTextData = new TextData(new Vector2f(0.2f), TextAlignment.TEXT_CENTER);

		final float x = -0.8f;

		/** main menu */

		UIObjectFactory
				.create(PlayButtonUIObject.class, uiTextData, new Transform3D(new Vector3f(x, 0, -0.25f)))
				.then(workers, (Consumer<PlayButtonUIObject>) (t) -> {
					mainMenuGroup.add(t);
				})
				.push();
		UIObjectFactory
				.create(OptionsButtonUIObject.class, uiTextData, new Transform3D(new Vector3f(x, 0, 0)))
				.then(workers, (Consumer<OptionsButtonUIObject>) (t) -> {
					mainMenuGroup.add(t);
				})
				.push();
		UIObjectFactory
				.create(QuitButtonUIObject.class, uiTextData, new Transform3D(new Vector3f(x, 0, 0.25f)))
				.then(workers, (Consumer<QuitButtonUIObject>) (t) -> {
					mainMenuGroup.add(t);
				})
				.push();

		UIObjectFactory
				.create(LargeLogoUIObject.class, new Transform3D(new Vector3f(0, 0, -0.75f), new Quaternionf(), new Vector3f(2)))
				.then(workers, (Consumer<LargeLogoUIObject>) (t) -> {
					mainMenuGroup.add(t);
				})
				.push();

		UIObjectFactory
				.create(GradientQuadUIObject.class, new Transform3D(), GradientDirection.UV_X, GameEngineUtils.hexToColorToVec4f("3b784a"))
				.then(workers, (Consumer<GradientQuadUIObject>) (GradientQuadUIObject t) -> {
					t.getTransform().translationSet(-1, -0.11f, 0).scaleSet(2, 1, 2).updateMatrix();
					greenGradient = t;
					mainMenuGroup.add(t);
				})
				.push();

		/** options */

		UIObjectFactory
				.create(GradientQuadUIObject.class,
						new Transform3D(),
						GradientDirection.UV_X,
						GameEngineUtils.hexToColorToVec4f("317dac8c"))
				.then(workers, (Consumer<GradientQuadUIObject>) (GradientQuadUIObject t) -> {
					t.getTransform().translationSet(0.9f, -0.1f, 0).scaleSet(2, 1, 2).rotationSet(0, (float) Math.PI, 0).updateMatrix();
					blueGradient = t;
					optionsMenuGroup.add(t);
				})
				.push();

		UIObjectFactory
				.create(BackButtonUIObject.class,
						uiTextData,
						new Transform3D(new Vector3f(-0.5f, 0, -0.5f), new Quaternionf(), new Vector3f(0.5f)))
				.then(workers, (Consumer<BackButtonUIObject>) (t) -> {
					optionsMenuGroup.add(t);
				})
				.push();

		UIObjectFactory
				.create(CursorUIObject.class, this, new Transform3D(new Vector3f(x, 0.01f, 0.25f), new Quaternionf(), new Vector3f(0.15f)))
				.then(workers, (Consumer<CursorUIObject>) (btn) -> {
					btn.setTargetedObject(mainMenuGroup.get(0));
					cursor = btn;
				})
				.push();

	}

	@Override
	public void update(
			WindowInputHandler inputHandler,
			float dTime,
			DeferredCompositor compositor,
			WorkerDispatcher workers,
			Dispatcher render) {
		super.update(inputHandler, dTime, compositor, workers, render);

		if (cursor != null) {
			hovering.stream().filter(c -> c instanceof TextUIObject).findFirst().ifPresent(cursor::setTargetedObject);
		}

		if (greenGradient != null) {
			greenGradient.getTransform().getTranslation().x = -camera.getProjection().getAspectRatio()
					+ (greenGradient.getTransform().getScale().x * (float) (greenGradient.getBounds().getWidth() / 2));
			greenGradient.getTransform().updateMatrix();
		}

		if (blueGradient != null) {
			blueGradient.getTransform().getTranslation().x = camera.getProjection().getAspectRatio()
					- (blueGradient.getTransform().getScale().x * (float) (blueGradient.getBounds().getWidth() / 2));
			blueGradient.getTransform().updateMatrix();
		}

		if (inputHandler.wasResized()) {
			restPositions[OPTIONS].x = camera.getProjection().getAspectRatio() * 2;
			if (currentGroup != OPTIONS) {
				optionsMenuGroup.getTransform().translationSet(restPositions[OPTIONS]).updateMatrix();
			}
		}

		if (targetGroup != currentGroup) {
			final OffsetUIObjectGroup target = groups[targetGroup];
			final OffsetUIObjectGroup current = groups[currentGroup];

			final float duration = 0.8f;
			progress = org.joml.Math.clamp(0, 1, progress + dTime / duration);

			final Vector3f targetPos = new Vector3f(0, 0, 0);
			final Vector3f targetStart = new Vector3f(restPositions[targetGroup]);

			if (PCUtils.compare(progress, 1, 1e-9)) {
				target.getTransform().setTranslation(targetPos).updateMatrix();

				// final Vector3f slideDir = new Vector3f(targetStart).normalize();
				current.getTransform().setTranslation(targetStart.negate()).updateMatrix();

				restPositions[currentGroup] = new Vector3f(current.getTransform().getTranslation());
				restPositions[targetGroup] = new Vector3f(targetPos);
				currentGroup = targetGroup;
				progress = 1;
			} else {
				final Vector3f targetOffset = new Vector3f(targetStart).lerp(targetPos, Interpolators.QUAD_IN_OUT.evaluate(progress));
				target.getTransform().setTranslation(targetOffset).updateMatrix();

				final Vector3f currentOffset = new Vector3f(targetOffset).sub(targetStart);
				current.getTransform().setTranslation(currentOffset).updateMatrix();
			}

			cursor.setCirclingMouse(super.getMouseCoords(inputHandler));
		}
	}

	public void startTransition(int target) {
		if (progress != 0 && progress != 1)
			return;
		System.err.println("Going to: " + target);
		this.targetGroup = target;
		this.progress = 0;
	}

}
