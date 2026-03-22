package lu.kbra.plant_game.engine.scene.world.data;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;

import lu.kbra.standalone.gameengine.geom.BoundingBox;

public interface SunLightOwner {

	Matrix4fc getLightSpaceMatrix();

	Matrix4fc getLightProjectionMatrix();

	Matrix4fc getLightViewMatrix();

	void setAmbientLight(final float ambientLight);

	float getAmbientLight();

	void setLightIntensity(final float lightIntensity);

	float getLightIntensity();

	void setLightDirection(final Vector3f lightDirection);

	default void lightDirectionSet(final Vector3fc lightDirection) {
		if (this.getLightDirection() == null) {
			this.setLightDirection(new Vector3f(lightDirection));
		} else {
			this.getLightDirection().set(lightDirection);
		}
	}

	Vector3f getLightDirection();

	void setLightColor(final Vector3f lightColor);

	Vector3f getLightColor();

	default void lightColorSet(final Vector3fc lightColor) {
		if (this.getLightColor() == null) {
			this.setLightColor(new Vector3f(lightColor));
		} else {
			this.getLightColor().set(lightColor);
		}
	}

	default void recomputeLightMatrices(BoundingBox bb, final Vector3f translation) {
		bb = bb.translated(translation);

		final float near = 1.0f;
		final float far = 200.0f;
		final float distance = 100f;

		final Vector3f center = bb.getCenter();
		final Vector3f lightDir = this.getLightDirection();
		final Vector3f lightPos = new Vector3f(lightDir).mul(distance).add(center);

		final Matrix4f lightView = (Matrix4f) this.getLightViewMatrix();
		lightView.identity().lookAt(lightPos, center, new Vector3f(0, 1, 0));

		final Vector3f[] corners = new Vector3f[8];
		final Vector3f min = bb.getMin();
		final Vector3f max = bb.getMax();
		int i = 0;
		for (int x = 0; x <= 1; x++) {
			for (int y = 0; y <= 1; y++) {
				for (int z = 0; z <= 1; z++) {
					corners[i++] = new Vector3f(x == 0 ? min.x : max.x, y == 0 ? min.y : max.y, z == 0 ? min.z : max.z);
				}
			}
		}

		float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;
		float minZ = Float.POSITIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;

		final Vector4f tmp = new Vector4f();
		for (final Vector3f corner : corners) {
			tmp.set(corner, 1.0f);
			tmp.mul(lightView);

			minX = Math.min(minX, tmp.x);
			maxX = Math.max(maxX, tmp.x);
			minY = Math.min(minY, tmp.y);
			maxY = Math.max(maxY, tmp.y);
			minZ = Math.min(minZ, tmp.z);
			maxZ = Math.max(maxZ, tmp.z);
		}

		final Matrix4f lightProj = (Matrix4f) this.getLightProjectionMatrix();
		lightProj.identity().ortho(minX, maxX, minY, maxY, -maxZ, -minZ); // note: OpenGL lookAt uses negative z

		((Matrix4f) this.getLightSpaceMatrix()).set(lightProj).mul(lightView);
	}
}
