package lu.kbra.plant_game.engine.entity.ui.group;

import java.awt.geom.Rectangle2D;
import java.util.Optional;

import org.joml.Vector2f;
import org.joml.Vector3fc;

import lu.kbra.pclib.concurrency.ObjectTriggerLatch;
import lu.kbra.plant_game.engine.entity.impl.BoundsSceneParentAware;
import lu.kbra.plant_game.engine.entity.impl.SceneBoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.factory.UIObjectFactory;
import lu.kbra.plant_game.engine.entity.ui.scroller.ScrollBarUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

@Deprecated
public class ScrollContainerUIObjectGroup extends OffsetUIObjectGroup implements BoundsSceneParentAware {

	private final ScrollDrivenUIObjectGroup scrollContent;
	private ScrollBarUIObject scrollBar;

	@Deprecated
	public ScrollContainerUIObjectGroup(
			final String str,
			final Transform3D transform,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, transform, values);
		this.scrollContent = new ScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, values);
	}

	@Deprecated
	public ScrollContainerUIObjectGroup(final String str, final Direction dir, final float margin, final UIObject... values) {
		super(str, values);
		this.scrollContent = new ScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, values);
	}

	@Deprecated
	public ScrollContainerUIObjectGroup(
			final String str,
			final Vector3fc pos,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, new Transform3D(pos), values);
		this.scrollContent = new ScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, values);
	}

	@Deprecated
	public ScrollContainerUIObjectGroup(
			final String str,
			final UIObjectGroup parent,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, parent, values);
		this.scrollContent = new ScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, values);
	}

	@Deprecated
	public ScrollContainerUIObjectGroup(
			final String str,
			final Transform3D transform,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, transform, values);
		this.scrollContent = new LayoutScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, layout, values);
	}

	@Deprecated
	public ScrollContainerUIObjectGroup(
			final String str,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, values);
		this.scrollContent = new LayoutScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, layout, values);
	}

	@Deprecated
	public ScrollContainerUIObjectGroup(
			final String str,
			final Vector3fc pos,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, new Transform3D(pos), values);
		this.scrollContent = new LayoutScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, layout, values);
	}

	@Deprecated
	public ScrollContainerUIObjectGroup(
			final String str,
			final UIObjectGroup parent,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, parent, values);
		this.scrollContent = new LayoutScrollDrivenUIObjectGroup(str
				+ ".scroll-content", this, () -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(), dir, margin, layout, values);
	}

	@Deprecated
	public void updateScrollBar() {
		final Optional<SceneBoundsOwner> scene = this.getBoundsSceneParent();
		if (this.scrollBar == null || scene.isEmpty()) {
			return;
		}
		this.scrollBar.setActive(this.scrollContent.needsScrollBar());
		final Rectangle2D sceneBounds = scene.get().getBounds().getBounds2D();
		this.scrollBar.setRange(new Vector2f((float) sceneBounds.getMinX() + this.scrollBar.getMargin() - (float) sceneBounds.getCenterX(),
				(float) sceneBounds.getMaxX() - this.scrollBar.getMargin() + (float) sceneBounds.getCenterX()));
	}

	@Deprecated
	public ScrollBarUIObject getScrollBar() {
		return this.scrollBar;
	}

	@Deprecated
	public ScrollDrivenUIObjectGroup getScrollContent() {
		return this.scrollContent;
	}

	@Deprecated
	public <T extends ScrollBarUIObject> ObjectTriggerLatch<? extends ScrollContainerUIObjectGroup> init(final Class<T> clazz) {
		final ObjectTriggerLatch<? extends ScrollContainerUIObjectGroup> latch = new ObjectTriggerLatch<>(1, this);
		UIObjectFactory.create(clazz)
				.set(i -> i.setTransform(new Transform3D()))
				.set(i -> i.setDir(this.scrollContent.getDirection()))
				.set(i -> i.setRange(new Vector2f(-1, 1)))
				.postInit(this::setScrollBar)
				.latch(latch)
				.push();
		return latch;
	}

	@Deprecated
	public void setScrollBar(final ScrollBarUIObject scrollBar) {
		this.replace(this.scrollBar, scrollBar);
		this.scrollBar = scrollBar;
		this.scrollContent.setScrollRatioSupplier(() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio());
		this.updateScrollBar();
	}

	@Deprecated
	@Override
	public String toString() {
		return "ScrollContainerUIObjectGroup [scrollContent=" + this.scrollContent + ", scrollBar=" + this.scrollBar + ", active="
				+ this.active + ", name=" + this.name + ", getTransform()=" + this.getTransform() + ", size()=" + this.size() + "]";
	}

}
