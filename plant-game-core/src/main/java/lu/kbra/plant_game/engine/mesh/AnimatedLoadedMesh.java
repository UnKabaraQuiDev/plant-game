package lu.kbra.plant_game.engine.mesh;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.loader.AnimatedMeshLoader.AnimationData;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.impl.JavaAttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class AnimatedLoadedMesh extends OffsetLoadedMesh implements AnimatedMesh {

	private AnimationData animationData;

	public AnimatedLoadedMesh(
			final String name,
			final Material material,
			final Vector3f origin,
			final AnimationData animationData,
			final Vec3fAttribArray vertices,
			final UIntAttribArray indices,
			final JavaAttribArray... attribs) {
		super(name, material, origin, vertices, indices, attribs);
	}

	@Override
	public AnimationData getAnimation() {
		return this.animationData;
	}

	@Override
	public void setAnimation(final AnimationData animationData) {
		this.animationData = animationData;
	}

	@Override
	public String toString() {
		return "AnimatedLoadedMesh@" + System.identityHashCode(this) + " [animationData=" + this.animationData + ", name=" + this.name
				+ ", vao=" + this.vao + ", vbo=" + this.vbo + ", material=" + this.material + ", vertices=" + this.vertices + ", indices="
				+ this.indices + ", attribs=" + this.attribs + ", vertexCount=" + this.vertexCount + ", indicesCount=" + this.indicesCount
				+ ", boundingBox=" + this.boundingBox + "]";
	}

}
