package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.GameEngine;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface TransformedBoundsOwner extends BoundsOwner, Transform3DOwner {

	default Shape getTransformedBounds() {
		final Shape bounds = getBounds();

		final Transform3D transform = getTransform();
		final Vector3f pos3 = transform == null ? GameEngine.ZERO : transform.getTranslation();
		final Vector3f scale = transform == null ? GameEngine.IDENTITY_VECTOR3F : transform.getScale();
		final Quaternionf rotation = transform == null ? GameEngine.IDENTITY_QUATERNIONF : transform.getRotation();

		final Vector2f pos = GeoPlane.XZ.projectToPlane(pos3);

		final float angleY = rotation.getEulerAnglesXYZ(new Vector3f()).y;

		final AffineTransform affine = new AffineTransform();
		affine.translate(pos.x, pos.y);
		affine.rotate(-angleY);
		affine.scale(scale.x, scale.z);

		return affine.createTransformedShape(bounds);
	}

	default Shape getTransformedBounds(Matrix4f parentMatrix) {
		final Shape bounds = getBounds();

		final Transform3D transform = getTransform();
		final Matrix4f worldMatrix = new Matrix4f(parentMatrix);
		if (transform != null) {
			worldMatrix.mul(transform.getMatrix());
		}
		final Vector3f pos3 = worldMatrix.getTranslation(new Vector3f());
		final Quaternionf rotation = worldMatrix.getNormalizedRotation(new Quaternionf());
		final Vector3f scale = worldMatrix.getScale(new Vector3f());

		final Vector2f pos = GeoPlane.XZ.projectToPlane(pos3);

		final float angleY = rotation.getEulerAnglesXYZ(new Vector3f()).y;

		final AffineTransform affine = new AffineTransform();
		affine.translate(pos.x, pos.y);
		affine.rotate(-angleY);
		affine.scale(scale.x, scale.z);

		return affine.createTransformedShape(bounds);
	}

}
