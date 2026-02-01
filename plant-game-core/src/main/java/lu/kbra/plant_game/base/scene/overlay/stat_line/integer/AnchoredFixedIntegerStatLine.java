package lu.kbra.plant_game.base.scene.overlay.stat_line.integer;

import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AnchorOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredFixedIntegerStatLine extends FixedIntegerStatLine implements MarginOwner, AnchorOwner {

	protected Anchor objAnchor;
	protected Anchor tarAnchor;
	protected float margin;

	public AnchoredFixedIntegerStatLine(final String str, final Transform3D transform, final UIObject... values) {
		super(str, transform, values);
	}

	public AnchoredFixedIntegerStatLine(final String str, final UIObject... values) {
		super(str, values);
	}

	public AnchoredFixedIntegerStatLine(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, parent, values);
	}

	public AnchoredFixedIntegerStatLine(final String str, final Anchor obj, final Anchor tar) {
		super(str);
		this.objAnchor = obj;
		this.tarAnchor = tar;
	}

	public AnchoredFixedIntegerStatLine(final String str, final float gap, final Anchor obj, final Anchor tar) {
		super(str, gap);
		this.objAnchor = obj;
		this.tarAnchor = tar;
	}

	@Override
	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject> ObjectTriggerLatch<? extends AnchoredFixedIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final int valueLength,
			final Class<T> iconClazz,
			final Class<V> valueClazz) {
		return (ObjectTriggerLatch<? extends AnchoredFixedIntegerStatLine>) super.init(workers,
				render,
				height,
				valueLength,
				iconClazz,
				valueClazz);
	}

	@Override
	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject> ObjectTriggerLatch<? extends AnchoredFixedIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final Class<T> iconClazz,
			final Class<V> valueClazz) {
		return (ObjectTriggerLatch<? extends AnchoredFixedIntegerStatLine>) super.init(workers, render, height, iconClazz, valueClazz);
	}

	@Override
	public float getMargin() {
		return this.margin;
	}

	@Override
	public void setMargin(final float m) {
		this.margin = m;
	}

	@Override
	public Anchor getObjectAnchor() {
		return this.objAnchor;
	}

	@Override
	public void setObjectAnchor(final Anchor a) {
		this.objAnchor = a;
	}

	@Override
	public Anchor getTargetAnchor() {
		return this.tarAnchor;
	}

	@Override
	public void setTargetAnchor(final Anchor a) {
		this.tarAnchor = a;
	}

	@Override
	public String toString() {
		return "ExtAnchoredFixedIntegerStatLine [objAnchor=" + this.objAnchor + ", tarAnchor=" + this.tarAnchor + ", margin=" + this.margin
				+ ", icon=" + this.icon + ", value=" + this.value + ", comparator=" + this.comparator + ", layout=" + this.layout
				+ ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
