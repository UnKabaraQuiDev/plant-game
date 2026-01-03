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
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.FlowLayout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolator;
import lu.kbra.standalone.gameengine.utils.interpolation.Interpolators;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class OverlayIntegerStatLine extends LayoutOffsetUIObjectGroup
		implements LimitedObjectGroup<UIObject>, NeedsUpdate, OverlayStatLine<OverlayIntegerStatLine> {

	public static final float POPUP_TEXT_SCALE = 0.6f;
	public static final int VALUE_LENGTH = 5, POPUP_LENGTH = 3;
	public static final ColorMaterial DEFAULT_TEXT_COLOR = ColorMaterial.WHITE;
	public static final float gap = 0.08f;

	protected TexturedQuadMeshUIObject icon;
	protected IntegerTextUIObject value;
	protected SignedIntegerTextUIObject popup;
	protected Direction popupSpawnDirection = Direction.EAST;
	protected float popupSpawnDuration = 0.2f;
	protected float popupStayDuration = 1 - 2 * this.popupSpawnDuration;
	protected float progress = 100;
	protected Interpolator popupSpawnInterpolator = Interpolators.CUBIC_IN;

	protected Comparator<UIObject> comparator = (o1, o2) -> {
		if (o1 == o2) {
			return 0;
		}

		final int r1 = (o1 == this.icon) ? 0
				: (o1 == this.value) ? 1
				: (o1 instanceof SpacerUIObject) ? 2
				: (o1 == this.popup) ? 3
				: 4;

		final int r2 = (o2 == this.icon) ? 0
				: (o2 == this.value) ? 1
				: (o2 instanceof SpacerUIObject) ? 2
				: (o2 == this.popup) ? 3
				: 4;

		assert r1 != 4 : o1;
		assert r2 != 4 : o2;

		return Integer.compare(r1, r2);
	};

	public OverlayIntegerStatLine(final String str, final Transform3D transform, final UIObject... values) {
		super(str, new FlowLayout(false, gap), transform, values);
	}

	public OverlayIntegerStatLine(final String str, final UIObject... values) {
		super(str, new FlowLayout(false, gap), values);
	}

	public OverlayIntegerStatLine(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, new FlowLayout(false, gap), parent, values);
	}

	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> ObjectTriggerLatch<? extends OverlayIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final int valueLength,
			final int popupLength,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz) {

		final float iconHeightRatio = height / (float) TexturedQuadMeshUIObject.SQUARE_1_UNIT.getBounds2D().getHeight();
		final float textHeightRatio = height / UIObjectFactory.DEFAULT_CHAR_SIZE.y();

		final ObjectTriggerLatch<? extends OverlayIntegerStatLine> latch = new ObjectTriggerLatch<>(iconClazz == null ? 2 : 3, this);

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

		UIObjectFactory
				.createText(popupClazz,
						OptionalInt.of(valueLength + 1),
						Optional.empty(),
						Optional.empty(),
						Optional.of(this.getId() + "-popup"),
						Optional.empty())
				.set(i -> i.setNeutralColor(ColorMaterial.GRAY))
				.set(i -> i.setPositiveColor(ColorMaterial.LIGHT_GREEN))
				.set(i -> i.setNegativeColor(ColorMaterial.RED))
				.set(i -> i.setTransform(new Transform3D().scaleMul(textHeightRatio * POPUP_TEXT_SCALE)))
				.postInit(i -> this.popup = i)
				.add(this)
				.latch(latch)
				.push();

		return latch;
	}

	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> ObjectTriggerLatch<? extends OverlayIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz) {
		return this.init(workers, render, height, VALUE_LENGTH, POPUP_LENGTH, iconClazz, valueClazz, popupClazz);
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

	public SignedIntegerTextUIObject getPopup() {
		return this.popup;
	}

	public OverlayIntegerStatLine add(final int value) {
		if (this.getPopup() != null) {
			this.getPopup().setValue(this.getPopup().getValue() + value);
		}
		return this;
	}

	public OverlayIntegerStatLine set(final int value) {
		if (this.getPopup() != null && this.getValue() != null) {
			this.getPopup().setValue(value);
			this.getValue().setValue(0);
		}
		return this;
	}

	public OverlayIntegerStatLine flushValue() {
		if (this.getPopup() == null || this.getValue() == null) {
			return this;
		}
		this.getValue().setValue(this.getValue().getValue() + this.getPopup().getValue()).flushValue();
		this.getPopup().flushValue();
		this.getPopup().setValue(0);
		this.progress = this.progress > this.popupStayDuration + this.popupSpawnDuration ? 0 : this.popupSpawnDuration; // start animation
		return this;
	}

	@Override
	public void update(final WindowInputHandler input) {
		final SignedIntegerTextUIObject popup = this.getPopup();
		if (popup == null) {
			return;
		}

		if (!popup.getTextEmitter().getText().contains("%")) {
			return;
		}

		final float dTime = input.dTime();

//		final float dst = 0.25f;
//		final Transform3D transform = popup.getTransform();
		if (this.progress < this.popupSpawnDuration) {
			final float interpol = this.popupSpawnInterpolator.evaluate(this.progress / this.popupSpawnDuration);
			final TextEmitter text = popup.getTextEmitter();

//			transform.getTranslation().z = interpol * dst;
			text.setOpacity(interpol);
//			transform.updateMatrix();

			this.progress += dTime;

			if (this.progress >= this.popupSpawnDuration) {
				text.setOpacity(1);
			}
		} else if (this.progress >= this.popupSpawnDuration && this.progress < this.popupSpawnDuration + this.popupStayDuration) {
			this.progress += dTime;
		} else if (this.progress >= this.popupSpawnDuration + this.popupStayDuration
				&& this.progress < 2 * this.popupSpawnDuration + this.popupStayDuration) {
			final float interpol = this.popupSpawnInterpolator
					.evaluate((this.progress - (this.popupSpawnDuration + this.popupStayDuration)) / this.popupSpawnDuration);
			final TextEmitter text = popup.getTextEmitter();

//			transform.getTranslation().z = -interpol * dst;
			text.setOpacity(1f - interpol);
//			transform.updateMatrix();
			this.progress += dTime;

			if (this.progress >= 2 * this.popupSpawnDuration + this.popupStayDuration) {
				text.setOpacity(0);
			}
		} else if (this.progress >= 2 * this.popupSpawnDuration + this.popupStayDuration) {
			// do nothing
		}
	}

	@Override
	public void doSort() {
		super.sort(this.comparator);
	}

	@Override
	public int getMaxItems() {
		return 3;
	}

	@Override
	public String toString() {
		return "OverlayIntegerStatLine [icon=" + this.icon + ", value=" + this.value + ", popup=" + this.popup + ", popupSpawnDirection="
				+ this.popupSpawnDirection + ", popupSpawnDuration=" + this.popupSpawnDuration + ", popupStayDuration="
				+ this.popupStayDuration + ", progress=" + this.progress + ", popupSpawnInterpolator=" + this.popupSpawnInterpolator
				+ ", comparator=" + this.comparator + ", layout=" + this.layout + ", transform=" + this.transform + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", bounds=" + this.computedBounds + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
