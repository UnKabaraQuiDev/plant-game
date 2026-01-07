package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;

import lu.pcy113.pclib.concurrency.ObjectTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.LimitedObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class FixedIntegerStatLine extends LayoutOffsetUIObjectGroup implements LimitedObjectGroup<UIObject> {

	public static final float POPUP_TEXT_SCALE = 0.6f;
	public static final int VALUE_LENGTH = 5, POPUP_LENGTH = 3;
	public static final ColorMaterial DEFAULT_TEXT_COLOR = ColorMaterial.WHITE;
	public static final float GAP = 0.08f;

	protected TexturedQuadMeshUIObject icon;
	protected IntegerTextUIObject value;

	protected Comparator<UIObject> comparator = (o1, o2) -> {
		if (o1 == o2) {
			return 0;
		}

		final int r1 = (o1 == this.icon) ? 0
				: (o1 == this.value) ? 1
				: (o1 instanceof SpacerUIObject) ? 2
				: 4;

		final int r2 = (o2 == this.icon) ? 0
				: (o2 == this.value) ? 1
				: (o2 instanceof SpacerUIObject) ? 2
				: 4;

		assert r1 != 4 : o1;
		assert r2 != 4 : o2;

		return Integer.compare(r1, r2);
	};

	public FixedIntegerStatLine(final String str, final Transform3D transform, final UIObject... values) {
		super(str, new FlowLayout(false, GAP), transform, values);
	}

	public FixedIntegerStatLine(final String str, final UIObject... values) {
		super(str, new FlowLayout(false, GAP), values);
	}

	public FixedIntegerStatLine(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, new FlowLayout(false, GAP), parent, values);
	}

	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject> ObjectTriggerLatch<? extends FixedIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final int valueLength,
			final Class<T> iconClazz,
			final Class<V> valueClazz) {

		final float iconHeightRatio = height / (float) TexturedQuadMeshUIObject.SQUARE_1_UNIT.getBounds2D().getHeight();
		final float textHeightRatio = height / UIObjectFactory.DEFAULT_CHAR_SIZE.y();

		final ObjectTriggerLatch<? extends FixedIntegerStatLine> latch = new ObjectTriggerLatch<>(iconClazz == null ? 1 : 2, this);

		if (iconClazz != null) {
			UIObjectFactory.create(iconClazz)
					.set(i -> i.setTransform(new Transform3D().scaleMul(iconHeightRatio)))
					.postInit(i -> this.icon = i)
					.add(this)
					.latch(latch)
					.push();
		}

		UIObjectFactory
				.createText(valueClazz,
						OptionalInt.of(valueLength),
						Optional.empty(),
						Optional.empty(),
						Optional.of(this.getId() + "-value"),
						Optional.empty())
				.set(i -> i.setForceSign(false))
				.set(i -> i.setPadding(true))
				.set(i -> i.setPaddingZero(false))
				.set(i -> i.setPaddingLength(valueLength))
				.set(i -> i.setValue(0))
				.set(i -> i.setColorMaterial(DEFAULT_TEXT_COLOR))
				.set(i -> i.setTransform(new Transform3D().scaleMul(textHeightRatio)))
				.postInit(i -> this.value = i)
				.add(this)
				.latch(latch)
				.push();

		return latch;
	}

	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject> ObjectTriggerLatch<? extends FixedIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final Class<T> iconClazz,
			final Class<V> valueClazz) {
		return this.init(workers, render, height, VALUE_LENGTH, iconClazz, valueClazz);
	}

	public TexturedQuadMeshUIObject getIcon() {
		return this.icon;
	}

	public boolean hasIcon() {
		return this.icon != null;
	}

	public IntegerTextUIObject getValue() {
		return this.value;
	}

	public FixedIntegerStatLine set(final int value) {
		this.getValue().setValue(value);
		return this;
	}

	public FixedIntegerStatLine flushValue() {
		if (this.getValue() == null) {
			return this;
		}
		this.getValue().flushValue();
		return this;
	}

	@Override
	public void doSort() {
		super.sort(this.comparator);
	}

	@Override
	public int getMaxItems() {
		return 2;
	}

	@Override
	public String toString() {
		return "FixedIntegerStatLine [icon=" + this.icon + ", value=" + this.value + ", comparator=" + this.comparator + ", layout="
				+ this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities + ", computedBounds="
				+ this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name=" + this.name + "]";
	}

}
