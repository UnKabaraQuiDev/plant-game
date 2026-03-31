package lu.kbra.plant_game.engine.mesh;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.loader.AnimatedMeshLoader.AnimationData;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;
import lu.kbra.standalone.gameengine.graph.texture.SingleTexture;

public class AnimatedTexturedLoadedMesh extends OffsetLoadedMesh implements TexturedMesh, AnimatedMesh {

	protected SingleTexture texture;
	protected AnimationData animation;

	public AnimatedTexturedLoadedMesh(
			final String name,
			final Material mat,
			final Vector3f origin,
			final SingleTexture texture,
			final AnimationData data,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final JavaAttribArray... attribs) {
		super(name, mat, origin, vertices, indices, attribs);
		this.texture = texture;
		this.animation = data;
	}

	@Override
	public AnimationData getAnimation() {
		return this.animation;
	}

	@Override
	public void setAnimation(final AnimationData a) {
		this.animation = a;
	}

	@Override
	public SingleTexture getTexture() {
		return this.texture;
	}

	@Override
	public void setTexture(final SingleTexture texture) {
		this.texture = texture;
	}

	@Override
	public String toString() {
		return "AnimatedTexturedLoadedMesh@" + System.identityHashCode(this) + " [texture=" + this.texture + ", animation=" + this.animation
				+ ", name=" + this.name + ", vao=" + this.vao + ", vbo=" + this.vbo + ", material=" + this.material + ", vertices="
				+ this.vertices + ", indices=" + this.indices + ", attribs=" + this.attribs + ", vertexCount=" + this.vertexCount
				+ ", indicesCount=" + this.indicesCount + ", boundingBox=" + this.boundingBox + "]";
	}

}
