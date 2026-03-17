package lu.kbra.plant_game.base.scene.menu.main;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.ListTriggerLatch;
import lu.kbra.pclib.pointer.prim.IntPointer;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.base.scene.overlay.group.impl.MarginAnchoredUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.impl.ParentUIObjectGroup;
import lu.kbra.plant_game.base.scene.world.MainMenuWorldScene;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.impl.TransformOwner;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.LevelButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.data.GradientDirection;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.gradient.AnchoredGradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.icon.LargeLogoUIObject;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.VolumeSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.VolumeTextUIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor2D;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.scene.ui.layout.LayoutOwner;
import lu.kbra.plant_game.engine.scene.world.WorldLevelScene;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.plugin.registry.LevelRegistry;
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
	public static final int RESUME = 4;

	public static final int[] BACK_INDICES = { MAIN, MAIN, MAIN, MAIN, PLAY };
	public static final float TRANSITION_DURATION = 0.8f;

	public static final float GRADIENT_DEPTH = -0.1f;

	public static final float OPTIONS_SCROLL_SPEED = 0.1f;
	public static final float PLAY_SCROLL_SPEED = 0.1f;
	public static final int OPTIONS_COLUMN_COUNT = 25;

	protected int currentGroup = 0;
	protected int targetGroup = 0;
	protected float progress = 0;

	protected Vector3fc[] restPositions = { new Vector3f(0), new Vector3f(5, 0, 5), new Vector3f(8, 0, 0), null, new Vector3f(5, 0, 10) };

	protected ParentUIObjectGroup mainMenuGroup = new ParentUIObjectGroup("main",
			new AnchorLayout(),
			new Transform3D(new Vector3f(this.restPositions[MAIN])));
	protected MarginAnchoredUIObjectGroup mainButtonsMenuGroup = new MarginAnchoredUIObjectGroup("main.buttons",
			new FlowLayout(true, 0.06f, Anchor2D.LEADING),
			this.mainMenuGroup,
			Anchor.CENTER_LEFT,
			Anchor.CENTER_LEFT,
			0.25f);

	protected ParentUIObjectGroup optionsMenuGroup = new ParentUIObjectGroup("options",
			new AnchorLayout(),
			new Transform3D(this.restPositions[OPTIONS]));
	protected OptionsUIObjectGroup optionsVerticalGroup = new OptionsUIObjectGroup(this.optionsMenuGroup);
	protected UIObjectGroup optionsEntriesGroup = this.optionsVerticalGroup.getContent();

	protected ParentUIObjectGroup playMenuGroup = new ParentUIObjectGroup("play",
			new AnchorLayout(),
			new Transform3D(this.restPositions[PLAY]));
	protected PlayUIObjectGroup playHorizontalGroup = new PlayUIObjectGroup(this.playMenuGroup);
	protected UIObjectGroup playContentGroup = this.playHorizontalGroup.getContent();
	protected PlayInfoUIObjectGroup playInfoGroup = new PlayInfoUIObjectGroup(this.playMenuGroup);

	protected ParentUIObjectGroup resumeMenuGroup = new ParentUIObjectGroup("resume",
			new AnchorLayout(),
			new Transform3D(this.restPositions[RESUME]));
	protected ResumeUIObjectGroup resumeInfoGroup = new ResumeUIObjectGroup(this.resumeMenuGroup);

	protected OffsetUIObjectGroup[] groups = new OffsetUIObjectGroup[] {
			this.mainMenuGroup,
			this.playMenuGroup,
			this.optionsMenuGroup,
			null,
			this.resumeMenuGroup };

	protected Layout layout;

	final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.2f));
	final Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_LEFT);

	protected final MainMenuWorldScene worldScene;

	public MainMenuUIScene(final CacheManager parent, final MainMenuWorldScene worldScene) {
		super("main-menu", parent);
		this.worldScene = worldScene;
	}

	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		final float size = 1f / this.getCamera().getProjection().getSize();
		Arrays.stream(this.restPositions).filter(Objects::nonNull).forEach(c -> ((Vector3f) c).mul(size));

		super.addAll(this.mainMenuGroup, this.optionsMenuGroup, this.playMenuGroup, this.resumeMenuGroup);

		/* main menu */
		this.buildMainMenu();

		/* option content */
		this.buildOptionsMenu();

		/* play content */
		this.buildPlayMenu();

		this.buildResumeMenu();

		this.stream()
				.filter(TransformOwner.class::isInstance)
				.map(TransformOwner.class::cast)
				.filter(TransformOwner::hasTransform)
				.forEach(c -> c.getTransform().updateMatrix());
	}

	private void buildResumeMenu() {
		this.resumeInfoGroup.init();
	}

	private void buildPlayMenu() {
		final int count = LevelRegistry.LEVELS.size();
		final ListTriggerLatch<LevelButtonUIObject> btns = new ListTriggerLatch<>(count, list -> {
			Collections.sort(list);
			this.playContentGroup.addAll(list);
		});

		final float startPosX = -1;
		final IntPointer ip = new IntPointer(0);
		LevelRegistry.LEVELS.forEach(ld -> {
			final int j = ip.increment();
			UIObjectFactory.create(LevelButtonUIObject.class)
					.set(i -> i.setTransform(new Transform3D(new Vector3f(startPosX + 0.8f * j, 0, 0.6f * j % (2 - 0.5f) - 1),
							new Quaternionf().rotateY((float) Math.random()),
							new Vector3f(0.5f))))
					.set(i -> i.setLevelDefinition(ld))
					.latch(btns)
					.push();
		});

		UIObjectFactory
				.createText(BackButtonUIObject.class,
						OptionalInt.empty(),
						this.SMALL_TEXT_CHAR_SIZE,
						this.SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.add(this.playMenuGroup)
				.set(i -> i.setAnchors(Anchor.TOP_LEFT, Anchor.TOP_LEFT))
				.set(i -> i.setMargin(0.05f))
				.push();

		this.playInfoGroup.init();
		this.playInfoGroup.setActive(false);
	}

	private void buildOptionsMenu() {
		final float charSize = 0.1f;

		new OptionVolumeUIObjectGroup(this.optionsEntriesGroup).init(VolumeTextUIObject.class, VolumeSliderUIObject.class, charSize);

		this.optionsEntriesGroup.add(SpacerUIObject.getVerticalSpacer(0.1f));

		for (final DefaultKeyOption key : DefaultKeyOption.values()) {
			new OptionKeyUIObjectGroup(key, this.optionsEntriesGroup).init(PGLogic.INSTANCE.getInputHandler(), charSize);
		}

		final float size = 1f / this.getCamera().getProjection().getSize();

		UIObjectFactory.create(AnchoredGradientQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0),
						new Quaternionf().rotateY((float) Math.PI),
						new Vector3f(2.5f, 1, 2).mul(size))))
				.set(i -> i.setDirection(GradientDirection.UV_X))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("317dac8c")))
				.set(i -> i.setAnchors(Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT))
				.add(this.optionsMenuGroup)
				.push();

		UIObjectFactory.create(AnchoredGradientQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, GRADIENT_DEPTH - 0.1f, 0),
						new Quaternionf().rotateY((float) Math.PI),
						new Vector3f(5f, 1, 2).mul(size))))
				.set(i -> i.setDirection(GradientDirection.UV_Y))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("000000ff")))
				.set(i -> i.setAnchors(Anchor.TOP_LEFT, Anchor.TOP_LEFT))
				.add(this.optionsMenuGroup)
				.push();

		UIObjectFactory
				.createText(BackButtonUIObject.class,
						OptionalInt.empty(),
						this.SMALL_TEXT_CHAR_SIZE,
						this.SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.add(this.optionsMenuGroup)
				.set(i -> i.setAnchors(Anchor.TOP_LEFT, Anchor.TOP_LEFT))
				.set(i -> i.setMargin(0.05f))
				.push();
	}

	private void buildMainMenu() {
		final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.2f));
		final Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_CENTER);

		final float size = 1f / this.getCamera().getProjection().getSize();

		UIObjectFactory
				.createText(PlayButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.add(this.mainButtonsMenuGroup)
				.push();
		UIObjectFactory
				.createText(OptionsButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.add(this.mainButtonsMenuGroup)
				.push();
		UIObjectFactory
				.createText(QuitButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.add(this.mainButtonsMenuGroup)
				.push();

		UIObjectFactory.create(LargeLogoUIObject.class)
				.set(i -> i.setTransform(new Transform3D(2)))
				.set(i -> i.setAnchors(Anchor.CENTER_CENTER, Anchor.TOP_CENTER))
				.set(i -> i.setMargin(0.25f))
				.add(this.mainMenuGroup)
				.push();

		UIObjectFactory.create(AnchoredGradientQuadUIObject.class)
				.set(i -> i.setTransform(
						new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0), new Quaternionf(), new Vector3f(2.5f, 1, 2).mul(size))))
				.set(i -> i.setDirection(GradientDirection.UV_X))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("3b784a")))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.add(this.mainMenuGroup)
				.push();
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		super.input(inputHandler, frameState);
	}

	@Override
	public void update(
			final WindowInputHandler inputHandler,
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		super.update(inputHandler, compositor, workers, render);

		if (this.targetGroup != this.currentGroup) {

			final OffsetUIObjectGroup target = this.groups[this.targetGroup];
			final OffsetUIObjectGroup current = this.groups[this.currentGroup];

			if (target instanceof final LayoutOwner lo) {
				lo.doLayout();
			}
			if (current instanceof final LayoutOwner lo) {
				lo.doLayout();
			}

			final float dTime = inputHandler.dTime();
			this.progress = org.joml.Math.clamp(0, 1, this.progress + dTime / TRANSITION_DURATION);

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

		}
	}

	public void startTransition(final int target) {
		if (this.progress != 0 && this.progress != 1) {
			return;
		}
		this.targetGroup = target;
		this.progress = 0;

		final WorldLevelScene wls = PGLogic.INSTANCE.getWorldScene();
		if (wls instanceof final MainMenuWorldScene mmws) {
			mmws.startTransition(target);
		}
	}

	public PlayInfoUIObjectGroup getPlayInfoGroup() {
		return this.playInfoGroup;
	}

	public ResumeUIObjectGroup getResumeInfoGroup() {
		return this.resumeInfoGroup;
	}

	public int getCurrentGroup() {
		return this.currentGroup;
	}

	public int getTargetGroup() {
		return this.targetGroup;
	}

	public MainMenuWorldScene getWorldScene() {
		return this.worldScene;
	}

	@Override
	public String toString() {
		return "MainMenuUIScene@" + System.identityHashCode(this) + " [currentGroup=" + this.currentGroup + ", targetGroup="
				+ this.targetGroup + ", progress=" + this.progress + ", restPositions=" + Arrays.toString(this.restPositions)
				+ ", mainMenuGroup=" + this.mainMenuGroup + ", mainButtonsMenuGroup=" + this.mainButtonsMenuGroup + ", optionsMenuGroup="
				+ this.optionsMenuGroup + ", optionsVerticalGroup=" + this.optionsVerticalGroup + ", optionsEntriesGroup="
				+ this.optionsEntriesGroup + ", playMenuGroup=" + this.playMenuGroup + ", playHorizontalGroup=" + this.playHorizontalGroup
				+ ", playContentGroup=" + this.playContentGroup + ", playInfoGroup=" + this.playInfoGroup + ", groups="
				+ Arrays.toString(this.groups) + ", layout=" + this.layout + ", SMALL_TEXT_CHAR_SIZE=" + this.SMALL_TEXT_CHAR_SIZE
				+ ", SMALL_TEXT_TEXT_ALIGNMENT=" + this.SMALL_TEXT_TEXT_ALIGNMENT + ", uiCache=" + this.uiCache + ", hovering="
				+ this.hovering + ", focused=" + this.focused + ", name=" + this.name + ", camera=" + this.camera + ", entities="
				+ this.entities + "]";
	}

}
