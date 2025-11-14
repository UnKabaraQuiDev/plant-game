package lu.kbra.plant_game.engine.mesh;

import java.util.Arrays;

import org.joml.Vector3f;

import lu.kbra.plant_game.engine.mesh.loader.AnimatedMeshLoader.AnimationData;
import lu.kbra.standalone.gameengine.cache.attrib.AttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.UIntAttribArray;
import lu.kbra.standalone.gameengine.cache.attrib.Vec3fAttribArray;
import lu.kbra.standalone.gameengine.graph.material.Material;

public class AnimatedLoadedMesh extends OffsetMesh implements AnimatedMesh {

	private AnimationData animationData;

	public AnimatedLoadedMesh(
			String name,
			Material material,
			Vector3f origin,
			AnimationData animationData,
			Vec3fAttribArray vertices,
			UIntAttribArray indices,
			AttribArray... attribs) {
		super(name, material, origin, vertices, indices, attribs);
	}

	@Override
	public AnimationData getAnimation() {
		return animationData;
	}

	@Override
	public void setAnimation(AnimationData animationData) {
		this.animationData = animationData;
	}

	@Override
	public String toString() {
		return "AnimatedLoadedMesh [animationData=" + animationData + ", name=" + name + ", vao=" + vao + ", vbo=" + vbo + ", material="
				+ material + ", vertices=" + vertices + ", indices=" + indices + ", attribs=" + Arrays.toString(attribs) + ", vertexCount="
				+ vertexCount + ", indicesCount=" + indicesCount + ", isValid()=" + isValid() + "]";
	}

}
