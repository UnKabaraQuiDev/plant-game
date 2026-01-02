package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.Shape;

import lu.pcy113.pclib.concurrency.FutureTriggerLatch;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransform3DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.AbsoluteTransformedBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.prim.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.AnchorLayout;
import lu.kbra.plant_game.engine.window.input.WindowInputHandler;
import lu.kbra.standalone.gameengine.impl.future.Dispatcher;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareNode;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ExtAnchoredOverlayIntegerStatLine extends OverlayIntegerStatLine implements ExtAnchorOwner, MarginOwner {

	protected Anchor objAnchor;
	protected Anchor tarAnchor;
	protected UIObject target;
	protected float margin;

	public ExtAnchoredOverlayIntegerStatLine(final String str, final Transform3D transform, final UIObject... values) {
		super(str, transform, values);
	}

	public ExtAnchoredOverlayIntegerStatLine(final String str, final UIObject... values) {
		super(str, values);
	}

	public ExtAnchoredOverlayIntegerStatLine(final String str, final UIObjectGroup parent, final UIObject... values) {
		super(str, parent, values);
	}

	public ExtAnchoredOverlayIntegerStatLine(final String str, final Anchor obj, final Anchor tar, final UIObject target) {
		super(str);
		this.objAnchor = obj;
		this.tarAnchor = tar;
		this.target = target;
	}

	@Override
	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> FutureTriggerLatch<ExtAnchoredOverlayIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz) {
		return (FutureTriggerLatch<ExtAnchoredOverlayIntegerStatLine>) super.init(workers,
				render,
				height,
				iconClazz,
				valueClazz,
				popupClazz);
	}

	@Override
	public <T extends TexturedQuadMeshUIObject, V extends IntegerTextUIObject, P extends SignedIntegerTextUIObject> FutureTriggerLatch<ExtAnchoredOverlayIntegerStatLine> init(
			final Dispatcher workers,
			final Dispatcher render,
			final float height,
			final int valueLength,
			final int popupLength,
			final Class<T> iconClazz,
			final Class<V> valueClazz,
			final Class<P> popupClazz) {
		return (FutureTriggerLatch<ExtAnchoredOverlayIntegerStatLine>) super.init(workers,
				render,
				height,
				valueLength,
				popupLength,
				iconClazz,
				valueClazz,
				popupClazz);
	}

	@Override
	public void update(final WindowInputHandler input) {
		super.update(input);

		if (this.isAnchored()) {
			final Shape targetBounds = AbsoluteTransformedBoundsOwner.getAbsoluteTransformedBounds(this.target);

			AnchorLayout.alignAnchors(this.transform, this.getBounds().getBounds2D(),
//					this.target.getTransform(),
					targetBounds.getBounds2D(),
					this.objAnchor,
					this.tarAnchor,
					this.margin,
					this.margin);
		}
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
	public boolean isAnchored() {
		return ExtAnchorOwner.super.isAnchored() && this.target != null;
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
	public UIObject getTarget() {
		return this.target;
	}

	@Override
	public void setTarget(final UIObject target) {
		if (!(target instanceof ParentAwareNode || target instanceof AbsoluteTransform3DOwner
				|| target instanceof AbsoluteTransformedBoundsOwner)) {
			throw new IllegalArgumentException(target.getClass().getName() + " doesn't have an accessible hierarchy.");
		}
		this.target = target;
	}

	@Override
	public String toString() {
		return "ExtAnchoredOverlayIntegerStatLine [objAnchor=" + this.objAnchor + ", tarAnchor=" + this.tarAnchor + ", target="
				+ this.target + ", icon=" + this.icon + ", value=" + this.value + ", popup=" + this.popup + ", popupSpawnDirection="
				+ this.popupSpawnDirection + ", popupSpawnDuration=" + this.popupSpawnDuration + ", popupStayDuration="
				+ this.popupStayDuration + ", progress=" + this.progress + ", popupSpawnInterpolator=" + this.popupSpawnInterpolator
				+ ", comparator=" + this.comparator + ", layout=" + this.layout + ", transform=" + this.transform + ", subEntitiesLock="
				+ this.subEntitiesLock + ", subEntities=" + this.subEntities + ", bounds=" + this.bounds + ", active=" + this.active
				+ ", name=" + this.name + "]";
	}

}
