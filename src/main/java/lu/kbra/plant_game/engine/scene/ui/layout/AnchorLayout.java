package lu.kbra.plant_game.engine.scene.ui.layout;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwner;
import lu.kbra.plant_game.engine.entity.ui.impl.BoundsOwnerParentAware;
import lu.kbra.plant_game.engine.scene.ui.overlay.AnchorOwner;
import lu.kbra.plant_game.engine.scene.ui.overlay.Margin2DOwner;
import lu.kbra.plant_game.engine.scene.ui.overlay.MarginOwner;
import lu.kbra.plant_game.engine.scene.ui.overlay.Padding2DOwner;
import lu.kbra.plant_game.engine.scene.ui.overlay.PaddingOwner;
import lu.kbra.standalone.gameengine.objs.entity.ParentAwareComponent;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class AnchorLayout implements Layout, BoundsOwnerParentAware {

	private ParentAwareComponent parent;

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
				GlobalLogger.info("Skipping not anchor: " + obj);
				continue;
			}
			if (!ao.isAnchored()) {
				GlobalLogger.info("Skipping anchor: " + ao);
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
			float marginX, marginZ = marginX = margin;
			if (obj instanceof final Margin2DOwner mo) {
				marginX += mo.getMarginX();
				marginZ += mo.getMarginZ();
			}
			if (boundsParent instanceof final Padding2DOwner mo) {
				marginX += mo.getPaddingX();
				marginZ += mo.getPaddingZ();
			}

			final Rectangle2D bounds = obj.getLocalTransformedBounds().getBounds2D();
			alignAnchors(obj.getTransform(), bounds, screenBounds, objectAnchor, targetAnchor, marginX, marginZ);
		}
	}

	public static void alignAnchors(
			final Transform3D objectTransform,
			final Rectangle2D objectBounds,
			final Rectangle2D targetBounds,
			final Anchor objectAnchor,
			final Anchor targetAnchor,
			final float marginX,
			final float marginZ) {

		final float objAnchorX = switch (objectAnchor) {
		case BOTTOM_LEFT:
		case CENTER_LEFT:
		case TOP_LEFT:
			yield (float) objectBounds.getMinX();
		case BOTTOM_CENTER:
		case CENTER_CENTER:
		case TOP_CENTER:
			yield (float) objectBounds.getCenterX();
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

		final float targetAnchorX = switch (targetAnchor) {
		case BOTTOM_LEFT:
		case CENTER_LEFT:
		case TOP_LEFT:
			yield (float) targetBounds.getMinX() + marginX;
		case BOTTOM_CENTER:
		case CENTER_CENTER:
		case TOP_CENTER:
			yield (float) targetBounds.getCenterX();
		case BOTTOM_RIGHT:
		case CENTER_RIGHT:
		case TOP_RIGHT:
			yield (float) targetBounds.getMaxX() - marginX;
		default:
			throw new IllegalArgumentException(Objects.toString(targetAnchor));
		};

		final float targetAnchorZ = switch (targetAnchor) {
		case BOTTOM_CENTER:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			yield (float) targetBounds.getMaxY() - marginZ;
		case CENTER_CENTER:
		case CENTER_LEFT:
		case CENTER_RIGHT:
			yield (float) targetBounds.getCenterY();
		case TOP_CENTER:
		case TOP_LEFT:
		case TOP_RIGHT:
			yield (float) targetBounds.getMinY() + marginZ;
		default:
			throw new IllegalArgumentException(Objects.toString(targetAnchor));
		};

		objectTransform.getTranslation().x = targetAnchorX - objAnchorX;
		objectTransform.getTranslation().z = targetAnchorZ - objAnchorZ;

		objectTransform.updateMatrix();
	}

	@Override
	public <T extends ParentAwareComponent> void setParent(final T e) {
		this.parent = e;
	}

	@Override
	public ParentAwareComponent getParent() {
		return this.parent;
	}

}
