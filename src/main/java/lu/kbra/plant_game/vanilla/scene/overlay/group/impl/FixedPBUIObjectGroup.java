package lu.kbra.plant_game.vanilla.scene.overlay.group.impl;

import java.awt.geom.Rectangle2D;
import java.util.Optional;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class FixedPBUIObjectGroup extends FixedBoundsUIObjectGroup {

	public FixedPBUIObjectGroup(final String str, final Layout layout, final Direction2d dir, final float size, final UIObject... values) {
		super(str, layout, dir, size, values);
	}

	public FixedPBUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final Direction2d dir, final float size,
			final UIObject... values) {
		super(str, layout, transform, dir, size, values);
	}

	public FixedPBUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Direction2d dir, final float size,
			final UIObject... values) {
		super(str, layout, parent, dir, size, values);
	}

	@Override
	public boolean recomputeBounds() {
		final Optional<BoundsOwner> opo = this.getBoundsOwnerParent();
		if (this.dir == null || opo.isEmpty()) {
			return false;
		}

		final Rectangle2D parentBounds = opo.get().getBounds().getBounds2D();
		super.recomputeBounds();
//		final Rectangle2D compBounds = super.computedBounds.getBounds2D();

		final float paddingX = (float) (this.parallelStream().mapToDouble(marginSumX).sum() + paddingSumX.applyAsDouble(this));
		final float paddingZ = (float) (this.parallelStream().mapToDouble(marginSumZ).sum() + paddingSumZ.applyAsDouble(this));

		this.bounds.setFrame(switch (this.dir) {
		case VERTICAL -> new Rectangle2D.Float(-this.size / 2 + paddingX,
				(float) parentBounds.getY() + paddingZ,
				this.size - 2 * paddingX,
				(float) parentBounds.getHeight() - 2 * paddingZ);
		case HORIZONTAL -> new Rectangle2D.Float((float) parentBounds.getX() + paddingX,
				-this.size / 2 + paddingZ,
				(float) parentBounds.getWidth() - 2 * paddingX,
				this.size + 2 * paddingZ);
		});

		return true;
	}
}
