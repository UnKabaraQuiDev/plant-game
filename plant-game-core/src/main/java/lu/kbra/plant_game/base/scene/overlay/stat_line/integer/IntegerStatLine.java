package lu.kbra.plant_game.base.scene.overlay.stat_line.integer;

import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.LimitedObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.NeedsUpdate;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
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

public class IntegerStatLine extends LayoutOffsetUIObjectGroup implements LimitedObjectGroup<UIObject>, NeedsUpdate {

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
	protected boolean percentageMode = false;

	protected int targetValue;
	protected int lastTargetValue;
	protected float ratePerSecond;
	protected long lastTargetTimeNs = -1;

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

	public IntegerStatLine(final String str, final Transform3D transform, final UIObject... values) {
		super(str, new FlowLayout(false, gap), transform, values);
	}

	public IntegerStatLine(final String str, final UIObject... values) {
		super(str, new FlowLayout(false, gap), values);
	}

	public IntegerStatLine(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, new FlowLayout(false, gap), parent, values);
	}

	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> ObjectTriggerLatch<? extends IntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final int valueLength,
			final int popupLength,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz,
			final boolean percentage) {

		this.percentageMode = percentage;

		final float iconHeightRatio = height / (float) TexturedQuadMeshUIObject.SQUARE_1_UNIT.getBounds2D().getHeight();
		final float textHeightRatio = height / UIObjectFactory.DEFAULT_CHAR_SIZE.y();

		final ObjectTriggerLatch<? extends IntegerStatLine> latch = new ObjectTriggerLatch<>(iconClazz == null ? 2 : 3, this);

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
				.set(i -> i.setValue(-1))
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

	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> ObjectTriggerLatch<? extends IntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz,
			final boolean percentage) {
		return this.init(workers, render, height, VALUE_LENGTH, POPUP_LENGTH, iconClazz, valueClazz, popupClazz, percentage);
	}

	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> ObjectTriggerLatch<? extends IntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz) {
		return this.init(workers, render, height, VALUE_LENGTH, POPUP_LENGTH, iconClazz, valueClazz, popupClazz, false);
	}

	public IntegerStatLine setTarget(final int value) {
		if (value == this.targetValue) {
			return this;
		}

		final long now = System.nanoTime();

		if (this.lastTargetTimeNs != -1) {
			final float dt = (now - this.lastTargetTimeNs) / 1e9f;

			if (dt > 0) {
				final float newRate = (value - this.lastTargetValue) / dt;
				this.ratePerSecond = this.ratePerSecond * 0.85f + newRate * 0.15f;
			}
		}

		this.lastTargetValue = value;
		this.lastTargetTimeNs = now;
		this.targetValue = value;

		this.progress = this.progress < this.popupSpawnDuration ? this.progress
				: this.progress > this.popupSpawnDuration && this.progress < this.popupSpawnDuration + this.popupStayDuration
						? this.popupSpawnDuration
				: this.progress > 2 * this.popupSpawnDuration + this.popupStayDuration ? 0
				: this.progress;

		return this;
	}

	@Deprecated
	public IntegerStatLine add(final int value) {
		if (this.getPopup() != null) {
			this.getPopup().setValue(this.getPopup().getValue() + value);
		}
		return this;
	}

	@Deprecated
	public IntegerStatLine set(final int value) {
		if (this.getPopup() != null && this.getValue() != null) {
			this.getPopup().setValue(value);
			this.getValue().setValue(0);
		}
		return this;
	}

	@Deprecated
	public IntegerStatLine flushValue() {
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
		if (this.popup == null || this.value == null) {
			return;
		}

		if ((System.nanoTime() - this.lastTargetTimeNs) * 1e9f > this.popupSpawnDuration * 2 + this.popupStayDuration) {
			this.progress = 0;
		}

		final int current = this.value.getValue();

		if (current != this.targetValue) {
			final double speed = Math.max(20, Math.abs(this.ratePerSecond));
			final double delta = speed * input.dTime();

			final int step = (int) Math.signum(this.targetValue - current) * (int) Math.ceil(delta);

			int next = current + step;

			if ((this.targetValue - current) * (this.targetValue - next) <= 0) {
				next = this.targetValue;
			}

			this.value.setValue(next);
			this.popup.setValue(step);
			if (this.percentageMode) {
				this.value.flushValue("%");
				this.popup.flushValue("%");
			} else {
				this.value.flushValue();
				this.popup.flushValue();
			}
			this.popup.setActive(true);
		}

		final float dTime = input.dTime();

		if (this.progress < this.popupSpawnDuration) {
			final float interpol = this.popupSpawnInterpolator.evaluate(this.progress / this.popupSpawnDuration);
			final TextEmitter text = this.popup.getTextEmitter();

			text.setOpacity(interpol);

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
			final TextEmitter text = this.popup.getTextEmitter();

			text.setOpacity(1f - interpol);
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

	public boolean isPercentageMode() {
		return this.percentageMode;
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
