package lu.kbra.plant_game.engine.scene.ui.menu.main;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.concurrency.ListTriggerLatch;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.datastructure.pair.ReadOnlyPair;

import lu.kbra.plant_game.PGLogic;
import lu.kbra.plant_game.engine.UpdateFrameState;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.LevelButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.StaticFlatMeshLoader;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.gradient.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollContainerUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.mesh.line.TimelineMesh;
import lu.kbra.plant_game.engine.entity.ui.prim.MeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.ScrollBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.VolumeSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.OptionKeyUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.VolumeTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextFieldUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LargeLogoUIObject;
import lu.kbra.plant_game.engine.render.DeferredCompositor;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.EdgeStickLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.MarginFlowLayout;
import lu.kbra.plant_game.engine.window.input.MappingInputHandler;
import lu.kbra.plant_game.engine.window.input.StandardKeyOption;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.cache.CacheManager;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.impl.future.TaskFuture;
import lu.kbra.standalone.gameengine.impl.future.WorkerDispatcher;
import lu.kbra.standalone.gameengine.utils.GameEngineUtils;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.gl.consts.BufferType;
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

	protected Vector3fc[] restPositions = { new Vector3f(), new Vector3f(5, 0, 5), new Vector3f(5, 0, 0), null };

	protected OffsetUIObjectGroup mainMenuGroup = new OffsetUIObjectGroup("main", new Transform3D(new Vector3f(this.restPositions[MAIN])));

	protected LayoutOffsetUIObjectGroup mainLeftMenuGroup = new LayoutOffsetUIObjectGroup("main.left",
			new MarginFlowLayout(true, 0.02f, 0.3f, 0, 0.5f, 0.5f, MarginFlowLayout.LEFT),
			this.mainMenuGroup);
	protected LayoutOffsetUIObjectGroup mainButtonsMenuGroup = new LayoutOffsetUIObjectGroup("main.buttons",
			new FlowLayout(true, 0.02f),
			this.mainLeftMenuGroup);

	protected ScrollContainerUIObjectGroup optionsMenuGroup = new ScrollContainerUIObjectGroup("options",
			this.restPositions[OPTIONS],
			Direction.SOUTH,
			0.05f,
			new FlowLayout(true, 0.0f));
	protected LayoutScrollDrivenUIObjectGroup optionsEntriesMenuGroup = (LayoutScrollDrivenUIObjectGroup) this.optionsMenuGroup
			.getScrollContent();

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

	protected CursorUIObject cursor;

	protected GradientQuadUIObject greenGradient;
	protected GradientQuadUIObject blueGradient;
	protected BackButtonUIObject optionsBackBtn;

	public MainMenuUIScene(final CacheManager parent) {
		super("main-menu", parent);
	}

	@Override
	public void init(final Dispatcher workers, final Dispatcher renderDispatcher) {
		super.addAll(this.mainMenuGroup, this.optionsMenuGroup, this.playMenuGroup);

		/* main menu */
		this.buildMainMenu(workers, renderDispatcher);

		/* option content */
		this.buildOptionsMenu(workers, renderDispatcher);

		/* play content */
		this.buildPlayMenu(workers, renderDispatcher);

		/* common */
		UIObjectFactory.create(CursorUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0.01f, 0.25f), new Quaternionf(), new Vector3f(0.15f))))
				.add(this)
				.then(workers, (Consumer<CursorUIObject>) btn -> {
					btn.forceCirclingMouse();
					this.cursor = btn;
				})
				.push();
	}

	private void buildPlayMenu(final Dispatcher workers, final Dispatcher renderDispatcher) {
		UIObjectFactory.create(ScrollBarUIObject.class)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setColorMaterial(ColorMaterial.GRAY))
				.set(i -> i.setDir(Direction.EAST))
				.set(i -> i.setRange(new Vector2f(-1, 1)))
				.set(i -> i.setSize(new Vector2f(0.05f, 0.2f)))
				.set(i -> i.setSpeed(PLAY_SCROLL_SPEED))
//				.add(this.playMenuGroup)
				.postInit(this.playMenuGroup::setScrollBar)
				.push();

		final int count = 5;
		final ListTriggerLatch<LevelButtonUIObject> btns = new ListTriggerLatch<>(count, list -> {
			Collections.sort(list);
			this.playContentMenuGroup.addAll(list);

			new TaskFuture<>(workers, (Supplier<ReadOnlyPair<Vector3f[], int[]>>) () -> {
				final Vector3f[] pos = new Vector3f[12];
				for (int i = 0; i < list.size(); i++) {
//					final Rectangle2D bounds = list.get(i).getTransformedBounds().getBounds2D();
					pos[i] = list.get(i).getTransform().getTranslation();
				}
				final int[] indices = new int[pos.length * 2 + 2];
				for (int i = 0; i < pos.length - 1; i++) {
					indices[i * 2 + 0] = i;
					indices[i * 2 + 1] = i + 1;
				}

				return Pairs.readOnly(pos, indices);
			}).then(renderDispatcher, (Function<ReadOnlyPair<Vector3f[], int[]>, TimelineMesh>) pair -> {
				final Vector3f[] pos = pair.getKey();
				final int[] indices = pair.getValue();

				final TimelineMesh mesh = new TimelineMesh("timeline@" + System.identityHashCode(this),
						13,
						(SingleTexture) this.uiCache.getTexture(StaticFlatMeshLoader.TEXTURE_NAME),
						new Vec3fAttribArray(Mesh.ATTRIB_VERTICES_NAME, Mesh.ATTRIB_VERTICES_ID, pos),
						new UIntAttribArray(Mesh.ATTRIB_INDICES_NAME, Mesh.ATTRIB_INDICES_ID, indices, BufferType.ELEMENT_ARRAY));
				mesh.setLineWidth(100);
				mesh.setEffectiveLength(list.size() * 2 - 2);
				this.uiCache.addMesh(mesh);
				// TODO: replace this with a triangle-based mesh

				return mesh;
			}).then(workers, (Consumer<TimelineMesh>) (final TimelineMesh mesh) -> {
				UIObjectFactory.createManual(MeshUIObject.class, mesh)
						.set(i -> i.setTransform(new Transform3D(new Vector3f(0, -0.1f, 0))))
						.add(this.playContentMenuGroup);
				this.playContentMenuGroup.getTransform()
						.getTranslation().z = (float) -this.playContentMenuGroup.getBounds().getBounds2D().getCenterY();
				this.playContentMenuGroup.getTransform().updateMatrix();
			}).push();
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

	private void buildOptionsMenu(final Dispatcher workers, final Dispatcher renderDispatcher) {
		final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.1f));
		Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_CENTER);

		final LayoutOffsetUIObjectGroup optionsVolumeGroup = new LayoutOffsetUIObjectGroup("options.volume",
				new EdgeStickLayout(true, 0, SMALL_TEXT_CHAR_SIZE.get().x() * OPTIONS_COLUMN_COUNT));

		this.optionsEntriesMenuGroup.add(optionsVolumeGroup);
		this.optionsEntriesMenuGroup.add(SpacerUIObject.getVerticalSpacer(2 * SMALL_TEXT_CHAR_SIZE.get().y()));

		final ListTriggerLatch<OptionKeyUIObject> optionKeyGroupLatch = new ListTriggerLatch<>(StandardKeyOption.values().length,
				l -> workers.post(() -> {
					l.forEach(this.optionsEntriesMenuGroup::add);
					this.updateKeys();
				}));

		final OptionalInt SMALL_TEXT_BUFFER_SIZE = OptionalInt.of(OPTIONS_COLUMN_COUNT);
		for (final StandardKeyOption key : StandardKeyOption.values()) {
			UIObjectFactory
					.createText(OptionKeyUIObject.class,
							SMALL_TEXT_BUFFER_SIZE,
							SMALL_TEXT_CHAR_SIZE,
							SMALL_TEXT_TEXT_ALIGNMENT,
							Optional.of("options.keys" + key),
							Optional.of(key.name().toLowerCase()))
					.set(i -> i.setTransform(new Transform3D()))
					.set(i -> i.setDir(Scale2dDir.BOTH))
					.set(i -> i.setKey(key.name().toLowerCase()))
					.latch(optionKeyGroupLatch)
					.push();
		}

		/* volume */
		final ListTriggerLatch<UIObject> optionsVolumeGroupLatch = new TextSliderListTriggerLatch<>(workers,
				optionsVolumeGroup,
				VolumeTextUIObject.class,
				VolumeSliderUIObject.class);

		SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_LEFT);
		UIObjectFactory
				.createText(VolumeTextUIObject.class,
						SMALL_TEXT_BUFFER_SIZE,
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3DPivot()))
				.latch(optionsVolumeGroupLatch)
				.push();
		UIObjectFactory
				.createText(VolumeSliderUIObject.class,
						SMALL_TEXT_BUFFER_SIZE,
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3DPivot()))
				.latch(optionsVolumeGroupLatch)
				.push();

		UIObjectFactory.create(ScrollBarUIObject.class)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setColorMaterial(ColorMaterial.GRAY))
				.set(i -> i.setDir(Direction.NORTH))
				.set(i -> i.setRange(new Vector2f(0.8f, -0.8f)))
				.set(i -> i.setSize(new Vector2f(0.05f, 0.2f)))
				.set(i -> i.setSpeed(OPTIONS_SCROLL_SPEED))
				.set(this.optionsMenuGroup::setScrollBar)
				.push();
	}

	private void buildMainMenu(final Dispatcher workers, final Dispatcher renderDispatcher) {
		final Optional<Vector2fc> SMALL_TEXT_CHAR_SIZE = Optional.of(new Vector2f(0.2f));
		final Optional<TextAlignment> SMALL_TEXT_TEXT_ALIGNMENT = Optional.of(TextAlignment.TEXT_CENTER);

		final ListTriggerLatch<UIObject> mainButtonsLatch = new ListTriggerLatch<>(3, l -> workers.post(() -> {
			l.forEach(this.mainButtonsMenuGroup::add);
			this.mainLeftMenuGroup.doLayout();
		}));

		UIObjectFactory
				.createText(PlayButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.latch(mainButtonsLatch)
				.push();
		UIObjectFactory
				.createText(OptionsButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.latch(mainButtonsLatch)
				.push();
		UIObjectFactory
				.createText(QuitButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.latch(mainButtonsLatch)
				.push();

		UIObjectFactory.create(LargeLogoUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, 0, -0.75f), new Quaternionf(), new Vector3f(2))))
				.add(this.mainMenuGroup)
				.push();

		UIObjectFactory.create(GradientQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0), new Quaternionf(), new Vector3f(2.5f, 1, 2))))
				.set(i -> i.setDirection(GradientDirection.UV_X))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("3b784a")))
				.add(this.mainMenuGroup)
				.postInit(i -> this.greenGradient = i)
				.push();

		/** options */

		UIObjectFactory.create(GradientQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, GRADIENT_DEPTH, 0),
						new Quaternionf().rotateY((float) Math.PI),
						new Vector3f(2.5f, 1, 2))))
				.set(i -> i.setDirection(GradientDirection.UV_X))
				.set(i -> i.setTint(GameEngineUtils.hexToColorToVec4f("317dac8c")))
				.add(this.mainMenuGroup)
				.postInit(i -> this.blueGradient = i)
				.push();

		UIObjectFactory
				.createText(BackButtonUIObject.class,
						OptionalInt.empty(),
						SMALL_TEXT_CHAR_SIZE,
						SMALL_TEXT_TEXT_ALIGNMENT,
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D(new Vector3f(-0.5f, 0, -0.5f), new Quaternionf(), new Vector3f(0.5f))))
				.add(this.optionsMenuGroup)
				.postInit(i -> this.optionsBackBtn = i)
				.push();
	}

	private void updateKeys() {
		final MappingInputHandler inputHandler = PGLogic.INSTANCE.getInputHandler();

		this.optionsEntriesMenuGroup.parallelStream()
				.filter(OptionKeyUIObject.class::isInstance)
				.map(e -> (OptionKeyUIObject) e)
				.forEach(e -> e.setKeyValue(inputHandler.getInputName(e.getKeyOption().getPhysicalKey())));

		final OptionKeyUIObject example = this.optionsEntriesMenuGroup.parallelStream()
				.filter(OptionKeyUIObject.class::isInstance)
				.map(e -> (OptionKeyUIObject) e)
				.findFirst()
				.orElseThrow(IllegalStateException::new);

		PGLogic.INSTANCE.RENDER_DISPATCHER.post(() -> this.optionsEntriesMenuGroup.stream()
				.filter(ProgrammaticTextUIObject.class::isInstance)
				.map(e -> (ProgrammaticTextUIObject) e)
				.forEach(ProgrammaticTextUIObject::updateText));

		((FlowLayout) this.optionsEntriesMenuGroup.getLayout()).setGap((float) (example.getBounds().getBounds2D().getHeight()
				* (example.getTargetScale(true).z() - example.getTargetScale(false).z())));

		this.optionsEntriesMenuGroup.doLayout();

		this.optionsEntriesMenuGroup.getTransform().translationSet(0, 0, -1f + 0.3f).updateMatrix();
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

		if (this.cursor != null) {
			final Optional<UIObject> focusCandidate = this.hovering.stream()
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
			final DeferredCompositor compositor,
			final WorkerDispatcher workers,
			final Dispatcher render) {
		super.update(inputHandler, compositor, workers, render);

		if (this.cursor != null && this.currentGroup == MAIN && this.mainButtonsMenuGroup.size() > 1) {
			this.cursor.setActive(true);

			final float z1 = AbsoluteTransform3DOwner.getAbsoluteTransform(this.mainButtonsMenuGroup.get(0))
					.getTranslation(new Vector3f()).z;
			final float z2 = AbsoluteTransform3DOwner.getAbsoluteTransform(this.mainButtonsMenuGroup.get(1))
					.getTranslation(new Vector3f()).z;

			this.cursor.setSnapPhase(Math.max(z1, z2));
			this.cursor.setSnapAngle(Math.abs(Math.min(z1, z2) - Math.max(z1, z2)));
		}

		// TODO: This should be managed by layout or self-managed
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
