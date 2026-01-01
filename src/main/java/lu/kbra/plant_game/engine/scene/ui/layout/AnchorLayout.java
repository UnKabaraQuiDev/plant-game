package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.overlay.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.overlay.MarginOwner;
import lu.kbra.plant_game.engine.scene.ui.overlay.PaddingOwner;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchorLayout implements Layout, BoundsOwnerParentAware {

	private Object parent;

	@Override
	public void doLayout(final List<UIObject> children) {
		final BoundsOwner boundsParent = this.getBoundsOwnerParent();
		if (boundsParent == null) {
			return;
		}

		final Rectangle2D screenBounds = boundsParent.getBounds().getBounds2D();

		for (final UIObject obj : children) {
			if (!obj.hasTransform()) {
				continue;
			}
			final Anchor objectAnchor;
			final Anchor targetAnchor;
			if (!(obj instanceof final AnchorOwner ao)) {
				continue;
			}
			objectAnchor = ao.getObjectAnchor();
			targetAnchor = ao.getTargetAnchor();

			float margin = 0;
			// child has margin outwards
			if (obj instanceof final MarginOwner mo) {
				margin += mo.getMargin();
			}
			// parent has padding inwards
			if (boundsParent instanceof final PaddingOwner po) {
				margin += po.getPadding();
			}

			final Rectangle2D bounds = obj.getLocalTransformedBounds().getBounds2D();
			alignAnchors(obj.getTransform(), bounds, screenBounds, objectAnchor, targetAnchor, margin, margin);
		}
	}

	public static void alignAnchors(
			final Transform3D transform,
			final Rectangle2D objectBounds,
			final Rectangle2D screenBounds,
			final Anchor objectAnchor,
			final Anchor screenAnchor,
			final float marginX,
			final float marginZ) {

		final float objAnchorX = switch (objectAnchor) {
		case BOTTOM_CENTER:
		case CENTER_CENTER:
		case TOP_CENTER:
			yield (float) objectBounds.getCenterX();
		case BOTTOM_LEFT:
		case CENTER_LEFT:
		case TOP_LEFT:
			yield (float) objectBounds.getMinX();
		case BOTTOM_RIGHT:
		case CENTER_RIGHT:
		case TOP_RIGHT:
			yield (float) objectBounds.getMaxX();
		default:
			throw new IllegalArgumentException(Objects.toString(objectAnchor));
		};

		final float objAnchorZ = switch (objectAnchor) {
		case BOTTOM_CENTER:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			yield (float) objectBounds.getMaxY();
		case CENTER_CENTER:
		case CENTER_LEFT:
		case CENTER_RIGHT:
			yield (float) objectBounds.getCenterY();
		case TOP_CENTER:
		case TOP_LEFT:
		case TOP_RIGHT:
			yield (float) objectBounds.getMinY();
		default:
			throw new IllegalArgumentException(Objects.toString(objectAnchor));
		};

		final float screenAnchorX = switch (screenAnchor) {
		case BOTTOM_CENTER:
		case CENTER_CENTER:
		case TOP_CENTER:
			yield (float) screenBounds.getCenterX();
		case BOTTOM_LEFT:
		case CENTER_LEFT:
		case TOP_LEFT:
			// TODO: this isn't right, it should be #getMinX()
			yield -(float) screenBounds.getWidth() + marginX;
		case BOTTOM_RIGHT:
		case CENTER_RIGHT:
		case TOP_RIGHT:
			yield (float) screenBounds.getWidth() - marginX;
		default:
			throw new IllegalArgumentException(Objects.toString(screenAnchor));
		};

		final float screenAnchorZ = switch (screenAnchor) {
		case BOTTOM_CENTER:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			yield (float) screenBounds.getMaxY() - marginZ;
		case CENTER_CENTER:
		case CENTER_LEFT:
		case CENTER_RIGHT:
			yield (float) screenBounds.getCenterY();
		case TOP_CENTER:
		case TOP_LEFT:
		case TOP_RIGHT:
			yield (float) screenBounds.getMinY() + marginZ;
		default:
			throw new IllegalArgumentException(Objects.toString(screenAnchor));
		};

		transform.getTranslation().x = screenAnchorX - objAnchorX;
		transform.getTranslation().z = screenAnchorZ - objAnchorZ;

		transform.updateMatrix();
	}

	@Override
	public void setParent(final Object e) {
		this.parent = e;
	}

	@Override
	public Object getParent() {
		return this.parent;
	}

}
