package lu.kbra.plant_game.engine.scene.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextFieldUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LargeLogoUIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.concurrency.TriggerLatch;

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
			new Transform3D(new Vector3f(this.restPositions[OPTIONS])));
	protected LayoutOffsetUIObjectGroup optionsKeysMenuGroup = new LayoutOffsetUIObjectGroup("option.keys",
			new CenteringFlowLayout(true, 0.0f), this.optionsMenuGroup);

	protected OffsetUIObjectGroup[] groups = new OffsetUIObjectGroup[] { this.mainMenuGroup, null, this.optionsMenuGroup, null };

	protected CursorUIObject cursor;

	protected GradientQuadUIObject greenGradient;
	protected GradientQuadUIObject blueGradient;
	protected BackButtonUIObject optionsBackBtn;

	public MainMenuUIScene(final CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		super.addEntities(this.mainMenuGroup, this.optionsMenuGroup);

		final TextData uiTextData = new TextData(new Vector2f(0.2f), TextAlignment.TEXT_CENTER, -1);

		final float x = -0.8f;

		/** main menu */

		UIObjectFactory
				.create(PlayButtonUIObject.class, this.mainMenuGroup, uiTextData, new Transform3D(new Vector3f(x, 0, -0.25f)))
				.push();
		UIObjectFactory.create(OptionsButtonUIObject.class, this.mainMenuGroup, uiTextData, new Transform3D(new Vector3f(x, 0, 0))).push();
		UIObjectFactory.create(QuitButtonUIObject.class, this.mainMenuGroup, uiTextData, new Transform3D(new Vector3f(x, 0, 0.25f))).push();

		UIObjectFactory
				.create(LargeLogoUIObject.class,
						this.mainMenuGroup,
						new Transform3D(new Vector3f(0, 0, -0.75f), new Quaternionf(), new Vector3f(2)))
				.push();

		UIObjectFactory
				.create(GradientQuadUIObject.class,
						this.mainMenuGroup,
						new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0), new Quaternionf(), new Vector3f(2.5f, 1, 2)),
						GradientDirection.UV_X,
						GameEngineUtils.hexToColorToVec4f("3b784a"))
				.then(workers, (Consumer<GradientQuadUIObject>) (final GradientQuadUIObject t) -> {
					this.greenGradient = t;
				})
				.push();

		/** options */

		UIObjectFactory
				.create(GradientQuadUIObject.class,
						this.optionsMenuGroup,
						new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0), new Quaternionf().rotateY((float) Math.PI),
								new Vector3f(2.5f, 1, 2)),
						GradientDirection.UV_X,
						GameEngineUtils.hexToColorToVec4f("317dac8c"))
				.then(workers, (Consumer<GradientQuadUIObject>) (final GradientQuadUIObject t) -> {
					this.blueGradient = t;
				})
				.push();

		UIObjectFactory
				.create(BackButtonUIObject.class,
						this.optionsMenuGroup,
						uiTextData,
						new Transform3D(new Vector3f(-0.5f, 0, -0.5f), new Quaternionf(), new Vector3f(0.5f)))
				.then(workers, (Consumer<BackButtonUIObject>) t -> {
					this.optionsBackBtn = t;
				})
				.push();

		final TextData uiSmallLeftTextData = new TextData(new Vector2f(0.125f), TextAlignment.TEXT_LEFT, -1);

		final List<OptionKeyUIObject> all = new ArrayList<>();
		final TriggerLatch latch = new TriggerLatch(StandardKeyOption.values().length, () -> new TaskFuture<>(workers, this::updateKeys).push());
		for (final StandardKeyOption key : StandardKeyOption.values()) {
			uiSmallLeftTextData.setBufferSize(25);
			uiSmallLeftTextData.setName("options.keys" + key);
			UIObjectFactory
					.create(OptionKeyUIObject.class,
							this.optionsKeysMenuGroup,
							uiSmallLeftTextData,
							key.name().toLowerCase(),
							Scale2dDir.BOTH,
							new Transform3DPivot())
					.then(workers, (Consumer<OptionKeyUIObject>) t -> {
						all.add(t);
						latch.decrement();
					})
					.push();
		}
		uiSmallLeftTextData.setBufferSize(-1);
		uiSmallLeftTextData.setName(null);

		/* common */

		UIObjectFactory
				.create(CursorUIObject.class, this, new Transform3D(new Vector3f(x, 0.01f, 0.25f), new Quaternionf(), new Vector3f(0.15f)))
				.then(workers, (Consumer<CursorUIObject>) btn -> {
					btn.forceCirclingMouse();
					this.cursor = btn;
				})
				.push();
	}

	private void updateKeys() {
		final MappingInputHandler inputHandler = PGLogic.INSTANCE.getInputHandler();

		this.optionsKeysMenuGroup
				.getSubEntities()
				.parallelStream()
				.filter(OptionKeyUIObject.class::isInstance)
				.map(e -> (OptionKeyUIObject) e)
				.forEach(e -> e.setKeyValue(inputHandler.getMappedInputName(e.getKeyOption().getPhysicalKey())));

		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
			this.optionsKeysMenuGroup
					.getSubEntities()
					.stream()
					.filter(ProgrammaticTextUIObject.class::isInstance)
					.map(e -> (ProgrammaticTextUIObject) e)
					.forEach(e -> e.getTextEmitter().updateText());
		});

		this.optionsKeysMenuGroup.doLayout();
	}

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final float dTime,
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		super.update(inputHandler, dTime, compositor, workers, render);

		if (inputHandler.wasResized()) {
			this.camera.getProjection().update(inputHandler.getWindowSize());

			this.optionsKeysMenuGroup.doLayout();
		}

		if (this.cursor != null) {
			final Optional<UIObject> focusCandidate = this.hovering
					.stream()
					.filter(c -> c instanceof TextUIObject && !(c instanceof TextFieldUIObject))
					.findFirst();
			if (this.cursor.isCirclingMouse() && focusCandidate.isEmpty()) {
				this.cursor.setCirclingMouse(this.getMouseCoords(inputHandler));
			} else {
				if (currentGroup == OPTIONS) {
					cursor.setOffsetX(-0.1f);
				}
				focusCandidate.ifPresent(this.cursor::setTargetedObject);
			}
		}

		this.restPositions[OPTIONS].x = this.camera.getProjection().getAspectRatio() * 2;
		if (this.currentGroup != OPTIONS) {
			this.optionsMenuGroup.getTransform().translationSet(this.restPositions[OPTIONS]).updateMatrix();
		}

		if (this.greenGradient != null) {
			this.greenGradient.getTransform().getTranslation().x = -this.camera.getProjection().getAspectRatio()
					+ (this.greenGradient.getTransform().getScale().x * (float) (this.greenGradient.getBounds().getWidth() / 2));
			this.greenGradient.getTransform().updateMatrix();
		}

		if (this.blueGradient != null) {
			this.blueGradient.getTransform().getTranslation().x = this.camera.getProjection().getAspectRatio()
					- (this.blueGradient.getTransform().getScale().x * (float) (this.blueGradient.getBounds().getWidth() / 2));
			this.blueGradient.getTransform().updateMatrix();
		}

		if (this.optionsBackBtn != null) {
			this.optionsBackBtn.getTransform().getTranslation().x = -this.camera.getProjection().getAspectRatio()
					+ (this.optionsBackBtn.getTransform().getScale().x
							* (float) (this.optionsBackBtn.getBounds().getBounds2D().getWidth() / 2));
			this.optionsBackBtn.getTransform().getTranslation().z = -1
					+ (float) (this.optionsBackBtn.getBounds().getBounds2D().getHeight() / 2);
			this.optionsBackBtn.getTransform().updateMatrix();
		}

		if (this.targetGroup != this.currentGroup) {
			final OffsetUIObjectGroup target = this.groups[this.targetGroup];
			final OffsetUIObjectGroup current = this.groups[this.currentGroup];

			final float duration = 0.8f;
			this.progress = org.joml.Math.clamp(0, 1, this.progress + dTime / duration);

			final Vector3f targetPos = new Vector3f(0, 0, 0);
			final Vector3f targetStart = new Vector3f(this.restPositions[this.targetGroup]);

			if (PCUtils.compare(this.progress, 1, 1e-9)) {
				target.getTransform().setTranslation(targetPos).updateMatrix();

				// final Vector3f slideDir = new Vector3f(targetStart).normalize();
				current.getTransform().setTranslation(targetStart.negate()).updateMatrix();

				this.restPositions[this.currentGroup] = new Vector3f(current.getTransform().getTranslation());
				this.restPositions[this.targetGroup] = new Vector3f(targetPos);
				this.currentGroup = this.targetGroup;
				this.progress = 1;
			} else {
				final Vector3f targetOffset = new Vector3f(targetStart).lerp(targetPos, Interpolators.QUAD_IN_OUT.evaluate(this.progress));
				target.getTransform().setTranslation(targetOffset).updateMatrix();

				final Vector3f currentOffset = new Vector3f(targetOffset).sub(targetStart);
				current.getTransform().setTranslation(currentOffset).updateMatrix();
			}

			this.cursor.setCirclingMouse(super.getMouseCoords(inputHandler));
		}
	}

	public void startTransition(final int target) {
		if (this.progress != 0 && this.progress != 1) {
			return;
		}
		this.targetGroup = target;
		this.progress = 0;
	}

}
