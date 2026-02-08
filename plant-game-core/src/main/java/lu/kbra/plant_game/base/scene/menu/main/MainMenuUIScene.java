package lu.kbra.plant_game.base.scene.menu.main;

import java.awt.geom.CubicCurve2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.pclib.PCUtils;
import lu.kbra.pclib.concurrency.ListTriggerLatch;
import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.base.data.DefaultKeyOption;
import lu.kbra.plant_game.base.scene.overlay.group.impl.MarginAnchoredUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.impl.ParentUIObjectGroup;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.LevelButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.data.GradientDirection;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.gradient.AnchoredGradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollContainerUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.icon.LargeLogoUIObject;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.VolumeSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.VolumeTextUIObject;
import lu.kbra.plant_game.engine.loader.StaticFlatMeshLoader;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextAlignment;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

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

	protected Vector3fc[] restPositions = { new Vector3f(), new Vector3f(5, 0, 5), new Vector3f(5, 0, 0), null };

	protected ParentUIObjectGroup mainMenuGroup = new ParentUIObjectGroup("main",
			new AnchorLayout(),
			new Transform3D(new Vector3f(this.restPositions[MAIN])));

	protected MarginAnchoredUIObjectGroup mainButtonsMenuGroup = new MarginAnchoredUIObjectGroup("main.buttons",
			new FlowLayout(true, 0.06f),
			this.mainMenuGroup,
			Anchor.CENTER_LEFT,
			Anchor.CENTER_LEFT,
			0.25f);

	protected ParentUIObjectGroup optionsMenuGroup = new ParentUIObjectGroup("options",
			new AnchorLayout(),
			new Transform3D(this.restPositions[OPTIONS]));
	protected OptionsUIObjectGroup optionsVerticalGroup = new OptionsUIObjectGroup(this.optionsMenuGroup);
	protected UIObjectGroup optionsEntriesGroup = this.optionsVerticalGroup.getContent();

	protected ScrollContainerUIObjectGroup playMenuGroup = new ScrollContainerUIObjectGroup("play",
			this.restPositions[PLAY],
			Direction.EAST,
			0.00f);
	protected ScrollDrivenUIObjectGroup playContentMenuGroup = this.playMenuGroup.getScrollContent();

	protected OffsetUIObjectGroup[] groups = new OffsetUIObjectGroup[] {
			this.mainMenuGroup,
			this.playMenuGroup,
			this.optionsMenuGroup,
			null };

	protected Layout layout;

//	protected CursorUIObject cursor;

	public MainMenuUIScene(final CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		super.addAll(this.mainMenuGroup, this.optionsMenuGroup, this.playMenuGroup);

		/* main menu */
		this.buildMainMenu();

		/* option content */
		this.buildOptionsMenu();

		/* play content */
		this.buildPlayMenu(workers, renderDispatcher);

	}

	private void buildPlayMenu(final Dispatcher workers, final Dispatcher renderDispatcher) {

		final int count = 5;
		final ListTriggerLatch<LevelButtonUIObject> btns = new ListTriggerLatch<>(count, list -> {
			Collections.sort(list);
			this.playContentMenuGroup.addAll(list);

			new TaskFuture<>(workers, (Supplier<Vector3f[]>) () -> {
				final CubicCurve2D.Float curve = new CubicCurve2D.Float(0, 0, 0.5f, 1, 2, 2.8f, 3, 3);
				return BezierStripGenerator.generateTriangleStrip(curve, 0.25f, 10, 0, GeoPlane.XZ);
			}).then(renderDispatcher, (Function<Vector3f[], Mesh>) arr -> {
				final Vec3fAttribArray pos = new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME,
						Mesh.ATTRIB_VERTICES_ID,
						arr,
						BufferType.ARRAY,
						false);
//pos.genInit();
				System.err.println(Arrays.stream(arr).map(c -> c.x + ", " + c.y + ", " + c.z).collect(Collectors.joining("\n")));
				return new TexturedLoadedTriangleStripMesh("meshTest",
						null,
						(SingleTexture) this.uiCache.getTexture(StaticFlatMeshLoader.TEXTURE_NAME),
						pos);
			}).then(workers, (Consumer<Mesh>) m -> {
				System.err.println(m);
				TintedMeshUIObject go = new TintedMeshUIObject("meshTestObject", m);
				go.setTransform(new Transform3D(new Vector3f(), new Quaternionf().rotateXYZ(0.1f, 0.2f, 0.3f), new Vector3f(0.4f)));
				go.setColorMaterial(ColorMaterial.GREEN);
				this.mainMenuGroup.add(go);
				System.err.println(go);
			}).push();

//			new TaskFuture<>(workers, (Supplier<ReadOnlyPair<Vector3f[], int[]>>) () -> {
//				final Vector3f[] pos = new Vector3f[12];
//				for (int i = 0; i < list.size(); i++) {
////					final Rectangle2D bounds = list.get(i).getTransformedBounds().getBounds2D();
//					pos[i] = list.get(i).getTransform().getTranslation();
//				}
//				final int[] indices = new int[pos.length * 2 + 2];
//				for (int i = 0; i < pos.length - 1; i++) {
//					indices[i * 2 + 0] = i;
//					indices[i * 2 + 1] = i + 1;
//				}
//
//				return Pairs.readOnly(pos, indices);
//			}).then(renderDispatcher, (Function<ReadOnlyPair<Vector3f[], int[]>, TimelineMesh>) pair -> {
//				final Vector3f[] pos = pair.getKey();
//				final int[] indices = pair.getValue();
//
//				final TimelineMesh mesh = new TimelineMesh("timeline@" + System.identityHashCode(this),
//						13,
//						(SingleTexture) this.uiCache.getTexture(StaticFlatMeshLoader.TEXTURE_NAME),
//						new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, pos),
//						new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, indices, BufferType.ELEMENT_ARRAY));
//				mesh.setLineWidth(100);
//				mesh.setEffectiveLength(list.size() * 2 - 2);
//				this.uiCache.addMesh(mesh);
//				// TODO: replace this with a triangle-based mesh
//
//				return mesh;
//			}).then(workers, (Consumer<TimelineMesh>) (final TimelineMesh mesh) -> {
//				UIObjectFactory.createManual(MeshUIObject.class, mesh)
//						.set(i -> i.setTransform(new Transform3D(new Vector3f(0, -0.1f, 0))))
//						.add(this.playContentMenuGroup);
//				this.playContentMenuGroup.getTransform()
//						.getTranslation().z = (float) -this.playContentMenuGroup.getBounds().getBounds2D().getCenterY();
//				this.playContentMenuGroup.getTransform().updateMatrix();
//			}).push();
		});

		final float startPosX = -1;
		IntStream.range(0, count)
				.forEach(j -> UIObjectFactory.create(LevelButtonUIObject.class)
						.set(i -> i.setTransform(new Transform3D(new Vector3f(startPosX + 0.6f * j, 0, (0.6f * j) % (2 - 0.5f) - 1),
								new Quaternionf().rotateY((float) Math.random()),
								new Vector3f(0.5f))))
						.set(i -> i.setLevelId("lvl" + j))
						.latch(btns)
						.push());
	}

	private void buildOptionsMenu() {
		final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.2f));
		final Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_LEFT);

		final float charSize = 0.1f;

		new OptionVolumeUIObjectGroup(this.optionsEntriesGroup).init(VolumeTextUIObject.class, VolumeSliderUIObject.class, charSize);

		this.optionsEntriesGroup.add(SpacerUIObject.getVerticalSpacer(0.1f));

		for (final DefaultKeyOption key : DefaultKeyOption.values()) {
			new OptionKeyUIObjectGroup(key, this.optionsEntriesGroup).init(PGLogic.INSTANCE.getInputHandler(), charSize);
		}

		UIObjectFactory.create(AnchoredGradientQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0),
						new Quaternionf().rotateY((float) Math.PI),
						new Vector3f(2.5f, 1, 2))))
				.set(i -> i.setDirection(GradientDirection.UV_X))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("317dac8c")))
				.set(i -> i.setAnchors(Anchor.CENTER_RIGHT, Anchor.CENTER_RIGHT))
				.add(this.optionsMenuGroup)
				.push();

		UIObjectFactory.create(AnchoredGradientQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, GRADIENT_DEPTH - 0.1f, 0),
						new Quaternionf().rotateY((float) Math.PI),
						new Vector3f(5f, 1, 2))))
				.set(i -> i.setDirection(GradientDirection.UV_Y))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("000000ff")))
				.set(i -> i.setAnchors(Anchor.TOP_LEFT, Anchor.TOP_LEFT))
				.add(this.optionsMenuGroup)
				.push();

		UIObjectFactory
				.createText(BackButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.add(this.optionsMenuGroup)
				.set(i -> i.setAnchors(Anchor.TOP_LEFT, Anchor.TOP_LEFT))
				.set(i -> i.setMargin(0.05f))
//		.postInit(i -> this.optionsBackBtn = i)
				.push();
	}

	private void buildMainMenu() {
		final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.2f));
		final Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_CENTER);

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
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0), new Quaternionf(), new Vector3f(2.5f, 1, 2))))
				.set(i -> i.setDirection(GradientDirection.UV_X))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("3b784a")))
				.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
				.add(this.mainMenuGroup)
				.push();
	}

	@Override
	public void input(final WindowInputHandler inputHandler, final UpdateFrameState frameState) {
		super.input(inputHandler, frameState);

		final OffsetUIObjectGroup current = this.groups[this.currentGroup];
		if (current instanceof final ScrollContainerUIObjectGroup scrollContainer && scrollContainer.getScrollBar() != null) {
			scrollContainer.updateScrollBar();
			scrollContainer.getScrollBar().addScrollPosition((float) inputHandler.getMouseScroll().y);
		}
		current.recomputeBounds();

		frameState.uiSceneCaughtMouseInput = true;

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

			final float duration = 0.8f;
			final float dTime = inputHandler.dTime();
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
