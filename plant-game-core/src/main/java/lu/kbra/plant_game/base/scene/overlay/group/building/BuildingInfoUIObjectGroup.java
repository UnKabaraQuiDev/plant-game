package lu.kbra.plant_game.base.scene.overlay.group.building;

import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.pclib.concurrency.DeferredTriggerLatch;
import lu.kbra.pclib.concurrency.ListTriggerLatch;
import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.pclib.pointer.ObjectPointer;
import lu.kbra.plant_game.BuildingDefinition;
import lu.kbra.plant_game.base.data.DefaultResourceType;
import lu.kbra.plant_game.base.scene.overlay.group.impl.AnchoredLayoutUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.group.impl.FixedBoundsUIObjectGroup;
import lu.kbra.plant_game.base.scene.overlay.stat_line.integer.FixedIntegerStatLine;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.factory.UOCreatingTaskFuture;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.ExtAnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.PaddingOwner;
import lu.kbra.plant_game.engine.entity.ui.prim.IBAnchoredFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.scene.world.data.resource.ResourceType;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.plant_game.plugin.registry.ResourceRegistry;
import lu.kbra.standalone.gameengine.impl.WeakArrayList;
import lu.kbra.standalone.gameengine.impl.WeakList;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BuildingInfoUIObjectGroup extends FixedBoundsUIObjectGroup implements ExtAnchorOwner, PaddingOwner, MarginOwner {

	public static final float FONT_HEIGHT = FixedIntegerStatLine.GAP;
	public static final int COLUMN_COUNT = 25;
	public static final Vector2fc SMALLEST_CHAR_SIZE = new Vector2f(FONT_HEIGHT / 2.5f);

	protected final LayoutOffsetUIObjectGroup content;

	protected ObjectPointer<UIObject> backdrop = new ObjectPointer<>();

	protected Anchor objectAnchor = Anchor.BOTTOM_CENTER;
	protected Anchor targetAnchor = Anchor.TOP_CENTER;
	protected WeakReference<UIObject> target;
	protected WeakReference<BuildingDefinition<?>> buildingDef;

	protected WeakList<ResourceLineUIObjectGroup> resourceLines = new WeakArrayList<>();
	protected WeakList<ProgrammaticTextUIObject> messages = new WeakArrayList<>();

	protected float margin = 0.1f;
	protected float padding = 0.02f;

	public BuildingInfoUIObjectGroup() {
		super("building-info", new AnchorLayout(), Direction2d.VERTICAL, FONT_HEIGHT * COLUMN_COUNT);

		this.content = new AnchoredLayoutUIObjectGroup(super.getId() + "-content",
				new FlowLayout(true, 0.05f),
				Anchor.CENTER_CENTER,
				Anchor.CENTER_CENTER);
		this.add(this.content);
	}

	public boolean setBuildingDefinition(final BuildingDefinition<?> buildingDefinition) {
		if (this.buildingDef != null && this.buildingDef.get() == buildingDefinition) {
			return false;
		}
		this.buildingDef = new WeakReference<>(buildingDefinition);
		buildingDefinition.accept(this);
		return true;
	}

	public ObjectTriggerLatch<? extends BuildingInfoUIObjectGroup> init() {
		final ObjectTriggerLatch<? extends BuildingInfoUIObjectGroup> latch = new ObjectTriggerLatch<>(3, this);

		/* cost */
		this.addCostIntLine(DefaultResourceType.MONEY).latch(latch);

		this.addStringLine().latch(latch);

		/* backdrop */
		UIObjectFactory.create(IBAnchoredFlatQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, -0.2f, 0), new Quaternionf(), new Vector3f(1f / this.size, 1, 1))))
				.set(i -> i.setColor(new Vector4f(ColorMaterial.DARK_GRAY.getColor()).mul(1, 1, 1, 0.8f)))
				.set(i -> i.setAnchors(Anchor.CENTER_CENTER, Anchor.CENTER_CENTER))
				.add(this)
				.get(this.backdrop)
				.latch(latch)
				.push();

		latch.thenOther(BuildingInfoUIObjectGroup::recomputeBounds);

		return latch;
	}

	public ObjectTriggerLatch<? extends ResourceLineUIObjectGroup> addCostIntLine(final ResourceType resourceType) {
		return this.addIntLine(ResourceRegistry.getInternalName(resourceType), resourceType);
	}

	public DeferredTriggerLatch<ProgrammaticTextUIObject> addStringLine() {
		return UIObjectFactory
				.createText(ProgrammaticTextUIObject.class,
						OptionalInt.of(64),
						Optional.of(SMALLEST_CHAR_SIZE),
						Optional.empty(),
						Optional.empty(),
						Optional.empty())
				.set(i -> i.setTransform(new Transform3D()))
				.add(this.content)
				.postInit(this.messages::add)
				.pushAsLatch();
	}

	public ObjectTriggerLatch<? extends ResourceLineUIObjectGroup> addIntLine(final String id, final ResourceType rt) {
		final ResourceLineUIObjectGroup newLine = new ResourceLineUIObjectGroup(id, new AnchorLayout(), Direction2d.VERTICAL, rt);
		return newLine.init().thenOther(c -> {
			this.content.add(c);
			this.resourceLines.add(c);
		});
	}

	public ListTriggerLatch<UIObject> addLine(
			final String id,
			final ResourceType rt,
			final Function<ListTriggerLatch<UIObject>, UOCreatingTaskFuture<? extends UIObject>>... funcs) {
		final ListTriggerLatch<UIObject> latch = new ListTriggerLatch<>(funcs.length, (final List<UIObject> lineChildren) -> {
			final ResourceLineUIObjectGroup newLine = new ResourceLineUIObjectGroup(id, new AnchorLayout(), Direction2d.VERTICAL, rt);
			newLine.addAll(lineChildren);
			this.content.add(newLine);
			this.resourceLines.add(newLine);
		});

		for (Function<ListTriggerLatch<UIObject>, UOCreatingTaskFuture<? extends UIObject>> func : funcs) {
			func.apply(latch).push();
		}

		return latch;
	}

	public ListTriggerLatch<UIObject> addLine(final String id, final ResourceType rt, final Consumer<ListTriggerLatch<UIObject>>... funcs) {
		final ListTriggerLatch<UIObject> line = new ListTriggerLatch<>(funcs.length, (final List<UIObject> list) -> {
			final ResourceLineUIObjectGroup newLine = new ResourceLineUIObjectGroup(id, new AnchorLayout(), Direction2d.VERTICAL, rt);
			newLine.addAll(list);
			this.content.add(newLine);
			this.resourceLines.add(newLine);
		});

		for (Consumer<ListTriggerLatch<UIObject>> func : funcs) {
			func.accept(line);
		}

		return line;
	}

	@Override
	public boolean recomputeBounds() {
		final boolean recomp = super.recomputeBounds();
		if (this.backdrop != null) {
			this.backdrop.ifSet(p -> {
				final Rectangle2D b2d = this.getBounds().getBounds2D();
				System.err.println(b2d);
				p.getTransform().scaleSet((float) b2d.getWidth(), 1, (float) b2d.getHeight()).updateMatrix();
			});
		}
		return recomp;
	}

	public LayoutOffsetUIObjectGroup getContent() {
		return this.content;
	}

	@Override
	public UIObject getTarget() {
		return this.target == null ? null : this.target.get();
	}

	@Override
	public void setTarget(final UIObject target) {
		if (target == null) {
			this.target = null;
		} else if (this.getTarget() != target) {
			this.target = new WeakReference<>(target);
		}
	}

	public WeakList<ResourceLineUIObjectGroup> getResourceLines() {
		return this.resourceLines;
	}

	public WeakList<ProgrammaticTextUIObject> getMessages() {
		return this.messages;
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objectAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor objectAnchor) {
		this.objectAnchor = objectAnchor;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.targetAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor targetAnchor) {
		this.targetAnchor = targetAnchor;
	}

	@Override
	public float getMargin() {
		return this.margin;
	}

	@Override
	public void setMargin(final float margin) {
		this.margin = margin;
	}

	@Override
	public float getPadding() {
		return this.padding;
	}

	@Override
	public void setPadding(final float padding) {
		this.padding = padding;
	}

	@Override
	public String toString() {
		return "BuildingInfoUIObjectGroup@" + System.identityHashCode(this) + " [backdrop=" + this.backdrop + ", objectAnchor="
				+ this.objectAnchor + ", targetAnchor=" + this.targetAnchor + ", target=" + this.target + ", margin=" + this.margin
				+ ", padding=" + this.padding + ", dir=" + this.dir + ", bounds=" + this.bounds + ", size=" + this.size + ", layout="
				+ this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
