package lu.kbra.plant_game.engine.scene.ui;

import java.util.Optional;
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
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.entity.ui.text.BackwardButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ForwardButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.LeftButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.RightButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextFieldUIObject;
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

	public static final float GRADIENT_DEPTH = -0.1f;

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
	protected BackButtonUIObject optionsBackBtn;

	public MainMenuUIScene(CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(Dispatcher workers, Dispatcher renderDispatcher) {
		super.addEntities(mainMenuGroup, optionsMenuGroup);

		final TextData uiTextData = new TextData(new Vector2f(0.2f), TextAlignment.TEXT_CENTER, -1);

		final float x = -0.8f;

		/** main menu */

		UIObjectFactory.create(PlayButtonUIObject.class, mainMenuGroup, uiTextData, new Transform3D(new Vector3f(x, 0, -0.25f))).push();
		UIObjectFactory.create(OptionsButtonUIObject.class, mainMenuGroup, uiTextData, new Transform3D(new Vector3f(x, 0, 0))).push();
		UIObjectFactory.create(QuitButtonUIObject.class, mainMenuGroup, uiTextData, new Transform3D(new Vector3f(x, 0, 0.25f))).push();

		UIObjectFactory
				.create(LargeLogoUIObject.class,
						mainMenuGroup,
						new Transform3D(new Vector3f(0, 0, -0.75f), new Quaternionf(), new Vector3f(2)))
				.push();

		UIObjectFactory
				.create(GradientQuadUIObject.class,
						mainMenuGroup,
						new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0), new Quaternionf(), new Vector3f(2.5f, 1, 2)),
						GradientDirection.UV_X,
						GameEngineUtils.hexToColorToVec4f("3b784a"))
				.then(workers, (Consumer<GradientQuadUIObject>) (GradientQuadUIObject t) -> {
					greenGradient = t;
				})
				.push();

		/** options */

		UIObjectFactory
				.create(GradientQuadUIObject.class,
						optionsMenuGroup,
						new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0), new Quaternionf().rotateY((float) Math.PI),
								new Vector3f(2.5f, 1, 2)),
						GradientDirection.UV_X,
						GameEngineUtils.hexToColorToVec4f("317dac8c"))
				.then(workers, (Consumer<GradientQuadUIObject>) (GradientQuadUIObject t) -> {
					blueGradient = t;
				})
				.push();

		UIObjectFactory
				.create(BackButtonUIObject.class,
						optionsMenuGroup,
						uiTextData,
						new Transform3D(new Vector3f(-0.5f, 0, -0.5f), new Quaternionf(), new Vector3f(0.5f)))
				.then(workers, (Consumer<BackButtonUIObject>) (t) -> {
					optionsBackBtn = t;
				})
				.push();

		final TextData uiSmallLeftTextData = new TextData(new Vector2f(0.1f), TextAlignment.TEXT_LEFT, -1);

		UIObjectFactory.create(ForwardButtonUIObject.class, optionsMenuGroup, uiSmallLeftTextData).push();
		UIObjectFactory.create(BackwardButtonUIObject.class, optionsMenuGroup, uiSmallLeftTextData).push();
		UIObjectFactory.create(LeftButtonUIObject.class, optionsMenuGroup, uiSmallLeftTextData).push();
		UIObjectFactory.create(RightButtonUIObject.class, optionsMenuGroup, uiSmallLeftTextData).push();

		/* common */

		UIObjectFactory
				.create(CursorUIObject.class, this, new Transform3D(new Vector3f(x, 0.01f, 0.25f), new Quaternionf(), new Vector3f(0.15f)))
				.then(workers, (Consumer<CursorUIObject>) (btn) -> {
					btn.forceCirclingMouse();
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
			final Optional<UIObject> focusCandidate = hovering
					.stream()
					.filter(c -> c instanceof TextUIObject && !(c instanceof TextFieldUIObject))
					.findFirst();
			if (cursor.isCirclingMouse() && focusCandidate.isEmpty()) {
				cursor.setCirclingMouse(getMouseCoords(inputHandler));
			} else {
				focusCandidate.ifPresent(cursor::setTargetedObject);
			}
		}

		restPositions[OPTIONS].x = camera.getProjection().getAspectRatio() * 2;
		if (currentGroup != OPTIONS) {
			optionsMenuGroup.getTransform().translationSet(restPositions[OPTIONS]).updateMatrix();
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

		if (optionsBackBtn != null) {
			optionsBackBtn.getTransform().getTranslation().x = -camera.getProjection().getAspectRatio()
					+ (optionsBackBtn.getTransform().getScale().x * (float) (optionsBackBtn.getBounds().getBounds2D().getWidth() / 2));
			optionsBackBtn.getTransform().getTranslation().z = -1 + (float) (optionsBackBtn.getBounds().getBounds2D().getHeight() / 2);
			optionsBackBtn.getTransform().updateMatrix();
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
		if (progress != 0 && progress != 1) {
			return;
		}
		this.targetGroup = target;
		this.progress = 0;
	}

}
