package lu.kbra.plant_game.engine.entity.ui.bar;

import java.awt.geom.Rectangle2D;

import org.joml.Vector3f;
import org.joml.Vector4fc;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.concurrency.FutureTriggerLatch;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.mesh.ColorMaterialOwner;
import lu.kbra.plant_game.engine.mesh.TintOwner;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.generated.ColorMaterial;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.kbra.standalone.gameengine.utils.transform.Transform3DPivot;

public class ProgressBarUIObject extends OffsetUIObjectGroup implements LimitedObjectGroup<UIObject> {

	public static final float Y_OFFSET = 0.01f;
	public static final ColorMaterial DEFAULT_BG_COLOR = ColorMaterial.BLACK;
	public static final ColorMaterial DEFAULT_FG_COLOR = ColorMaterial.PINK;

	protected float margin = 0.1f;
	protected float value = 0.5f;
	protected UIObject background;
	protected UIObject foreground;

	public ProgressBarUIObject(final String str, final Transform3D transform, final UIObject... values) {
		super(str, transform, values);
	}

	public ProgressBarUIObject(final String str, final UIObject... values) {
		super(str, values);
	}

	public ProgressBarUIObject(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, parent, values);
	}

	public ProgressBarUIObject(final String str, final UIScene parent, final UIObject... values) {
		super(str, parent, values);
	}

	public ProgressBarUIObject(final String str, final UIObjectGroup parent, final Transform3D transform, final UIObject... values) {
		super(str, parent, transform, values);
	}

	public ProgressBarUIObject(final String str, final UIScene parent, final Transform3D transform, final UIObject... values) {
		super(str, parent, transform, values);
	}

	public ProgressBarUIObject(final String str, final UIScene parent, final Transform3D transform, final float margin, final float value) {
		super(str, parent, transform);
		this.margin = margin;
		this.value = value;
	}

	public <T extends UIObject & ColorMaterialOwner, V extends UIObject & ColorMaterialOwner> FutureTriggerLatch<ProgressBarUIObject> init(
			final Dispatcher workers,
			final Dispatcher render,
			final Class<T> bgClazz,
			final Class<V> fgClazz,
			final ColorMaterial bg,
			final ColorMaterial fg) {

		final FutureTriggerLatch<ProgressBarUIObject> latch = new FutureTriggerLatch<>(2, this);

		UIObjectFactory.create(bgClazz)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setColorMaterial(bg))
				.set(i -> this.background = i)
				.add(this)
				.latch(latch)
				.set(i -> {
					if (this.foreground != null) {
						this.updateScaling();
					}
				})
				.push();
		UIObjectFactory.create(fgClazz)
				.set(i -> i.setTransform(new Transform3DPivot().scalePivotSet(-0.5f, 0, 0)))
				.set(i -> i.setColorMaterial(fg))
				.set(i -> this.foreground = i)
				.add(this)
				.latch(latch)
				.set(i -> {
					if (this.background != null) {
						this.updateScaling();
					}
				})
				.push();

		return latch;
	}

	public <T extends UIObject & ColorMaterialOwner, V extends UIObject & ColorMaterialOwner> FutureTriggerLatch<ProgressBarUIObject> init(
			final Dispatcher workers,
			final Dispatcher render,
			final Class<T> bgClazz,
			final Class<V> fgClazz) {
		return this.init(workers, render, bgClazz, fgClazz, DEFAULT_BG_COLOR, DEFAULT_FG_COLOR);
	}

	public float getValue() {
		return this.value;
	}

	public ProgressBarUIObject setValue(final float value) {
		this.value = PCUtils.clamp(0, 1, value);
		// this.updateScaling();
		return this;
	}

	public ProgressBarUIObject updateScaling() {
		if (this.foreground == null || this.background == null) {
			return this;
		}

		if (this.value == 0) {
			this.foreground.setActive(false);
			return this;
		}
		this.foreground.setActive(true);

		final Transform3D transform = this.getTransform();
		final Vector3f scale = transform.getScale();
		final float marginX = this.margin / scale.x;
		final float marginZ = this.margin / scale.z;
		final Rectangle2D bgBounds = this.background.getLocalTransformedBounds().getBounds2D();
		if (marginZ >= bgBounds.getHeight()) {
			GlobalLogger.severe("Margin (" + this.margin + ") on Z (" + marginZ + ") too big for scale (" + scale.z + ") and height ("
					+ bgBounds.getHeight() + ")");
		}
		if (marginX >= bgBounds.getWidth()) {
			GlobalLogger.severe("Margin (" + this.margin + ") on X (" + marginX + ") too big for scale (" + scale.x + ") and width ("
					+ bgBounds.getWidth() + ")");
		}

		this.foreground.getTransform()
				.scaleSet(org.joml.Math.lerp(0, 1 - 2 * marginX, this.value), 1, 1 - 2 * marginZ)
				.translationSet(marginX, Y_OFFSET, 0)
				.updateMatrix();

		return this;
	}

	public ProgressBarUIObject setForegroundColor(final Vector4fc color) {
		if (this.foreground == null || this.background == null) {
			return this;
		}
		if (!(this.foreground instanceof final TintOwner to)) {
			throw new UnsupportedOperationException(this.foreground.getClass().getName() + " isn't a " + TintOwner.class.getSimpleName());
		}
		to.setTint(color);
		return this;
	}

	public ProgressBarUIObject setBackgroundColor(final Vector4fc color) {
		if (this.foreground == null || this.background == null) {
			return this;
		}
		if (!(this.background instanceof final TintOwner to)) {
			throw new UnsupportedOperationException(this.background.getClass().getName() + " isn't a " + TintOwner.class.getSimpleName());
		}
		to.setTint(color);
		return this;
	}

	public ProgressBarUIObject setForegroundColor(final ColorMaterial color) {
		return this.setForegroundColor(color.getColor());
	}

	public ProgressBarUIObject setBackgroundColor(final ColorMaterial color) {
		return this.setBackgroundColor(color.getColor());
	}

	@Override
	public int getMaxItems() {
		return 2;
	}

//	@Override
//	public <V extends UIObject> boolean addChildren(final ObjectGroup<? extends V> c) {
//		return LimitedObjectGroup.super.addChildren(c);
//	}

}
