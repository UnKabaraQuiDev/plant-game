package lu.kbra.plant_game.engine.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.kbra.plant_game.engine.entity.AnimatedObjectLoader.AnimationData;
import lu.kbra.plant_game.engine.entity.impl.TexturedMesh;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.geom.LoadedMesh;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class AnimatedTexturedMesh extends LoadedMesh implements TexturedMesh, AnimatedMesh {

	protected SingleTexture texture;
	protected AnimationData animation;

	public AnimatedTexturedMesh(String name, Material mat, SingleTexture texture, AnimationData data,
			Vec3fAttribArray vertices, UIntAttribArray indices, AttribArray... attribs) {
		super(name, mat, vertices, indices, attribs);
		this.texture = texture;
		this.animation = data;
	}

	@Override
	public Matrix4f computeTransform(Matrix4f target, float fac) {
		final Vector3f pos = new Vector3f();
		animation.startPosition().lerp(animation.endPosition(), fac, pos);

		final Quaternionf rot = new Quaternionf();
		animation.startRotation().slerp(animation.endRotation(), fac, rot);

		final Vector3f scale = new Vector3f();
		animation.startPosition().lerp(animation.endPosition(), fac, scale);

		final Vector3f origin = animation.origin();

		target.identity().translate(pos).translate(origin).rotate(rot).scale(scale)
				.translate(new Vector3f(origin).negate());

		return target;
	}

	@Override
	public AnimationData getAnimation() {
		return animation;
	}

	@Override
	public void setAnimation(AnimationData a) {
		this.animation = a;
	}

	@Override
	public SingleTexture getTexture() {
		return texture;
	}

	@Override
	public void setTexture(SingleTexture texture) {
		this.texture = texture;
	}

}
