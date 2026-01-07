package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.impl.Direction2d;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class FixedBoundsUIObjectGroup extends LayoutOffsetUIObjectGroup implements BoundsOwnerParentAware {

	protected Direction2d dir;
	protected Rectangle2D.Float bounds = new Rectangle2D.Float();
	protected float size;

	public FixedBoundsUIObjectGroup(final String str, final Layout layout, final Direction2d dir, final float size,
			final UIObject... values) {
		super(str, layout, values);
		this.dir = dir;
		this.size = size;
	}

	public FixedBoundsUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final Direction2d dir,
			final float size, final UIObject... values) {
		super(str, layout, transform, values);
		this.dir = dir;
		this.size = size;
	}

	public FixedBoundsUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Direction2d dir,
			final float size, final UIObject... values) {
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

		this.bounds.setFrame(switch (this.dir) {
		case VERTICAL -> new Rectangle2D.Float(-this.size / 2, (float) compBounds.getY(), this.size / 2, (float) compBounds.getHeight());
		case HORIZONTAL -> new Rectangle2D.Float((float) compBounds.getX(), -this.size / 2, (float) compBounds.getWidth(), this.size / 2);
//		default -> compBounds;
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

}
