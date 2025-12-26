package lu.kbra.plant_game.engine.scene.ui.overlay;

import java.awt.geom.AffineTransform;

import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Matrix4f;

import lu.kbra.standalone.gameengine.utils.geo.GeoAxis;

public interface ShearOwner {

	ShearOwner shearSet(GeoAxis targteAxis, GeoAxis sourceAxis, float factor);

	ShearOwner shearAdd(GeoAxis targteAxis, GeoAxis sourceAxis, float factor);

	ShearOwner shearReset();

	Matrix4f getShearMatrix();

	ShearOwner setShear(final Matrix3f shear);

	Matrix3f getShear();

	default void applyShear(final AffineTransform affine) {
		final Matrix3fc shear = this.getShear();
		// project XZ plane: x' = m00*x + m02*z, z' = m20*x + m22*z
		final AffineTransform shearAffine = new AffineTransform(
				shear.m00(),
				shear.m20(), // m00, m10
				shear.m02(),
				shear.m22(), // m01, m11
				0,
				0 // no translation
		);
		affine.concatenate(shearAffine);
	}
}
