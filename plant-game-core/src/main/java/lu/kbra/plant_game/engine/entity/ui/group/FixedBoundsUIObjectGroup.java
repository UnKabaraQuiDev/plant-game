package lu.kbra.plant_game.engine.entity.ui.group;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

/**
 * Direction = Sub-components' size<br>
 * Horizontal = Vertical size + horizontal children<br>
 * Vertical = Horizontal size + vertical children<br>
 */
public class FixedBoundsUIObjectGroup extends LayoutOffsetUIObjectGroup implements BoundsOwnerParentAware {

	/**
	 * component size
	 */
	protected Direction2d dir;
	protected Rectangle2D.Float bounds = new Rectangle2D.Float();
	protected float size;

	public FixedBoundsUIObjectGroup(
			final String str,
			final Layout layout,
			final Direction2d dir,
			final float size,
			final UIObject... values) {
		super(str, layout, values);
		this.dir = dir;
		this.size = size;
	}

	public FixedBoundsUIObjectGroup(
			final String str,
			final Layout layout,
			final Transform3D transform,
			final Direction2d dir,
			final float size,
			final UIObject... values) {
		super(str, layout, transform, values);
		this.dir = dir;
		this.size = size;
	}

	public FixedBoundsUIObjectGroup(
			final String str,
			final Layout layout,
			final UIObjectGroup parent,
			final Direction2d dir,
			final float size,
			final UIObject... values) {
		super(str, layout, parent, values);
		this.dir = dir;
		this.size = size;
	}

	@Override
	public boolean recomputeBounds() {
		if (this.dir == null) {
			return false;
		}

		super.recomputeBounds();
		final Rectangle2D compBounds = super.computedBounds.getBounds2D();

		final float outerPaddingX = (float) paddingSumX.applyAsDouble(this);
		final float outerPaddingZ = (float) paddingSumZ.applyAsDouble(this);

		this.bounds.setFrame(switch (this.dir) {
		case VERTICAL -> new Rectangle2D.Float(-this.size / 2 - outerPaddingX,
				(float) compBounds.getY() - outerPaddingZ,
				this.size / 2 + 2 * outerPaddingX,
				(float) compBounds.getHeight() + 2 * outerPaddingZ);
		case HORIZONTAL -> new Rectangle2D.Float((float) compBounds.getX() - outerPaddingX,
				-this.size / 2 - outerPaddingZ,
				(float) compBounds.getWidth() + 2 * outerPaddingX,
				this.size / 2 + 2 * outerPaddingZ);
		});

		return true;
	}

	public float getSize() {
		return this.size;
	}

	public void setSize(final float size) {
		this.size = size;
	}

	@Override
	public Shape getBounds() {
		return this.bounds;
	}

	@Override
	public String toString() {
		return "FixedBoundsUIObjectGroup@" + System.identityHashCode(this) + " [dir=" + this.dir + ", bounds=" + this.bounds + ", size="
				+ this.size + ", layout=" + this.layout + ", subEntitiesLock=" + this.subEntitiesLock + ", subEntities=" + this.subEntities
				+ ", computedBounds=" + this.computedBounds + ", transform=" + this.transform + ", active=" + this.active + ", name="
				+ this.name + "]";
	}

}
