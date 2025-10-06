package lu.kbra.plant_game.engine.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.AnimatedObjectLoader.AnimationData;
import lu.kbra.plant_game.engine.entity.impl.TexturedMesh;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class AnimatedTexturedMesh extends TexturedMesh {

	private AnimationData data;

	public AnimatedTexturedMesh(String name, SingleTexture texture, AnimationData data, Vec3fAttribArray vertices,
			UIntAttribArray indices, AttribArray... attribs) {
		super(name, texture, vertices, indices, attribs);
		this.data = data;
	}

	public Matrix4f computeTransform(Matrix4f target, float fac) {
		final Vector3f pos = new Vector3f();
		data.startPosition().lerp(data.endPosition(), fac, pos);

		final Quaternionf rot = new Quaternionf();
		data.startRotation().slerp(data.endRotation(), fac, rot);

		final Vector3f scale = new Vector3f();
		data.startPosition().lerp(data.endPosition(), fac, scale);

		final Vector3f origin = data.origin();

		target.identity().translate(pos).translate(origin).rotate(rot).scale(scale)
				.translate(new Vector3f(origin).negate());

		return target;
	}

	public AnimationData getData() {
		return data;
	}

}
