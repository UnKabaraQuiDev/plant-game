package lu.kbra.plant_game.engine.entity.ui.impl;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import lu.kbra.plant_game.engine.entity.impl.Transform3DOwner;
import lu.kbra.standalone.gameengine.utils.geo.GeoPlane;
import lu.kbra.standalone.gameengine.utils.transform.Transform;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public interface TransformedBoundsOwner extends BoundsOwner, Transform3DOwner {

	default Shape getLocalTransformedBounds() {
		return getLocalTransformedBounds(this.getBounds(), this.getTransform());
	}

	default Shape getTransformedBounds() {
		return getTransformedBounds(this.getBounds(), this.getTransform());
	}

	default Shape getTransformedBounds(final Matrix4fc parentMatrix) {
		return getTransformedBounds(this.getBounds(), this.getTransform(), parentMatrix);
	}

	default Shape getTransformedBounds(final Rectangle2D bounds) {
		return getTransformedBounds(this.getBounds(), this.getTransform());
	}

	static Shape getLocalTransformedBounds(final Shape bounds, final Transform3D transform) {
		if (transform == null) {
			return bounds;
		}
		final Matrix4f worldMatrix = transform.getMatrix();

		final Vector3fc pos3 = transform.getTranslation();
		final Vector2fc pos = GeoPlane.XZ.projectToPlane(pos3);

		// Build the 2D linear part by transforming the X and Z basis vectors
		final Vector3f basisX = new Vector3f(1, 0, 0);
		final Vector3f basisZ = new Vector3f(0, 0, 1);
		worldMatrix.transformDirection(basisX);
		worldMatrix.transformDirection(basisZ);

		// Map XZ components to 2D affine
		final double m00 = basisX.x();
		final double m01 = basisZ.x();
		final double m10 = basisX.z();
		final double m11 = basisZ.z();

		final AffineTransform affine = new AffineTransform(m00, m10, m01, m11, 0, 0);
		return affine.createTransformedShape(bounds);
	}

	static Shape getTransformedBounds(final Shape bounds, final Transform3D transform) {
		if (transform == null) {
			return bounds;
		}
		final Matrix4f worldMatrix = transform.getMatrix();

		final Vector3fc pos3 = transform.getTranslation();
		final Vector2fc pos = GeoPlane.XZ.projectToPlane(pos3);

		// Build the 2D linear part by transforming the X and Z basis vectors
		final Vector3f basisX = new Vector3f(1, 0, 0);
		final Vector3f basisZ = new Vector3f(0, 0, 1);
		worldMatrix.transformDirection(basisX);
		worldMatrix.transformDirection(basisZ);

		// Map XZ components to 2D affine
		final double m00 = basisX.x();
		final double m01 = basisZ.x();
		final double m10 = basisX.z();
		final double m11 = basisZ.z();

		final AffineTransform affine = new AffineTransform(m00, m10, m01, m11, pos.x(), pos.y());
		return affine.createTransformedShape(bounds);
	}

	static Shape getTransformedBounds(final Shape bounds, final Matrix4fc worldMatrix) {
		final Vector3f pos3 = worldMatrix.getTranslation(new Vector3f());
		final Vector2f pos = GeoPlane.XZ.projectToPlane(pos3);

		final Vector3f basisX = new Vector3f(1, 0, 0);
		final Vector3f basisZ = new Vector3f(0, 0, 1);
		worldMatrix.transformDirection(basisX);
		worldMatrix.transformDirection(basisZ);

		// Map XZ components to 2D affine
		final double m00 = basisX.x();
		final double m01 = basisZ.x();
		final double m10 = basisX.z();
		final double m11 = basisZ.z();

		final AffineTransform affine = new AffineTransform(m00, m10, m01, m11, pos.x, pos.y);
		return affine.createTransformedShape(bounds);
	}

	static Shape getTransformedBounds(final Shape bounds, final Transform transform, final Matrix4fc parentMatrix) {
		final Matrix4f worldMatrix = new Matrix4f(parentMatrix);
		if (transform != null) {
			worldMatrix.mulAffine(transform.getMatrix());
		}

		final Vector3f pos3 = worldMatrix.getTranslation(new Vector3f());
		final Vector2f pos = GeoPlane.XZ.projectToPlane(pos3);

		final Vector3f basisX = new Vector3f(1, 0, 0);
		final Vector3f basisZ = new Vector3f(0, 0, 1);
		worldMatrix.transformDirection(basisX);
		worldMatrix.transformDirection(basisZ);

		// Map XZ components to 2D affine
		final double m00 = basisX.x();
		final double m01 = basisZ.x();
		final double m10 = basisX.z();
		final double m11 = basisZ.z();

		final AffineTransform affine = new AffineTransform(m00, m10, m01, m11, pos.x, pos.y);
		return affine.createTransformedShape(bounds);
	}

}
