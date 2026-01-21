package lu.kbra.plant_game.vanilla.scene.overlay.group.impl;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.function.ToDoubleFunction;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.entity.ui.impl.Margin2DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.MarginOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.Padding2DOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.PaddingOwner;
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

	protected static final ToDoubleFunction<? super UIObject> marginSumX = c -> {
		float padding = 0;
		if (c instanceof MarginOwner po) {
			padding += po.getMargin();
		}
		if (c instanceof Margin2DOwner po) {
			padding += po.getMarginX();
		}
		return padding;
	};
	protected static final ToDoubleFunction<? super UIObject> marginSumZ = c -> {
		float padding = 0;
		if (c instanceof MarginOwner po) {
			padding += po.getMargin();
		}
		if (c instanceof Margin2DOwner po) {
			padding += po.getMarginZ();
		}
		return padding;
	};

	protected static final ToDoubleFunction<? super UIObject> paddingSumX = c -> {
		float padding = 0;
		if (c instanceof PaddingOwner po) {
			padding += po.getPadding();
		}
		if (c instanceof Padding2DOwner po) {
			padding += po.getPaddingX();
		}
		return padding;
	};
	protected static final ToDoubleFunction<? super UIObject> paddingSumZ = c -> {
		float padding = 0;
		if (c instanceof PaddingOwner po) {
			padding += po.getPadding();
		}
		if (c instanceof Padding2DOwner po) {
			padding += po.getPaddingZ();
		}
		return padding;
	};

	@Override
	public boolean recomputeBounds() {
		if (this.dir == null) {
			return false;
		}

		super.recomputeBounds();
		final Rectangle2D compBounds = super.computedBounds.getBounds2D();

		final float paddingX = (float) (this.parallelStream().mapToDouble(marginSumX).sum() + paddingSumX.applyAsDouble(this));
		final float paddingZ = (float) (this.parallelStream().mapToDouble(marginSumZ).sum() + paddingSumZ.applyAsDouble(this));

		this.bounds.setFrame(switch (this.dir) {
		case VERTICAL -> new Rectangle2D.Float(-this.size / 2 - paddingX, (float) compBounds.getY() - paddingZ,
				this.size / 2 + 2 * paddingX, (float) compBounds.getHeight() + 2 * paddingZ);
		case HORIZONTAL -> new Rectangle2D.Float((float) compBounds.getX() - paddingX, -this.size / 2 - paddingZ,
				(float) compBounds.getWidth() + 2 * paddingX, this.size / 2 + 2 * paddingZ);
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
