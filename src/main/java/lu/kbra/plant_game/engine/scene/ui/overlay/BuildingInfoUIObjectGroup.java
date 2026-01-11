package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pclib.concurrency.ListTriggerLatch;
import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;
import lu.pcy113.pclib.pointer.ObjectPointer;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.UOCreatingTaskFuture;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.prim.IBAnchoredFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.AnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.MoneyIconUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class BuildingInfoUIObjectGroup extends FixedBoundsUIObjectGroup implements ExtAnchorOwner, PaddingOwner, MarginOwner {

	public static final float FONT_HEIGHT = FixedIntegerStatLine.GAP;
	public static final int COLUMN_COUNT = 25;

	protected final LayoutOffsetUIObjectGroup content;

	protected ObjectPointer<UIObject> backdrop = new ObjectPointer<>();

	protected Anchor objectAnchor = Anchor.BOTTOM_CENTER;
	protected Anchor targetAnchor = Anchor.TOP_CENTER;
	protected WeakReference<UIObject> target;

	protected float margin = 0.05f;
	protected float padding = 0.02f;

	public BuildingInfoUIObjectGroup() {
		super("building-info", new AnchorLayout(), Direction2d.VERTICAL, FONT_HEIGHT * COLUMN_COUNT);

		this.content = new AnchoredLayoutUIObjectGroup(super.getId() + "-content",
				new FlowLayout(true, 0.05f),
				Anchor.CENTER_CENTER,
				Anchor.CENTER_CENTER);
		this.add(this.content);
	}

	public ObjectTriggerLatch<? extends BuildingInfoUIObjectGroup> init(final Dispatcher workers, final Dispatcher render) {
		final ObjectTriggerLatch<? extends BuildingInfoUIObjectGroup> latch = new ObjectTriggerLatch<>(2, this);

		/* cost */
		this.addIntLine(workers, render, "cost-line", MoneyIconUIObject.class, "text.cost").latch(latch);

		/* backdrop */
		UIObjectFactory.create(IBAnchoredFlatQuadUIObject.class)
				.set(i -> i.setTransform(new Transform3D(new Vector3f(0, -0.2f, 0), new Quaternionf(), new Vector3f(1f / this.size, 1, 1))))
				.set(i -> i.setColor(new Vector4f(ColorMaterial.DARK_GRAY.getColor()).mul(1, 1, 1, 0.5f)))
				.set(i -> i.setAnchors(Anchor.CENTER_CENTER, Anchor.CENTER_CENTER))
				.add(this)
				.get(this.backdrop)
				.latch(latch)
				.push();

		latch.thenOther(BuildingInfoUIObjectGroup::recomputeBounds);

		return latch;
	}

	public ListTriggerLatch<UIObject> addIntLine(
			final Dispatcher workers,
			final Dispatcher render,
			final String id,
			final Class<? extends TexturedQuadMeshUIObject> iconClazz,
			final String key) {
		final AnchoredFixedIntegerStatLine costValue = new AnchoredFixedIntegerStatLine(id + "-value",
				0f,
				Anchor.CENTER_RIGHT,
				Anchor.CENTER_RIGHT);
		return this.addLine(id,
				(Consumer<ListTriggerLatch<UIObject>>) cost -> UIObjectFactory
						.createText(AnchoredProgrammaticTextUIObject.class, FONT_HEIGHT, key)
						.set(i -> i.setTransform(new Transform3D()))
						.set(i -> i.setAnchors(Anchor.CENTER_LEFT, Anchor.CENTER_LEFT))
						.latch(cost)
						.push(),
				(Consumer<ListTriggerLatch<UIObject>>) cost -> costValue
						.init(workers, render, FONT_HEIGHT, iconClazz, IntegerTextUIObject.class)
						.latch(cost)
						.then(AnchoredFixedIntegerStatLine::flushValue));
	}

	public ListTriggerLatch<UIObject> addLine(
			final String id,
			final Function<ListTriggerLatch<UIObject>, UOCreatingTaskFuture<? extends UIObject>>... funcs) {
		final ListTriggerLatch<UIObject> line = new ListTriggerLatch<>(funcs.length, (final List<UIObject> list) -> {
			final BoundedUIObjectGroup costLine = new BoundedUIObjectGroup(id, new AnchorLayout(), Direction2d.VERTICAL);
			costLine.addAll(list);
			this.content.add(costLine);
		});

		for (Function<ListTriggerLatch<UIObject>, UOCreatingTaskFuture<? extends UIObject>> func : funcs) {
			func.apply(line).push();
		}

		return line;
	}

	public ListTriggerLatch<UIObject> addLine(final String id, final Consumer<ListTriggerLatch<UIObject>>... funcs) {
		final ListTriggerLatch<UIObject> line = new ListTriggerLatch<>(funcs.length, (final List<UIObject> list) -> {
			final BoundedUIObjectGroup costLine = new BoundedUIObjectGroup(id, new AnchorLayout(), Direction2d.VERTICAL);
			costLine.addAll(list);
			this.content.add(costLine);
		});

		for (Consumer<ListTriggerLatch<UIObject>> func : funcs) {
			func.accept(line);
		}

		return line;
	}

	@Override
	public boolean recomputeBounds() {
		final boolean recomp = super.recomputeBounds();
		this.backdrop.ifSet(p -> {
			final Rectangle2D b2d = this.getBounds().getBounds2D();
			p.getTransform().scaleSet((float) b2d.getWidth(), 1, (float) b2d.getHeight()).update();
		});
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
		this.target = new WeakReference<>(target);

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
