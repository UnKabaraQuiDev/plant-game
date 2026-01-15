package lu.kbra.plant_game.vanilla.scene.overlay.group.impl;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchoredPBUIObjectGroup extends AnchoredLayoutUIObjectGroup implements BoundsOwnerParentAware {

	protected Direction2d dir;
	protected Rectangle2D.Float bounds = new Rectangle2D.Float();

	public AnchoredPBUIObjectGroup(final String str, final Layout layout, final Direction2d dir, final Anchor obj, final Anchor tar,
			final UIObject... values) {
		super(str, layout, obj, tar, values);
		this.dir = dir;
	}

	public AnchoredPBUIObjectGroup(final String str, final Layout layout, final Transform3D transform, final Direction2d dir,
			final UIObject... values) {
		super(str, layout, transform, values);
		this.dir = dir;
	}

	public AnchoredPBUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Direction2d dir,
			final UIObject... values) {
		super(str, layout, parent, values);
		this.dir = dir;
	}

	public AnchoredPBUIObjectGroup(final String str, final Layout layout, final UIObjectGroup parent, final Direction2d dir,
			final Anchor obj, final Anchor tar, final UIObject... values) {
		super(str, layout, parent, obj, tar, values);
		this.dir = dir;
	}

	@Override
	public boolean recomputeBounds() {
		final Optional<BoundsOwner> obo = this.getBoundsOwnerParent();
		if (obo.isEmpty() || this.dir == null) {
			return false;
		}
		final Rectangle2D parentBounds = obo.get().getBounds().getBounds2D();
		super.recomputeBounds();
		final Rectangle2D compBounds = super.computedBounds.getBounds2D();

		this.bounds = switch (this.dir) {
		case VERTICAL -> new Rectangle2D.Float((float) parentBounds.getX(),
				(float) compBounds.getY(),
				(float) parentBounds.getWidth(),
				(float) compBounds.getHeight());
		case HORIZONTAL -> new Rectangle2D.Float((float) compBounds.getX(),
				(float) parentBounds.getY(),
				(float) compBounds.getWidth(),
				(float) parentBounds.getHeight());
		};

		return true;
	}

	@Override
	public Shape getBounds() {
		return this.bounds;
	}

}
