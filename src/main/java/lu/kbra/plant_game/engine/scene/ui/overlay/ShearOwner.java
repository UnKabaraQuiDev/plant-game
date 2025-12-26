package lu.kbra.plant_game.engine.scene.ui.overlay;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import lu.kbra.standalone.gameengine.utils.geo.GeoAxis;

public interface ShearOwner {

	ShearOwner shearSet(GeoAxis targteAxis, GeoAxis sourceAxis, float factor);

	ShearOwner shearAdd(GeoAxis targteAxis, GeoAxis sourceAxis, float factor);

	ShearOwner shearReset();

	Matrix4f getShearMatrix();

	ShearOwner setShear(final Matrix3f shear);

	Matrix3f getShear();
}
