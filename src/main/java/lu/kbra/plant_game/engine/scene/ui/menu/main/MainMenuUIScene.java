package lu.kbra.plant_game.engine.scene.ui.menu.main;

import java.util.Optional;
import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.concurrency.ListTriggerLatch;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory.TextData;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollContainerUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.ScrollBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.VolumeSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.OptionKeyUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.VolumeTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextFieldUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LargeLogoUIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.MarginFlowLayout;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.StandardKeyOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

public class MainMenuUIScene extends UIScene {

	public static final int MAIN = 0;
	public static final int PLAY = 1;
	public static final int OPTIONS = 2;
	public static final int QUIT = 3;

	public static final float GRADIENT_DEPTH = -0.1f;

	public static final float OPTIONS_SCROLL_SPEED = 0.1f;
	public static final float PLAY_SCROLL_SPEED = 0.1f;
	public static final int OPTIONS_COLUMN_COUNT = 25;

	protected int currentGroup = 0;
	protected int targetGroup = 0;
	protected float progress = 0;

	protected Vector3fc[] restPositions = { new Vector3f(), new Vector3f(0, 5, 0), new Vector3f(5, 0, 0), null };

	protected OffsetUIObjectGroup mainMenuGroup = new OffsetUIObjectGroup("main", new Transform3D(new Vector3f(this.restPositions[MAIN])));

	protected LayoutOffsetUIObjectGroup mainLeftMenuGroup = new LayoutOffsetUIObjectGroup(
			"main.left",
			new MarginFlowLayout(true, 0.02f, 0.3f, 0, 0.5f, 0.5f, MarginFlowLayout.LEFT),
			this.mainMenuGroup);
	protected LayoutOffsetUIObjectGroup mainButtonsMenuGroup = new LayoutOffsetUIObjectGroup(
			"main.buttons",
			new FlowLayout(true, 0.02f),
			this.mainLeftMenuGroup);

	protected ScrollContainerUIObjectGroup optionsMenuGroup = new ScrollContainerUIObjectGroup(
			"options",
			this.restPositions[OPTIONS],
			Direction.SOUTH,
			0.05f,
			new FlowLayout(true, 0.0f));
	protected LayoutScrollDrivenUIObjectGroup optionsEntriesMenuGroup = (LayoutScrollDrivenUIObjectGroup) this.optionsMenuGroup
			.getScrollContent();

	protected ScrollContainerUIObjectGroup playMenuGroup = new ScrollContainerUIObjectGroup(
			"play",
			this.restPositions[PLAY],
			Direction.EAST,
			0.05f);
	protected ScrollDrivenUIObjectGroup playContentMenuGroup = this.playMenuGroup.getScrollContent();

	protected OffsetUIObjectGroup[] groups = new OffsetUIObjectGroup[] {
			this.mainMenuGroup,
			this.playMenuGroup,
			this.optionsMenuGroup,
			null };

	protected CursorUIObject cursor;

	protected GradientQuadUIObject greenGradient;
	protected GradientQuadUIObject blueGradient;
	protected BackButtonUIObject optionsBackBtn;

	public MainMenuUIScene(final CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		super.addEntities(this.mainMenuGroup, this.optionsMenuGroup, this.playMenuGroup);

		/* main menu */
		this.buildMainMenu(workers, renderDispatcher);

		/* option content */
		this.buildOptionsMenu(workers, renderDispatcher);

		/* play content */
		this.buildPlayMenu(workers, renderDispatcher);

		/* common */
		UIObjectFactory
				.create(CursorUIObject.class, this, new Transform3D(new Vector3f(0, 0.01f, 0.25f), new Quaternionf(), new Vector3f(0.15f)))
				.then(workers, (Consumer<CursorUIObject>) btn -> {
					btn.forceCirclingMouse();
					this.cursor = btn;
				})
				.push();
	}

	private void buildPlayMenu(final Dispatcher workers, final Dispatcher renderDispatcher) {
		UIObjectFactory
				.create(ScrollBarUIObject.class,
						this.playMenuGroup,
						new Transform3D(),
						ColorMaterial.GRAY,
						Direction.EAST,
						new Vector2f(-1, 1),
						new Vector2f(0.05f, 0.2f),
						PLAY_SCROLL_SPEED)
				.then(workers, (Consumer<ScrollBarUIObject>) this.playMenuGroup::setScrollBar)
				.push();

		UIObjectFactory
				.create(CursorUIObject.class,
						this.playContentMenuGroup,
						new Transform3D(new Vector3f(0, 0.01f, 0.25f), new Quaternionf(), new Vector3f(0.15f)))
				.push();
	}

	private void buildOptionsMenu(final Dispatcher workers, final Dispatcher renderDispatcher) {
		final TextData uiSmallLeftTextData = new TextData(new Vector2f(0.1f), TextAlignment.TEXT_CENTER, -1);

		final LayoutOffsetUIObjectGroup optionsVolumeGroup = new LayoutOffsetUIObjectGroup(
				"options.volume",
				new EdgeStickLayout(true, 0, uiSmallLeftTextData.getCharSize().x() * OPTIONS_COLUMN_COUNT));

		this.optionsEntriesMenuGroup.add(optionsVolumeGroup);
		this.optionsEntriesMenuGroup.add(UIObjectFactory.createVerticalSpacer(2 * uiSmallLeftTextData.getCharSize().y()));

		final ListTriggerLatch<OptionKeyUIObject> optionKeyGroupLatch = new ListTriggerLatch<>(
				StandardKeyOption.values().length,
				l -> workers.post(() -> {
					l.forEach(this.optionsEntriesMenuGroup::add);
					this.updateKeys();
				}));
		for (final StandardKeyOption key : StandardKeyOption.values()) {
			uiSmallLeftTextData.setBufferSize(OPTIONS_COLUMN_COUNT);
			uiSmallLeftTextData.setName("options.keys" + key);
			UIObjectFactory
					.create(OptionKeyUIObject.class,
							optionKeyGroupLatch,
							uiSmallLeftTextData,
							key.name().toLowerCase(),
							Scale2dDir.BOTH,
							new Transform3D())
					.push();
		}
		uiSmallLeftTextData.setBufferSize(-1);
		uiSmallLeftTextData.setName(null);

		/* volume */
		final ListTriggerLatch<UIObject> optionsVolumeGroupLatch = new TextSliderListTriggerLatch<>(
				workers,
				optionsVolumeGroup,
				VolumeTextUIObject.class,
				VolumeSliderUIObject.class);

		uiSmallLeftTextData.setTextAlignment(TextAlignment.TEXT_LEFT);
		UIObjectFactory.create(VolumeTextUIObject.class, optionsVolumeGroupLatch, uiSmallLeftTextData, new Transform3DPivot()).push();
		UIObjectFactory.create(VolumeSliderUIObject.class, optionsVolumeGroupLatch, uiSmallLeftTextData, new Transform3DPivot()).push();

		UIObjectFactory
				.create(ScrollBarUIObject.class,
						this.optionsMenuGroup,
						new Transform3D(),
						ColorMaterial.GRAY,
						Direction.NORTH,
						new Vector2f(0.8f, -0.8f),
						new Vector2f(0.05f, 0.2f),
						OPTIONS_SCROLL_SPEED)
				.then(workers, (Consumer<ScrollBarUIObject>) this.optionsMenuGroup::setScrollBar)
				.push();
	}

	private void buildMainMenu(final Dispatcher workers, final Dispatcher renderDispatcher) {
		final TextData uiTextData = new TextData(new Vector2f(0.2f), TextAlignment.TEXT_CENTER, -1);

		final ListTriggerLatch<UIObject> mainButtonsLatch = new ListTriggerLatch<>(3, l -> workers.post(() -> {
			l.forEach(this.mainButtonsMenuGroup::add);
			this.mainLeftMenuGroup.doLayout();
		}));

		UIObjectFactory.create(PlayButtonUIObject.class, mainButtonsLatch, uiTextData, new Transform3D()).push();
		UIObjectFactory.create(OptionsButtonUIObject.class, mainButtonsLatch, uiTextData, new Transform3D()).push();
		UIObjectFactory.create(QuitButtonUIObject.class, mainButtonsLatch, uiTextData, new Transform3D()).push();

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
						new Transform3D(
								new Vector3f(0, GRADIENT_DEPTH, 0),
								new Quaternionf().rotateY((float) Math.PI),
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
	}

	private void updateKeys() {
		final MappingInputHandler inputHandler = PGLogic.INSTANCE.getInputHandler();

		this.optionsEntriesMenuGroup
				.getSubEntities()
				.parallelStream()
				.filter(OptionKeyUIObject.class::isInstance)
				.map(e -> (OptionKeyUIObject) e)
				.forEach(e -> e.setKeyValue(inputHandler.getInputName(e.getKeyOption().getPhysicalKey())));

		final OptionKeyUIObject example = this.optionsEntriesMenuGroup
				.getSubEntities()
				.parallelStream()
				.filter(OptionKeyUIObject.class::isInstance)
				.map(e -> (OptionKeyUIObject) e)
				.findFirst()
				.orElseThrow(IllegalStateException::new);

		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> {
			this.optionsEntriesMenuGroup
					.getSubEntities()
					.stream()
					.filter(ProgrammaticTextUIObject.class::isInstance)
					.map(e -> (ProgrammaticTextUIObject) e)
					.forEach(e -> e.getTextEmitter().updateText());
		});

		((FlowLayout) this.optionsEntriesMenuGroup.getLayout())
				.setGap((float) (example.getBounds().getBounds2D().getHeight()
						* (example.getTargetScale(true).z() - example.getTargetScale(false).z())));

		this.optionsEntriesMenuGroup.doLayout();

		this.optionsEntriesMenuGroup.getTransform().translationSet(0, 0, -1f + 0.3f).updateMatrix();
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final float dTime, final UpdateFrameState frameState) {
		super.input(inputHandler, dTime, frameState);

		if (inputHandler.wasResized()) {
			this.camera.getProjection().update(inputHandler.getWindowSize());

			this.optionsEntriesMenuGroup.doLayout();
			this.mainLeftMenuGroup.doLayout();
		}

		final OffsetUIObjectGroup current = this.groups[this.currentGroup];
		if (current instanceof final ScrollContainerUIObjectGroup scrollContainer && scrollContainer.getScrollBar() != null) {
			scrollContainer.updateScrollBar();
			scrollContainer.getScrollBar().addScrollPosition((float) inputHandler.getMouseScroll().y);
		}

		frameState.uiSceneCaughtMouseInput = true;

		if (this.cursor != null) {
			final Optional<UIObject> focusCandidate = this.hovering
					.stream()
					.filter(c -> c instanceof TextUIObject && !(c instanceof TextFieldUIObject))
					.findFirst();
			if (this.cursor.isCirclingMouse() && focusCandidate.isEmpty()) {
				this.cursor.setCirclingMouse(this.getMouseCoords(inputHandler));
			} else {
				if (this.currentGroup == OPTIONS) {
					this.cursor.setOffsetX(-0.1f);
				}
				focusCandidate.ifPresent(this.cursor::setTargetedObject);
			}
		}
	}

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final float dTime,
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		super.update(inputHandler, dTime, compositor, workers, render);

		if (this.cursor != null && this.currentGroup == MAIN && this.mainButtonsMenuGroup.size() > 1) {
			this.cursor.setActive(true);

			final float z1 = ((AbsoluteTransformOwner) this.mainButtonsMenuGroup.get(0))
					.getAbsoluteTransform()
					.getTranslation(new Vector3f()).z;
			final float z2 = ((AbsoluteTransformOwner) this.mainButtonsMenuGroup.get(1))
					.getAbsoluteTransform()
					.getTranslation(new Vector3f()).z;

			this.cursor.setSnapPhase(Math.max(z1, z2));
			this.cursor.setSnapAngle(Math.abs(Math.min(z1, z2) - Math.max(z1, z2)));
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
			this.cursor.setActive(this.targetGroup == MAIN);

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
