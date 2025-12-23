package lu.kbra.plant_game.engine.entity.ui.group;

import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.ScrollBarUIObject;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class ScrollContainerUIObjectGroup extends OffsetUIObjectGroup {

	private final ScrollDrivenUIObjectGroup scrollContent;
	private ScrollBarUIObject scrollBar;

	public ScrollContainerUIObjectGroup(
			final String str,
			final Transform3D transform,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, transform, values);
		this.scrollContent = new ScrollDrivenUIObjectGroup(
				str + ".scroll-content",
				this,
				() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(),
				dir,
				margin,
				values);
	}

	public ScrollContainerUIObjectGroup(final String str, final Direction dir, final float margin, final UIObject... values) {
		super(str, values);
		this.scrollContent = new ScrollDrivenUIObjectGroup(
				str + ".scroll-content",
				this,
				() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(),
				dir,
				margin,
				values);
	}

	public ScrollContainerUIObjectGroup(
			final String str,
			final UIObjectGroup parent,
			final Direction dir,
			final float margin,
			final UIObject... values) {
		super(str, parent, values);
		this.scrollContent = new ScrollDrivenUIObjectGroup(
				str + ".scroll-content",
				this,
				() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(),
				dir,
				margin,
				values);
	}

	public ScrollContainerUIObjectGroup(
			final String str,
			final Transform3D transform,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, transform, values);
		this.scrollContent = new LayoutScrollDrivenUIObjectGroup(
				str + ".scroll-content",
				this,
				() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(),
				dir,
				margin,
				layout,
				values);
	}

	public ScrollContainerUIObjectGroup(
			final String str,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, values);
		this.scrollContent = new LayoutScrollDrivenUIObjectGroup(
				str + ".scroll-content",
				this,
				() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(),
				dir,
				margin,
				layout,
				values);
	}

	public ScrollContainerUIObjectGroup(
			final String str,
			final UIObjectGroup parent,
			final Direction dir,
			final float margin,
			final Layout layout,
			final UIObject... values) {
		super(str, parent, values);
		this.scrollContent = new LayoutScrollDrivenUIObjectGroup(
				str + ".scroll-content",
				this,
				() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio(),
				dir,
				margin,
				layout,
				values);
	}

	public ScrollBarUIObject getScrollBar() {
		return this.scrollBar;
	}

	public ScrollDrivenUIObjectGroup getScrollContent() {
		return this.scrollContent;
	}

	public void setScrollBar(final ScrollBarUIObject scrollBar) {
		this.scrollBar = scrollBar;
		this.scrollContent.setScrollRatioSupplier(() -> this.scrollBar == null ? 0 : this.scrollBar.getScrollRatio());
	}

	@Override
	public String toString() {
		return "ScrollContainerUIObjectGroup [scrollContent=" + this.scrollContent + ", scrollBar=" + this.scrollBar + ", active="
				+ this.active + ", name=" + this.name + ", getTransform()=" + this.getTransform() + ", size()=" + this.size() + "]";
	}

}
