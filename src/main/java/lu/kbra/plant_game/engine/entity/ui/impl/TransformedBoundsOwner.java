package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;

public interface TransformedBoundsOwner extends BoundsOwner, Transform3DOwner {

	default Shape getTransformedBounds() {
		final Shape bounds = getBounds();
		final Vector3f pos3 = getTransform() == null ? GameEngine.ZERO : getTransform().getTranslation();
		final Vector3f scale = getTransform() == null ? GameEngine.IDENTITY_VECTOR3F : getTransform().getScale();
		final Quaternionf rotation = getTransform() == null ? GameEngine.IDENTITY_QUATERNIONF : getTransform().getRotation();
		final Vector2f pos = GeoPlane.XZ.projectToPlane(pos3);

		final float angleY = rotation.getEulerAnglesXYZ(new Vector3f()).y;

		final AffineTransform transform = new AffineTransform();
		transform.translate(pos.x, pos.y);
		transform.rotate(-angleY);
		transform.scale(scale.x, scale.z);

		return transform.createTransformedShape(bounds);
	}
	
}
